package com.example.diabetescarbapp.activities.options;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.diabetescarbapp.R;

public class FollowUpLogActivity extends AppCompatActivity {

    // declare views vars..
    private Button basalInsulinDoseBtn, bolusInsulinDoseBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_up_log);

        // initialize views vars..
        initViews();

        // listening to views events..
        listenViewsEvents();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, OptionsActivity.class));
        finish();
    }

    private void initViews() {
        basalInsulinDoseBtn = findViewById(R.id.btn_basalInsulinDose);
        bolusInsulinDoseBtn = findViewById(R.id.btn_bolusInsulinDose);
    }

    private void listenViewsEvents() {
        basalInsulinDoseBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, BasalInsulinDoseActivity.class));
            finish();
        });

        bolusInsulinDoseBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, BolusInsulinDoseActivity.class));
            finish();
        });
    }
}