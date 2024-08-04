package com.example.caloriemate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.caloriemate.databases.CalarieMateDatabase;
import com.example.caloriemate.databinding.ActivityLoginBinding;
import com.example.caloriemate.databinding.ActivitySignUpBinding;
import com.example.caloriemate.models.Program;
import com.example.caloriemate.models.User;
import com.example.caloriemate.ui.program.ProgramActivityStep1;
import com.example.caloriemate.utils.ActivityLevel;
import com.example.caloriemate.utils.ProgramType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    CalarieMateDatabase calarieMateDatabase;
    String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        calarieMateDatabase = Room.databaseBuilder(getApplicationContext(), CalarieMateDatabase.class, "calariemate.db").build();
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        binding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = binding.editTextLoginUsername.getText().toString();
                password = binding.editTextLoginPassword.getText().toString();

                if (!username.equals("") && !password.equals("")) {
                    validateUser(username, password);
                }
                else{
                    showMessage("Username or password cannot be empty");
                }
            }
        });
    }

    private void validateUser(String userName, String password){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                User user = calarieMateDatabase.userDAO().getUser(userName,password);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (user!=null) {
                            if(user.isAdmin()){

                                SharedPreferences settings = getSharedPreferences("PREFS_CM", 0);
                                // settings = getSharedPreferences("PREFS_CM", 0);
                                SharedPreferences.Editor editor = settings.edit();
                                editor.putString("USERNAME", user.getUserName());
                                editor.putString("PASSWORD", user.getPassword());
                                editor.putString("USERID", user.getId());
                                editor.putBoolean("IS_LOGGED", true);
                                editor.apply();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            }else {
                                Log.d("DB User found : ", user.getUserName());
                                Log.d("CM", "DB User found image : " + user.getImagePath());

                                // save current user info
                                SharedPreferences settings = getSharedPreferences("PREFS_CM", 0);
                                // settings = getSharedPreferences("PREFS_BBW", 0);
                                SharedPreferences.Editor editor = settings.edit();
                                editor.putString("USERNAME", user.getUserName());
                                editor.putString("PASSWORD", user.getPassword());
                                editor.putString("USERID", user.getId());
                                editor.putString("USERIMAGE", user.getImagePath());
                                editor.putBoolean("IS_LOGGED", true);
                                editor.apply();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            }
                        }
                        else{
                            EditText txtUserName = binding.editTextLoginUsername;
                            EditText txtPassword = binding.editTextLoginPassword;
                            txtUserName.setText("");
                            txtPassword.setText("");
                            txtUserName.requestFocus();
                            showMessage("Incorrect username or password. Try again.");
                        }
                    }
                });

            }
        });
    }

    private void showMessage(String message){
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}