package com.example.caloriemate.ui.program;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.caloriemate.R;
import com.example.caloriemate.StartViewActivity;
import com.example.caloriemate.databinding.ActivityProgramStep1Binding;
import com.example.caloriemate.databinding.ActivityStartViewBinding;

public class ProgramActivityStep1 extends AppCompatActivity {
    ActivityProgramStep1Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProgramStep1Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonProgramStep1Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProgramActivityStep1.this, ProgramActivityStep2.class));
            }
        });
    }
}