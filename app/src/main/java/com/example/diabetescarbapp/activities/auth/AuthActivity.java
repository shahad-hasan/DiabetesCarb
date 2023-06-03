package com.example.diabetescarbapp.activities.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.diabetescarbapp.R;
import com.example.diabetescarbapp.activities.start.StartActivity;

public class AuthActivity extends AppCompatActivity {

    // declare views vars..
    private Button signUpBtn, signInBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        // initialize views vars..
        initViews();

        // listening to views events..
        listenViewsEvents();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, StartActivity.class));
        finish();
    }

    private void initViews() {
        signUpBtn = findViewById(R.id.btn_signUp);
        signInBtn = findViewById(R.id.btn_signIn);
    }

    private void listenViewsEvents() {
        signUpBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, SignUpActivity.class));
            finish();
        });

        signInBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        });
    }
}