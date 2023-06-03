package com.example.diabetescarbapp.activities.options;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.diabetescarbapp.R;
import com.example.diabetescarbapp.daos.CarbCoefficientDao;
import com.example.diabetescarbapp.db.MyDatabase;
import com.example.diabetescarbapp.services.SignInInfo;

public class CorrectionDoseActivity extends AppCompatActivity {

    // declare views vars..
    private EditText currentValueEt, objectiveValueEt;
    private TextView doseValueTv;
    private Button calculateBtn;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_correction_dose);

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
        currentValueEt = findViewById(R.id.et_currentValue);
        objectiveValueEt = findViewById(R.id.et_objectiveValue);
        doseValueTv = findViewById(R.id.tv_doseValue);
        calculateBtn = findViewById(R.id.btn_calculate);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void listenViewsEvents() {
        calculateBtn.setOnClickListener(v -> {
            checkInputsData();
        });
    }

    private void checkInputsData() {
        String currentValue = currentValueEt.getText().toString();
        String objectiveValue = objectiveValueEt.getText().toString();

        if (currentValue.trim().isEmpty()) {
            currentValueEt.setError("this field is required..");
            currentValueEt.requestFocus();
            return;
        }

        if (objectiveValue.trim().isEmpty()) {
            objectiveValueEt.setError("this field is required..");
            objectiveValueEt.requestFocus();
            return;
        }

        calculateDoseValue(Float.parseFloat(currentValue), Float.parseFloat(objectiveValue));
    }

    private void refreshViews(long doseValue) {
        doseValueTv.setText(doseValue + (doseValue > 1 ? "Units" : "Unit"));
    }

    private void calculateDoseValue(float currentValue, float objectiveValue) {
        MyDatabase.getInstance(this).userDao().getUserWeight(SignInInfo.getUserID()).observe(this, weight -> {
            double correctionCoefficient = 1700 / (0.55 * weight);
            double doseValue = (currentValue - objectiveValue) / correctionCoefficient;
            refreshViews(Math.round(doseValue));
        });
    }
}