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

    @NonNull
    @ColumnInfo(name = "mealDate")
    private String mealDate;

    @ColumnInfo(name = "mealDay")
    private String mealDay;

    @ColumnInfo(name = "mealType")
    private String mealType;

    @ColumnInfo(name = "mealDescription")
    private String mealDescription;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "calories")
    private double calories;

    @ColumnInfo(name = "mealObjString")
    private String mealObjString;

    public Meal(@NonNull String id, @NonNull String userId, String programId, @NonNull String mealDate, String mealDay, String mealType, String mealDescription, String name, double calories, String mealObjString) {
        this.id = id;
        this.userId = userId;
        this.programId = programId;
        this.mealDate = mealDate;
        this.mealDay = mealDay;
        this.mealType = mealType;
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
    public String getMealDate() {
        return mealDate;
    }

    public String getMealDay() {
        return mealDay;
    }

    public void setMealDay(String mealDay) {
        this.mealDay = mealDay;
    }

    public void setMealDate(@NonNull String mealDate) {
        this.mealDate = mealDate;
    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
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
