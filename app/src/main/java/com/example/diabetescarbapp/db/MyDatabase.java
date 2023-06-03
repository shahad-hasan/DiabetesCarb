package com.example.diabetescarbapp.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.diabetescarbapp.daos.BasalIDDao;
import com.example.diabetescarbapp.daos.BasalID_ReadingDao;
import com.example.diabetescarbapp.daos.BolusID_ReadingDao;
import com.example.diabetescarbapp.daos.CarbCoefficientDao;
import com.example.diabetescarbapp.daos.MealCarbDao;
import com.example.diabetescarbapp.daos.UserDao;
import com.example.diabetescarbapp.models.BasalID;
import com.example.diabetescarbapp.models.BasalID_Reading;
import com.example.diabetescarbapp.models.BolusID_Reading;
import com.example.diabetescarbapp.models.CarbCoefficient;
import com.example.diabetescarbapp.models.MealCarb;
import com.example.diabetescarbapp.models.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kotlin.jvm.Volatile;

@Database(entities = {User.class, MealCarb.class, BasalID_Reading.class, BolusID_Reading.class, CarbCoefficient.class, BasalID.class}, version = 1, exportSchema = false)
public abstract class MyDatabase extends RoomDatabase {

    @Volatile
    private static MyDatabase instance;
    private static final String DATABASE_NAME = "my_database";
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static synchronized MyDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            MyDatabase.class, DATABASE_NAME)
                    .build();
        }
        return instance;
    }

    public abstract UserDao userDao();
    public abstract MealCarbDao mealCarbDao();
    public abstract BasalID_ReadingDao basalID_readingDao();
    public abstract BolusID_ReadingDao bolusID_readingDao();
    public abstract CarbCoefficientDao carbCoefficientDao();
    public abstract BasalIDDao basalIDDao();
}
