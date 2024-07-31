package com.example.caloriemate.databases;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.caloriemate.interfaces.ProgramDao;
import com.example.caloriemate.interfaces.UserDao;
import com.example.caloriemate.models.Program;
import com.example.caloriemate.models.User;

@Database(entities = {User.class, Program.class}, version = 1, exportSchema = false)
public abstract class CalarieMateDatabase extends RoomDatabase {
    public abstract UserDao userDAO();
    public abstract ProgramDao programDAO();
}
