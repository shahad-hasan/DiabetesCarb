package com.example.diabetescarbapp.activities.start;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.diabetescarbapp.R;
import com.example.diabetescarbapp.activities.auth.AuthActivity;
import com.example.diabetescarbapp.activities.main.MainActivity;
import com.example.diabetescarbapp.activities.options.OptionsActivity;
import com.example.diabetescarbapp.services.SignInInfo;

public class StartActivity extends AppCompatActivity {

    // declare views vars..
    private Button nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        // initialize views vars..
        initViews();

        // listening to views events..
        listenViewsEvents();
    }

    @Override
    protected void onStart() {
        if (SignInInfo.isSigned(this)) {
//            startActivity(new Intent(this, OptionsActivity.class));
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        super.onStart();
    }

    private void initViews() {
        nextBtn = findViewById(R.id.btn_next);
    }

    private void listenViewsEvents() {
        nextBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, AuthActivity.class));
            finish();
        });
    }
}