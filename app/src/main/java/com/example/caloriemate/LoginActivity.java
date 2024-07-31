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
    List<User> users = new ArrayList<>();
    List<Program> programs = new ArrayList<>();
    CalarieMateDatabase calarieMateDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupInitialData();

        calarieMateDatabase = Room.databaseBuilder(getApplicationContext(), CalarieMateDatabase.class, "calariemate.db").build();
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                calarieMateDatabase.userDAO().insertUsersFromList(users);
                Log.d("DB", users.size() + " users added");

                calarieMateDatabase.programDAO().insertProgramsFromList(programs);
                Log.d("DB", programs.size() + " programs added");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        });

        binding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
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

                                SharedPreferences settings = getSharedPreferences("PREFS_BBW", 0);
                                settings = getSharedPreferences("PREFS_BBW", 0);
                                SharedPreferences.Editor editor = settings.edit();
                                editor.putString("USERNAME", user.getUserName());
                                editor.putString("PASSWORD", user.getPassword());
                                editor.putString("USERID", user.getId());
                                editor.putBoolean("IS_LOGGED", true);
                                editor.commit();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            }else {
                                Log.d("DB User found : ", user.getUserName());
                                Log.d("BUBBLEWASH", "DB User found image : " + user.getImagePath());

                                // save current user info
                                SharedPreferences settings = getSharedPreferences("PREFS_BBW", 0);
                                settings = getSharedPreferences("PREFS_BBW", 0);
                                SharedPreferences.Editor editor = settings.edit();
                                editor.putString("USERNAME", user.getUserName());
                                editor.putString("PASSWORD", user.getPassword());
                                editor.putString("USERID", user.getId());
                                editor.putString("USERIMAGE", user.getImagePath());
                                editor.putBoolean("IS_LOGGED", true);
                                editor.commit();

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

    private void setupInitialData() {
        
        boolean isFirstRun = false;
        SharedPreferences settings = getSharedPreferences("PREFS_CM", 0);
        isFirstRun = settings.getBoolean("FIRST_RUN", false);
        if (!isFirstRun) {
            // do the thing for the first time
            Log.d("CM", "FIRST RUN");
            settings = getSharedPreferences("PREFS_CM", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("FIRST_RUN", true);
            editor.commit();
            readUsersFromCSV();
            readProgramsFromCSV();
        } else {
            // other time your app loads
            Log.d("BBL", " NOT THE FIRST RUN");
        }
    }

    private void readProgramsFromCSV() {
        programs = new ArrayList<>();
        String inputLine;
        InputStream inputStream = getResources().openRawResource(R.raw.programs);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        //rest of the file read logic next class

        try{
            if((inputLine = reader.readLine()) !=null){
                // header lin is contained in inputLine
            }
            while ((inputLine = reader.readLine()) != null){
                String[] eachProgramFields = inputLine.split(",");
                Program eachProgram = new Program(
                        // Id - 00001
                        eachProgramFields[0],
                        // UserId - 00001
                        eachProgramFields[1],
                        // Gender - Male
                        eachProgramFields[2],
                        // Weight - 70
                        Float.parseFloat(eachProgramFields[3]),
                        // Height - 170
                        Float.parseFloat(eachProgramFields[4]),
                        // ProgramType - LOSE_WEIGHT
                        ProgramType.valueOf(eachProgramFields[5]),
                        // ActivityLevel - LOSE_WEIGHT
                        ActivityLevel.valueOf(eachProgramFields[6]),
                        // ProgramSummary -
                        eachProgramFields[7],
                        // ProgramName -
                        eachProgramFields[8]
                );
                programs.add(eachProgram);
            }
        }
        catch (IOException ex){
            ex.printStackTrace();
        } finally {
            try{
                inputStream.close();
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }

    private void readUsersFromCSV() {
        //read users from users.csv
        users = new ArrayList<>();
        String inputLine;
        InputStream inputStream = getResources().openRawResource(R.raw.users);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        //rest of the file read logic next class

        try{
            if((inputLine = reader.readLine()) !=null){
                // header lin is contained in inputLine
            }
            while ((inputLine = reader.readLine()) != null){
                String[] eachUserFields = inputLine.split(",");
                User eachUser = new User(eachUserFields[0],eachUserFields[1],eachUserFields[2], Boolean.parseBoolean(eachUserFields[3]));
                users.add(eachUser);
            }
        }
        catch (IOException ex){
            ex.printStackTrace();
        } finally {
            try{
                inputStream.close();
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }
}