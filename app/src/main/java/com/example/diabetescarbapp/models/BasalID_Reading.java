package com.example.diabetescarbapp.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(tableName = "basal_id_readings",
        primaryKeys = {"user_id", "date"},
        foreignKeys = @ForeignKey(entity = User.class,
                parentColumns = "id",
                childColumns = "user_id",
                onDelete = ForeignKey.CASCADE),
        indices = @Index(value = {"user_id"})
)
public class BasalID_Reading {
    @ColumnInfo(name = "before_sleep")
    public float beforeSleep;
    @ColumnInfo(name = "after_sleep")
    public float afterSleep;
    @ColumnInfo(name = "date")
    @NonNull
    public String date;
    @ColumnInfo(name = "user_id")
    public int userId;
}
