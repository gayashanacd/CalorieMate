package com.example.caloriemate.ui.add;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.caloriemate.LoginActivity;
import com.example.caloriemate.MainActivity;
import com.example.caloriemate.R;
import com.example.caloriemate.adapters.MealItemsAdapter;
import com.example.caloriemate.databases.CalarieMateDatabase;
import com.example.caloriemate.databinding.FragmentAddBinding;
import com.example.caloriemate.databinding.FragmentDashboardBinding;
import com.example.caloriemate.models.Meal;
import com.example.caloriemate.models.User;
import com.example.caloriemate.utils.JsonParser;
import com.example.caloriemate.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddFragment extends Fragment {

    private FragmentAddBinding binding;
    CalarieMateDatabase cmDB;
    private RequestQueue requestQueue;
    private static final String API_KEY = "J7maQHP30d/j0ZHmagDdvw==0Xl99C7CIu04hBuT";
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private MealItemsAdapter adapter;
    private static final String CHARACTERS = "0123456789";
    private static final int ID_LENGTH = 5;
    private static final SecureRandom random = new SecureRandom();
    JSONArray jsonArray;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AddViewModel addViewModel =
                new ViewModelProvider(this).get(AddViewModel.class);

        binding = FragmentAddBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        cmDB = Room.databaseBuilder(requireContext(), CalarieMateDatabase.class, "calariemate.db").build();
        requestQueue = VolleySingleton.getInstance(requireContext()).getRequestQueue();

        recyclerView = binding.recyclerViewAddMealItems;
        layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);

        binding.buttonAddMealAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMealNutritions(binding.editTextAddMealVal.getText().toString());
            }
        });

        return root;
    }

    private void getMealNutritions(String mealText) {
        String url = "https://api.calorieninjas.com/v1/nutrition?query=" + mealText;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the response
                        try {
                            jsonArray = response.getJSONArray("items");
                            // Log.d("DATA_CAME", jsonArray.toString());
                            List<JSONObject> jsonObjectList = JsonParser.parseJsonArray(String.valueOf(jsonArray));
                            adapter = new MealItemsAdapter(jsonObjectList, requireContext());
                            recyclerView.setAdapter(adapter);
                            saveMeal();
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle the error
                        Toast.makeText(requireContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("X-Api-Key", API_KEY);
                return headers;
            }
        };

        // Add the request to the RequestQueue.
        requestQueue.add(jsonObjectRequest);
    }

    public static String generateID() {
        StringBuilder sb = new StringBuilder(ID_LENGTH);
        for (int i = 0; i < ID_LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(randomIndex));
        }
        return sb.toString();
    }

    private void saveMeal() throws JSONException {
        SharedPreferences settings =  requireActivity().getSharedPreferences("PREFS_CM", 0);
        String userId = settings.getString("USERID", "");
        String programId = settings.getString("PROGRAMID", "");
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);
        DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
        String dayName = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault());

        int mealPosition = binding.spinnerAddMealType.getSelectedItemPosition();
        String mealType = "";

        if(mealPosition == 0){
            mealType = "Breakfast";
        }
        else if(mealPosition == 1){
            mealType = "Lunch";
        }
        else if(mealPosition == 2){
            mealType = "Dinner";
        }
        else if(mealPosition == 3){
            mealType = "Snacks";
        }

        for (int i = 0; i < jsonArray.length(); i ++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String name = jsonObject.getString("name");
            String calories = jsonObject.getString("calories");

            Meal meal = new Meal(generateID(), userId, programId, formattedDate, dayName, mealType, binding.editTextAddMealVal.getText().toString(), name, Double.parseDouble(calories), jsonObject.toString());

            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    cmDB.mealDao().insertOneMeal(meal);
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(requireActivity(), "Successfully added a meal !", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}