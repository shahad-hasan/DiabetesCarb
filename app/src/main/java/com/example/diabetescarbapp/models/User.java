package com.example.diabetescarbapp.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    @ColumnInfo(name = "birth_date")
    public String birthDate;
    public float weight;
    public String email;
    public String password;
}
