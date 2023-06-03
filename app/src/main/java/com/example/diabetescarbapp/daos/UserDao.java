package com.example.diabetescarbapp.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.diabetescarbapp.models.User;

@Dao
public interface UserDao {
    @Insert
    long insert(User user);

    @Query("SELECT * FROM users WHERE id = :userId")
    LiveData<User> getUserById(int userId);

    @Query("SELECT id FROM users WHERE email = :email AND password = :password")
    LiveData<Integer> signIn(String email, String password);

    @Query("SELECT weight FROM users WHERE id = :userId")
    LiveData<Float> getUserWeight(int userId);

    @Query("UPDATE users SET name = :userName, weight = :weight, birth_date = :birthDate WHERE id = :userId")
    void updateUser(int userId, String userName, float weight, String birthDate);

    @Query("UPDATE users SET password = :password WHERE id = :userId")
    void updatePassword(int userId, String password);
}

