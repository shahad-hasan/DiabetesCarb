package com.example.diabetescarbapp.utils;

public class EmailMatch {

    public static boolean isValidEmail(String email) {
        return email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.[a-z]+");
    }

}
