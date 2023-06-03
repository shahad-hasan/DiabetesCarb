package com.example.diabetescarbapp.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.diabetescarbapp.models.BasalID_Reading;

import java.util.List;

@Dao
public interface BasalID_ReadingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(BasalID_Reading basalInsulinDose);

    @Query("SELECT * FROM basal_id_readings WHERE user_id = :userId AND date = :date")
    LiveData<BasalID_Reading> getBasalInsulinDosesForUserAt(int userId, String date);
}
