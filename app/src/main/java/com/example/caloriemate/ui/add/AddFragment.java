package com.example.caloriemate.ui.add;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.caloriemate.LoginActivity;
import com.example.caloriemate.MainActivity;
import com.example.caloriemate.adapters.MealItemsAdapter;
import com.example.caloriemate.databinding.FragmentAddBinding;
import com.example.caloriemate.databinding.FragmentDashboardBinding;
import com.example.caloriemate.utils.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddFragment extends Fragment {

    private FragmentAddBinding binding;
    private RequestQueue requestQueue;
    private static final String API_KEY = "J7maQHP30d/j0ZHmagDdvw==0Xl99C7CIu04hBuT";
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private MealItemsAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AddViewModel addViewModel =
                new ViewModelProvider(this).get(AddViewModel.class);

        binding = FragmentAddBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.buttonAddMealAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMealNutritions(binding.editTextAddMealVal.getText().toString());
            }
        });

//        final TextView textView = binding.textDashboard;
//        addViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
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
                            JSONArray jsonArray = response.getJSONArray("items");
                            List<JSONObject> jsonObjectList = JsonParser.parseJsonArray(String.valueOf(jsonArray));
                            adapter = new MealItemsAdapter(jsonObjectList, requireContext());
                            recyclerView.setAdapter(adapter);

                            // for (int i = 0; i < jsonArray.length(); i ++){
//                                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                                String title = jsonObject.getString("title");
//                                String overview = jsonObject.getString("overview");
//                                double rating = jsonObject.getDouble("vote_average");
//                                String imgUrl = "https://image.tmdb.org/t/p/w300" + jsonObject.getString("poster_path");

//                                Movie movie = new Movie(title, imgUrl, rating, overview);
//                                movieList.add(movie);
                            // }
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }

//                        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(MainActivity.this, movieList);
//                        recyclerView.setAdapter(recyclerAdapter);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}