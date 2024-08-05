package com.example.caloriemate.ui.dashboard;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import com.example.caloriemate.databases.CalarieMateDatabase;
import com.example.caloriemate.databinding.FragmentDashboardBinding;
import com.example.caloriemate.models.Meal;
import com.example.caloriemate.models.Program;
import com.example.caloriemate.ui.program.ProgramActivityStep2;
import com.example.caloriemate.utils.ProgramType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    CalarieMateDatabase cmDB;
    Program currentProgram;
    SharedPreferences settings;
    double breakfastCals, lunchCals, dinnerCals, snackCals, totalDailyRemCals, completionPerc;
    String formattedDate, userId;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        cmDB = Room.databaseBuilder(requireContext(), CalarieMateDatabase.class, "calariemate.db").build();

        settings =  requireActivity().getSharedPreferences("PREFS_CM", 0);
        userId = settings.getString("USERID", "");
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        formattedDate = currentDate.format(formatter);
        fetchData();

        binding.buttonPreviousDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                formattedDate = getPlusMinusDates("minus");
                fetchData();
            }
        });

        binding.buttonNextDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                formattedDate = getPlusMinusDates("plus");
                if(isFutureDate(formattedDate)){
                    Toast.makeText(requireActivity(), "Selected date is a future date !", Toast.LENGTH_SHORT).show();
                }
                else {
                    fetchData();
                }
            }
        });

        return root;
    }

    public static boolean isFutureDate(String dateString) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate givenDate = LocalDate.parse(dateString, formatter);
            LocalDate currentDate = LocalDate.now();
            return givenDate.isAfter(currentDate);
        } catch (DateTimeParseException e) {
            // Handle invalid date format
            System.err.println("Invalid date format: " + dateString);
            return false;
        }
    }

    private void fetchData(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                currentProgram = cmDB.programDAO().getProgramByUserId(userId);

                // get total calories for all meals by type for today
                breakfastCals = cmDB.mealDao().getAllMealsByType(userId, "Breakfast", formattedDate);
                lunchCals = cmDB.mealDao().getAllMealsByType(userId, "Lunch", formattedDate);
                dinnerCals = cmDB.mealDao().getAllMealsByType(userId, "Dinner", formattedDate);
                snackCals = cmDB.mealDao().getAllMealsByType(userId, "Snacks", formattedDate);

                // calculate total remaining cals
                double totalDailyCals = breakfastCals + lunchCals + dinnerCals + snackCals;
                totalDailyRemCals = ProgramActivityStep2.roundUp(currentProgram.getTargetCal() - totalDailyCals, 2);
                completionPerc = ProgramActivityStep2.roundUp((totalDailyCals / currentProgram.getTargetCal() * 100), 1);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setData();
                    }
                });
            }
        });
    }

    private String getPlusMinusDates(String position){
        // Parse the date string to LocalDate
        LocalDate date = LocalDate.parse(formattedDate);

        LocalDate plusOneDay = date.plusDays(1);
        LocalDate minusOneDay = date.minusDays(1);

        // Format the dates back to strings
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String plusOneDayString = plusOneDay.format(formatter);
        String minusOneDayString = minusOneDay.format(formatter);

        if(position.equals("plus")){
            return plusOneDayString;
        }
        else {
            return minusOneDayString;
        }
    }

    private void setData() {
        // set program in shared preferences
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("PROGRAMID", currentProgram.getId());
        editor.apply();

        // set current date
        binding.textViewDateSelector.setText(formattedDate);

        // Set calories
        binding.textViewBreakfastCalories.setText(String.valueOf(breakfastCals));
        binding.textViewLunchCalories.setText(String.valueOf(lunchCals));
        binding.textViewDinnerCalories.setText(String.valueOf(dinnerCals));
        binding.textViewSnacksCalories.setText(String.valueOf(snackCals));
        binding.textViewLeftCalories.setText(String.valueOf(totalDailyRemCals));
        binding.textViewCaloriePercentage.setText(String.valueOf(completionPerc) + " %");

        // update weight goal data
        float futureWeight = 0;
        if(currentProgram.getProgramType() == ProgramType.LOSE_WEIGHT){
            futureWeight =  currentProgram.getWeight() - 4;
        }
        else {
            futureWeight = currentProgram.getWeight()  + 4;
        }
        binding.textViewDashboardWeightFuture.setText((int) futureWeight + " kg");
        binding.textViewDashboardWeightNow.setText((int) currentProgram.getWeight() + " kg");
        binding.textViewDashboardFutureWeightDate.setText(get2MonthDate());
        binding.textViewCalorieBudgetValue.setText(String.valueOf(currentProgram.getTargetCal()));
    }

    public static String get2MonthDate() {
        LocalDate currentDate = LocalDate.now();
        LocalDate futureDate = currentDate.plusMonths(2);
        String month = futureDate.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        int day = futureDate.getDayOfMonth();
        return month + " " + day;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}