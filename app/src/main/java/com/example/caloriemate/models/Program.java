package com.example.caloriemate.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.caloriemate.utils.ActivityLevel;
import com.example.caloriemate.utils.ProgramType;

@Entity(tableName = "programs")
public class Program {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    private String id;

    @NonNull
    @ColumnInfo(name = "userId")
    private String userId;

    @NonNull
    @ColumnInfo(name = "gender")
    private String gender;

    @NonNull
    @ColumnInfo(name = "weight")
    private float weight;

    @NonNull
    @ColumnInfo(name = "height")
    private float height;

    @NonNull
    @ColumnInfo(name = "programType")
    private ProgramType programType;

    @NonNull
    @ColumnInfo(name = "activityLevel")
    private ActivityLevel activityLevel;

    @ColumnInfo(name = "programSummary")
    private String programSummary;

    @ColumnInfo(name = "programName")
    private String programName;

    public Program() {
    }

    public Program(@NonNull String id, @NonNull String userId, @NonNull String gender, float weight, float height, @NonNull ProgramType programType, @NonNull ActivityLevel activityLevel, String programSummary, String programName) {
        this.id = id;
        this.userId = userId;
        this.gender = gender;
        this.weight = weight;
        this.height = height;
        this.programType = programType;
        this.activityLevel = activityLevel;
        this.programSummary = programSummary;
        this.programName = programName;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    @NonNull
    public String getGender() {
        return gender;
    }

    public void setGender(@NonNull String gender) {
        this.gender = gender;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    @NonNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    @NonNull
    public ProgramType getProgramType() {
        return programType;
    }

    public void setProgramType(@NonNull ProgramType programType) {
        this.programType = programType;
    }

    @NonNull
    public ActivityLevel getActivityLevel() {
        return activityLevel;
    }

    public void setActivityLevel(@NonNull ActivityLevel activityLevel) {
        this.activityLevel = activityLevel;
    }

    public String getProgramSummary() {
        return programSummary;
    }

    public void setProgramSummary(String programSummary) {
        this.programSummary = programSummary;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }
}
