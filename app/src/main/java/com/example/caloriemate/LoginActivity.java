package com.example.caloriemate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.caloriemate.databinding.ActivityLoginBinding;
import com.example.caloriemate.databinding.ActivitySignUpBinding;
import com.example.caloriemate.ui.program.ProgramActivityStep1;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        });
    }
}