package com.example.caloriemate.interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.caloriemate.models.User;

import java.util.List;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long[] insertUsersFromList(List<User> usersList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOneUser(User user);

    @Query("UPDATE users SET password = :newPassword WHERE id = :userId")
    void updatePassword(String userId, String newPassword);

    @Query("UPDATE users SET password = :password, username = :userName, firstName = :firstName, lastName = :lastName WHERE id = :userId")
    void updateUser(String userId, String userName, String password, String firstName, String lastName);

    @Query("SELECT * FROM users WHERE username= :userName AND password= :password")
    User getUser(String userName, String password);

    @Query("SELECT * FROM users WHERE id=:userId")
    User getUserById(String userId);

    @Query("SELECT * FROM users WHERE username= :userName")
    User getUserByUserName(String userName);
}
