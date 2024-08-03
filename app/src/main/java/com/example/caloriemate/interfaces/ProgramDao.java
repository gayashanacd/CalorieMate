package com.example.caloriemate.interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.caloriemate.models.Program;

import java.util.List;

@Dao
public interface ProgramDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long[] insertProgramsFromList(List<Program> programListList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOneProgram(Program program);

    @Query("SELECT * FROM programs WHERE id=:programId")
    Program getProgramById(String programId);

    @Query("SELECT * FROM programs WHERE userId=:userId")
    Program getProgramByUserId(String userId);
}
