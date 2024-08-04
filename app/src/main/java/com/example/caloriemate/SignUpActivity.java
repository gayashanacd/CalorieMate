package com.example.caloriemate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
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
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        cmDB = Room.databaseBuilder(getApplicationContext(), CalarieMateDatabase.class, "calariemate.db").build();
        Bundle bundle = getIntent().getExtras();
        userId = bundle.getString("NEW_USER_ID");

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
        String _userId, userName, password;

        if(userId.equals("")){
            _userId = generateID();
        }
        else {
            _userId = userId;
        }

        userName = binding.editTextSignUpUsername.getText().toString();
        password = binding.editTextSignUpPassword.getText().toString();

        User user = new User(_userId, userName, password, false);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                cmDB.userDAO().insertOneUser(user);

                SharedPreferences settings = getSharedPreferences("PREFS_CM", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("USERNAME", userName);
                editor.putString("PASSWORD", password);
                editor.putString("USERID", _userId);
                editor.putBoolean("IS_LOGGED", true);
                editor.apply();

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