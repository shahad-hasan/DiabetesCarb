package com.example.diabetescarbapp.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(tableName = "bolus_id_readings",
        primaryKeys = {"user_id", "date"},
        foreignKeys = @ForeignKey(entity = User.class,
                parentColumns = "id",
                childColumns = "user_id",
                onDelete = ForeignKey.CASCADE),
        indices = @Index(value = {"user_id"})
)
public class BolusID_Reading {
    @ColumnInfo(name = "before_morning")
    public float beforeMorning;
    @ColumnInfo(name = "after_morning")
    public float afterMorning;
    @ColumnInfo(name = "before_noon")
    public float beforeNoon;
    @ColumnInfo(name = "after_noon")
    public float afterNoon;
    @ColumnInfo(name = "before_night")
    public float beforeNight;
    @ColumnInfo(name = "after_night")
    public float afterNight;
    @ColumnInfo(name = "date")
    @NonNull
    public String date;
    @ColumnInfo(name = "user_id")
    public int userId;
}
