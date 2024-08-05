package com.example.caloriemate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.caloriemate.databases.CalarieMateDatabase;
import com.example.caloriemate.databinding.ActivityStartViewBinding;
import com.example.caloriemate.models.Meal;
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

public class StartViewActivity extends AppCompatActivity {

    ActivityStartViewBinding binding;
    List<User> users = new ArrayList<>();
    List<Program> programs = new ArrayList<>();
    List<Meal> meals = new ArrayList<>();
    CalarieMateDatabase calarieMateDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStartViewBinding.inflate(getLayoutInflater());
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

                calarieMateDatabase.mealDao().insertMealsFromList(meals);
                Log.d("DB", meals.size() + " meals added");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        });

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
            readMealsFromCSV();
        } else {
            // other time your app loads
            Log.d("CM", " NOT THE FIRST RUN");
            readUsersFromCSV();
            readProgramsFromCSV();
            readMealsFromCSV();
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
                        // DOB - 1986-08-03
                        eachProgramFields[5],
                        // ProgramType - LOSE_WEIGHT
                        ProgramType.valueOf(eachProgramFields[6]),
                        // ActivityLevel - LOSE_WEIGHT
                        ActivityLevel.valueOf(eachProgramFields[7]),
                        // ProgramSummary -
                        eachProgramFields[8],
                        // ProgramName -
                        eachProgramFields[9],
                        // Target Calories -
                        Double.parseDouble(eachProgramFields[10])

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

    private void readMealsFromCSV() {
        meals = new ArrayList<>();
        String inputLine;
        InputStream inputStream = getResources().openRawResource(R.raw.meals);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        //rest of the file read logic next class

        try{
            if((inputLine = reader.readLine()) !=null){
                // header lin is contained in inputLine
            }
            while ((inputLine = reader.readLine()) != null){
                String[] eachMealFields = inputLine.split(",");
                Meal eachMeal = new Meal(
                        // Id - 00001
                        eachMealFields[0],
                        // UserId - 00001
                        eachMealFields[1],
                        // ProgramId - 00001
                        eachMealFields[2],
                        // MealDate - 2024-08-04
                        eachMealFields[3],
                        // MealDay - Saturday
                        eachMealFields[4],
                        // MealType - Breakfast
                        eachMealFields[5],
                        // MealDescription -
                        eachMealFields[6],
                        // Name -
                        eachMealFields[7],
                        // Calories - 200
                        Double.parseDouble(eachMealFields[8]),
                        // MealObjString -
                        eachMealFields[9]
                );
                meals.add(eachMeal);
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