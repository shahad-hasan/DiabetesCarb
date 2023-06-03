package com.example.diabetescarbapp.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(tableName = "CarbCoefficients",
        primaryKeys = {"user_id"},
        foreignKeys = {
                @ForeignKey(
                        entity = User.class,
                        parentColumns = "id",
                        childColumns = "user_id",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {@Index(value = {"user_id"}, unique = true)}
)
public class CarbCoefficient {
    public int value;
    public String date;
    @ColumnInfo(name = "user_id")
    public int userId;
}
