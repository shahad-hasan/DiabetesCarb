package com.example.diabetescarbapp.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.diabetescarbapp.models.BasalID;

@Dao
public interface BasalIDDao {
    @Insert
    void insert(BasalID basalID);

    @Query("UPDATE basalID SET value = value + :extraValue, date = :date WHERE user_id = :userId")
    void update(int userId, int extraValue, String date);

    @Query("UPDATE basalID SET time = :time WHERE user_id = :userId")
    void updateTime(int userId, String time);

    @Query("SELECT * FROM basalID WHERE user_id = :userId")
    LiveData<BasalID> getCarbCoefficientDaoOfUser(int userId);

    @Query("SELECT time FROM basalID WHERE user_id = :userId")
    LiveData<String> getTimeOfUser(int userId);
}
