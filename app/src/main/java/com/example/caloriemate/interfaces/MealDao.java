package com.example.caloriemate.interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import com.example.caloriemate.models.Meal;
import com.example.caloriemate.models.User;

import java.util.List;

@Dao
public interface MealDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long[] insertMealsFromList(List<Meal> mealsList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOneMeal(Meal meal);
}
