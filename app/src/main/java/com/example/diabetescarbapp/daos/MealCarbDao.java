package com.example.diabetescarbapp.daos;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.diabetescarbapp.activities.main.MainActivity;
import com.example.diabetescarbapp.activities.main.MainActivity.Meal;
import com.example.diabetescarbapp.models.BasalID_Reading;
import com.example.diabetescarbapp.models.MealCarb;
import com.example.diabetescarbapp.models.User;

import java.time.LocalDate;

@Dao
public interface MealCarbDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MealCarb mealCarb);

    @Query("UPDATE meal_carb SET breakfast = breakfast + :value WHERE user_id = :userId")
    void updateBreakFast(int userId, int value);

    @Query("UPDATE meal_carb SET lunch = lunch + :value WHERE user_id = :userId")
    void updateLunch(int userId, int value);

    @Query("UPDATE meal_carb SET dinner = dinner + :value WHERE user_id = :userId")
    void updateDinner(int userId, int value);

    @Query("UPDATE meal_carb SET snack = snack + :value WHERE user_id = :userId")
    void updateSnack(int userId, int value);

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Transaction
    default void reset(int userId) {
        MealCarb mealCarb = new MealCarb();
        mealCarb.breakfast = mealCarb.lunch = mealCarb.dinner = mealCarb.snack = 0;
        LocalDate now = LocalDate.now();
        mealCarb.date = now.getDayOfMonth() + " " + now.getMonth().getValue() + " " + now.getYear();
        mealCarb.userId = userId;
        insert(mealCarb);
    }

    @Query("SELECT * FROM meal_carb WHERE user_id = :userId")
    LiveData<MealCarb> getMealCarb(int userId);
}

