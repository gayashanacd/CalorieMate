package com.example.caloriemate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.caloriemate.databases.CalarieMateDatabase;
import com.example.caloriemate.databinding.ActivitySignUpBinding;
import com.example.caloriemate.databinding.ActivityStartViewBinding;
import com.example.caloriemate.models.User;
import com.example.caloriemate.ui.program.ProgramActivityStep1;

import java.security.SecureRandom;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;
    CalarieMateDatabase cmDB;
    private static final String CHARACTERS = "0123456789";
    private static final int ID_LENGTH = 5;
    private static final SecureRandom random = new SecureRandom();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        cmDB = Room.databaseBuilder(getApplicationContext(), CalarieMateDatabase.class, "calariemate.db").build();

        binding.buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
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

    private void createUser(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        User user = new User(generateID(), binding.editTextSignUpUsername.getText().toString(), binding.editTextSignUpPassword.getText().toString());
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                cmDB.userDAO().insertOneUser(user);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Successfully created the user !", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}