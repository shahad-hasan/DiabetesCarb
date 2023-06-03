package com.example.diabetescarbapp.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.diabetescarbapp.models.CarbCoefficient;

@Dao
public interface CarbCoefficientDao {
    @Insert
    void insert(CarbCoefficient carbCoefficient);

    @Query("UPDATE carbcoefficients SET value = :value, date = :date WHERE user_id = :userId")
    void update(int userId, int value, String date);

    @Query("SELECT value FROM carbcoefficients WHERE user_id = :userId")
    int getValue(int userId);

    @Query("SELECT value FROM carbcoefficients WHERE user_id = :userId")
    LiveData<Integer> getCarbCoefficientValueOfUser(int userId);

    @Query("SELECT date FROM carbcoefficients WHERE user_id = :userId")
    LiveData<String> getCarbCoefficientDateOfUser(int userId);
}
