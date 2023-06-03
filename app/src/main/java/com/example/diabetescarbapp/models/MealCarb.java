package com.example.diabetescarbapp.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(tableName = "meal_carb",
        primaryKeys = {"user_id"},
        foreignKeys = @ForeignKey(entity = User.class,
                parentColumns = "id",
                childColumns = "user_id",
                onDelete = ForeignKey.CASCADE),
        indices = @Index(value = {"user_id"})
)
public class MealCarb {
    @ColumnInfo(name = "breakfast")
    public int breakfast;
    @ColumnInfo(name = "lunch")
    public int lunch;
    @ColumnInfo(name = "dinner")
    public int dinner;
    @ColumnInfo(name = "snack")
    public int snack;
    @ColumnInfo(name = "date")
    public String date;

    @ColumnInfo(name = "user_id")
    public int userId;
}
