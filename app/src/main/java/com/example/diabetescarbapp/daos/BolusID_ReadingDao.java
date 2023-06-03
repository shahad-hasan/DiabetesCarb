package com.example.diabetescarbapp.daos;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.diabetescarbapp.models.BolusID_Reading;

@Dao
public interface BolusID_ReadingDao {

    @Insert(onConflict = REPLACE)
    void insert(BolusID_Reading bolusInsulinDose);

    @Query("SELECT * FROM bolus_id_readings WHERE user_id = :userId AND date = :date")
    LiveData<BolusID_Reading> getBolusInsulinDosesForUserAt(int userId, String date);

    @Query("SELECT after_noon FROM bolus_id_readings WHERE user_id = :userId AND date = :date")
    LiveData<Float> getAfterNoonReadingAt(int userId, String date);
}
