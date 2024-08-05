package com.example.caloriemate.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "meals")
public class DailyCaloryIntakeTuple {

    @ColumnInfo(name = "mealDate")
    private String mealDate;

    @ColumnInfo(name = "mealDay")
    private String mealDay;

    @ColumnInfo(name = "calories")
    private double calories;

    public DailyCaloryIntakeTuple(String mealDate, String mealDay, double calories) {
        this.mealDate = mealDate;
        this.mealDay = mealDay;
        this.calories = calories;
    }

    public String getMealDate() {
        return mealDate;
    }

    public void setMealDate(String mealDate) {
        this.mealDate = mealDate;
    }

    public String getMealDay() {
        return mealDay;
    }

    public void setMealDay(String mealDay) {
        this.mealDay = mealDay;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }
}
