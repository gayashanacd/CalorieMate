package com.example.caloriemate.ui.dashboard;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import com.example.caloriemate.databases.CalarieMateDatabase;
import com.example.caloriemate.databinding.FragmentDashboardBinding;
import com.example.caloriemate.models.Program;
import com.example.caloriemate.utils.ProgramType;

import java.time.LocalDate;
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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textDashboard;
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        cmDB = Room.databaseBuilder(requireContext(), CalarieMateDatabase.class, "calariemate.db").build();
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        // get current program
        SharedPreferences settings =  requireActivity().getSharedPreferences("PREFS_CM", 0);
        String userId = settings.getString("USERID", "");
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                currentProgram = cmDB.programDAO().getProgramByUserId(userId);
                setData();
            }
        });

        return root;
    }

    private void setData() {

        // update weight goal data
        float futureWeight = 0;
        if(currentProgram.getProgramType() == ProgramType.LOSE_WEIGHT){
            futureWeight =  currentProgram.getWeight() - 4;
        }
        else {
            futureWeight = currentProgram.getWeight()  + 4;
        }
        binding.textViewWeightFuture.setText((int) futureWeight + " kg");
        binding.textViewWeightNow.setText((int) currentProgram.getWeight() + " kg");
        binding.textViewFutureWeightDate.setText(get2MonthDate());

        binding.textViewCalorieBudgetValue.setText((int) currentProgram.getTargetCal());
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