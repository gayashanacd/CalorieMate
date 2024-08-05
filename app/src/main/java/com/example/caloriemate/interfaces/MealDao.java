package com.example.caloriemate.interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.caloriemate.models.DailyCaloryIntakeTuple;
import com.example.caloriemate.models.Meal;
import com.example.caloriemate.models.Program;
import com.example.caloriemate.models.User;

import java.util.List;

@Dao
public interface MealDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long[] insertMealsFromList(List<Meal> mealsList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOneMeal(Meal meal);

    @Query("SELECT SUM(calories) AS tot_cals FROM meals WHERE userId=:userId AND mealType=:mealType AND mealDate=:mealDate")
    double getAllMealsByType(String userId, String mealType, String mealDate);

    @Query("SELECT mealDate, SUM(calories) AS calories from meals WHERE userId=:userId GROUP BY mealDate")
    List<DailyCaloryIntakeTuple> getDailyUsage(String userId);
}
