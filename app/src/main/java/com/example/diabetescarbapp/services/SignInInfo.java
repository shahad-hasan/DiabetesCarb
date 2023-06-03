package com.example.diabetescarbapp.services;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

public final class SignInInfo {
    private static int userID;

    public static int getUserID() {
        return userID;
    }

    public static void saveSignInData(Context context, int id) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("sign_in_info", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("id", id);
        editor.apply();
        initSignInData(context);
    }

    private static void initSignInData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("sign_in_info", MODE_PRIVATE);
        userID = sharedPreferences.getInt("id", 0);
    }

    public static boolean isSigned(Context context) {
        initSignInData(context);
        return userID > 0;
    }

    public static void reset(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("sign_in_info", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("id", 0);
        editor.apply();
        initSignInData(context);
    }
}
