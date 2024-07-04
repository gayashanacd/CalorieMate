package com.example.caloriemate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.caloriemate.databinding.ActivityStartViewBinding;
import com.example.caloriemate.ui.program.ProgramActivityStep1;

public class StartViewActivity extends AppCompatActivity {

    ActivityStartViewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStartViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonStartGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartViewActivity.this, ProgramActivityStep1.class));
            }
        });

        binding.buttonStartLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartViewActivity.this, LoginActivity.class));
            }
        });
    }
}