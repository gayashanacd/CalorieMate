package com.example.caloriemate.ui.program;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import com.example.caloriemate.R;
import com.example.caloriemate.StartViewActivity;
import com.example.caloriemate.databinding.ActivityProgramStep1Binding;
import com.example.caloriemate.databinding.ActivityStartViewBinding;
import com.example.caloriemate.utils.ProgramType;

public class ProgramActivityStep1 extends AppCompatActivity {
    ActivityProgramStep1Binding binding;
    String gender;
    String programType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProgramStep1Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.rdGroupProgramStep1Gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.radioButtonProgramStep1GenderFemale){
                    gender = "Female";
                }
                else {
                    gender = "Male";
                }
            }
        });

        binding.rdGroupProgramStep1Type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.radioButtonProgramStep1TypeGain){
                    programType = "GAIN_WEIGHT";
                }
                else {
                    programType = "LOSE_WEIGHT";
                }
            }
        });

        binding.buttonProgramStep1Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("GENDER", gender);
                bundle.putFloat("WEIGHT", Float.parseFloat(binding.editTextProgramStep1Weight.getText().toString()));
                bundle.putFloat("HEIGHT", Float.parseFloat(binding.editTextProgramStep1Height.getText().toString()));
                bundle.putString("DOB", binding.editTextProgramStep1Dob.getText().toString());
                bundle.putString("PROGRAM_TYPE", programType);
                Intent intent = new Intent(ProgramActivityStep1.this, ProgramActivityStep2.class);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });
    }
}