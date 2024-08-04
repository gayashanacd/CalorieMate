package com.example.caloriemate.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "meals")
public class Meal {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    private String id;

    @NonNull
    @ColumnInfo(name = "userId")
    private String userId;

    @ColumnInfo(name = "programId")
    private String programId;

    @ColumnInfo(name = "mealDescription")
    private String mealDescription;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "calories")
    private double calories;

    @ColumnInfo(name = "mealObjString")
    private String mealObjString;

    public Meal(@NonNull String id, @NonNull String userId, String programId, String mealDescription, String name, double calories, String mealObjString) {
        this.id = id;
        this.userId = userId;
        this.programId = programId;
        this.mealDescription = mealDescription;
        this.name = name;
        this.calories = calories;
        this.mealObjString = mealObjString;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    @NonNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    public String getProgramId() {
        return programId;
    }

    public void setProgramId(String programId) {
        this.programId = programId;
    }

    public String getMealDescription() {
        return mealDescription;
    }

    public void setMealDescription(String mealDescription) {
        this.mealDescription = mealDescription;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public String getMealObjString() {
        return mealObjString;
    }

    public void setMealObjString(String mealObjString) {
        this.mealObjString = mealObjString;
    }
}
