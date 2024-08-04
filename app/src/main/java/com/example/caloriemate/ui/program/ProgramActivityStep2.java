package com.example.caloriemate.ui.program;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.caloriemate.LoginActivity;
import com.example.caloriemate.R;
import com.example.caloriemate.SignUpActivity;
import com.example.caloriemate.databases.CalarieMateDatabase;
import com.example.caloriemate.databinding.ActivityProgramStep1Binding;
import com.example.caloriemate.databinding.ActivityProgramStep2Binding;
import com.example.caloriemate.models.Program;
import com.example.caloriemate.utils.ActivityLevel;
import com.example.caloriemate.utils.ProgramType;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProgramActivityStep2 extends AppCompatActivity {

    ActivityProgramStep2Binding binding;
    ActivityLevel activityLevel;
    CalarieMateDatabase cmDB;
    Program currentProgram;
    int age;
    String dob, gender, programType, userId;
    float weight, height;
    double bmr, tdee, targetCal;
    private static final String CHARACTERS = "0123456789";
    private static final int ID_LENGTH = 5;
    private static final SecureRandom random = new SecureRandom();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProgramStep2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle bundle = getIntent().getExtras();
        cmDB = Room.databaseBuilder(getApplicationContext(), CalarieMateDatabase.class, "calariemate.db").build();

        binding.rdGroupProgramStep2ActivityLevel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.radioButtonProgramStep2ActivitySedentary){
                    activityLevel = ActivityLevel.valueOf("SEDENTARY");
                }
                else if(checkedId == R.id.radioButtonProgramStep2ActivityLight) {
                    activityLevel = ActivityLevel.valueOf("LIGHTLY");
                }
                else if(checkedId == R.id.radioButtonProgramStep2ActivityModerate) {
                    activityLevel = ActivityLevel.valueOf("MODERATELY");
                }
                else if(checkedId == R.id.radioButtonProgramStep2ActivityVery) {
                    activityLevel = ActivityLevel.valueOf("VERY_ACTIVE");
                }
                else if(checkedId == R.id.radioButtonProgramStep2ActivitySuper) {
                    activityLevel = ActivityLevel.valueOf("SUPER_ACTIVE");
                }
            }
        });

        binding.buttonProgramFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateProfile(bundle);
            }
        });

        binding.buttonCreateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProgram();
                Bundle bundle = new Bundle();
                bundle.putString("NEW_USER_ID", userId);
                Intent intent = new Intent(ProgramActivityStep2.this, SignUpActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        binding.buttonProgramBack.setVisibility(View.INVISIBLE);
        binding.buttonProgramBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProgramActivityStep2.this, ProgramActivityStep1.class));
            }
        });
    }

    public static String generateID() {
        StringBuilder sb = new StringBuilder(ID_LENGTH);
        for (int i = 0; i < ID_LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(randomIndex));
        }
        return sb.toString();
    }

    private void saveProgram(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                cmDB.programDAO().insertOneProgram(currentProgram);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Successfully created the program !", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void generateProfile(Bundle bundle) {
        SharedPreferences settings = getSharedPreferences("PREFS_CM", 0);
        userId = settings.getString("USERID", "");

        if(userId.equals("")){
            userId = generateID();
        }

        dob = bundle.getString("DOB");
        gender = bundle.getString("GENDER");
        age = calculateAge(dob);
        weight = bundle.getFloat("WEIGHT");
        height = bundle.getFloat("HEIGHT");
        programType = bundle.getString("PROGRAM_TYPE");

        float futureWeight = 0;
        if(programType.equals("LOSE_WEIGHT")){
            binding.textViewProgramStep2FinalResultInfo.setText("Lose 0.45kg (1lb) per week.");
            futureWeight = weight - 4;
        }
        else {
            binding.textViewProgramStep2FinalResultInfo.setText("Gain 0.45kg (1lb) per week.");
            futureWeight = weight + 4;
        }
        binding.textViewWeightFuture.setText((int) futureWeight + " kg");
        binding.textViewWeightNow.setText((int) weight + " kg");
        binding.textViewFutureWeightDate.setText(get2MonthDate());

        // determine BMR (Basal Metabolic Rate ) : Mifflin-St Jeor Equation
        if(gender.equals("male")){
            bmr = 10 * weight + 6.25 * height - 5 * age + 5;
        }
        else {
            bmr = 10 * weight + 6.25 * height - 5 * age - 161;
        }

        // Calculate your Total Daily Energy Expenditure (TDEE)
        switch (activityLevel){
            case SEDENTARY:
                tdee = bmr * 1.2;
                break;
            case LIGHTLY:
                tdee = bmr * 1.375;
                break;
            case MODERATELY:
                tdee = bmr * 1.55;
                break;
            case VERY_ACTIVE:
                tdee = bmr * 1.725;
                break;
            case SUPER_ACTIVE:
                tdee = bmr * 1.9;
                break;
        }

        // Set a calorie deficit or surplus
        if(programType.equals("GAIN_WEIGHT")){
            targetCal = tdee + 500;
        }
        else {
            targetCal = tdee - 500;
        }

        targetCal = roundUp(targetCal, 2);
        Log.d("CM-DOB", String.valueOf(targetCal));
        binding.textViewProgramStep2DailyCalorieVal.setText(String.valueOf(targetCal));
        currentProgram = new Program(generateID(), userId, gender, weight, height, dob, ProgramType.valueOf(programType), activityLevel, "", "", targetCal);
    }

    public static double roundUp(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        double scale = Math.pow(10, places);
        return Math.ceil(value * scale) / scale;
    }

    public static String get2MonthDate() {
        LocalDate currentDate = LocalDate.now();
        LocalDate futureDate = currentDate.plusMonths(2);
        String month = futureDate.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        int day = futureDate.getDayOfMonth();
        return month + " " + day;
    }

    public static int calculateAge(String birthDateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birthDate = LocalDate.parse(birthDateString, formatter);
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(birthDate, currentDate);
        return period.getYears();
    }
}