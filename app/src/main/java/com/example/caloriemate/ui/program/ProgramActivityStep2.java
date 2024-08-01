package com.example.caloriemate.ui.program;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import com.example.caloriemate.LoginActivity;
import com.example.caloriemate.R;
import com.example.caloriemate.SignUpActivity;
import com.example.caloriemate.databinding.ActivityProgramStep1Binding;
import com.example.caloriemate.databinding.ActivityProgramStep2Binding;
import com.example.caloriemate.utils.ActivityLevel;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class ProgramActivityStep2 extends AppCompatActivity {

    ActivityProgramStep2Binding binding;
    ActivityLevel activityLevel;
    int age;
    String dob, gender, programType;
    float weight, height;
    double bmr, tdee, targetCal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProgramStep2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle bundle = getIntent().getExtras();

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
                startActivity(new Intent(ProgramActivityStep2.this, SignUpActivity.class));
            }
        });

        binding.buttonProgramBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProgramActivityStep2.this, ProgramActivityStep1.class));
            }
        });
    }

    private void generateProfile(Bundle bundle) {

        dob = bundle.getString("DOB");
        gender = bundle.getString("GENDER");
        age = calculateAge(dob);
        weight = bundle.getFloat("WEIGHT");
        height = bundle.getFloat("HEIGHT");
        programType = bundle.getString("PROGRAM_TYPE");

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
    }

    public static int calculateAge(String birthDateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birthDate = LocalDate.parse(birthDateString, formatter);
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(birthDate, currentDate);
        return period.getYears();
    }
}