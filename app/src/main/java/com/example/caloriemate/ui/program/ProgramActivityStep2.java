package com.example.caloriemate.ui.program;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.caloriemate.LoginActivity;
import com.example.caloriemate.R;
import com.example.caloriemate.databinding.ActivityProgramStep1Binding;
import com.example.caloriemate.databinding.ActivityProgramStep2Binding;

public class ProgramActivityStep2 extends AppCompatActivity {

    ActivityProgramStep2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProgramStep2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProgramActivityStep2.this, LoginActivity.class));
            }
        });

        binding.buttonProgramBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProgramActivityStep2.this, ProgramActivityStep1.class));
            }
        });
    }
}