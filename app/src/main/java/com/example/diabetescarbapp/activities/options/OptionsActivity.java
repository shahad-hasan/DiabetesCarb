package com.example.diabetescarbapp.activities.options;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.diabetescarbapp.R;
import com.example.diabetescarbapp.activities.auth.AuthActivity;
import com.example.diabetescarbapp.activities.start.StartActivity;
import com.example.diabetescarbapp.daos.UserDao;
import com.example.diabetescarbapp.db.MyDatabase;
import com.example.diabetescarbapp.services.SignInInfo;
import com.example.diabetescarbapp.utils.AlarmManagerBuilder;
import com.example.diabetescarbapp.utils.Request;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class OptionsActivity extends AppCompatActivity {

    // declare views vars..
    private Button aboutUserBtn, followUpLogBtn, correctionDoseBtn, signOutBtn;

    private BottomSheetDialog signOutDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        // initialize views vars..
        initViews();

        // listening to views events..
        listenViewsEvents();
    }

//    @Override
//    public void onBackPressed() {
//        startActivity(new Intent(this, HomeActivity.class));
//        finish();
//    }

    private void initViews() {
        aboutUserBtn = findViewById(R.id.btn_aboutUser);
        followUpLogBtn = findViewById(R.id.btn_followUpLog);
        correctionDoseBtn = findViewById(R.id.btn_correctionDose);
        signOutBtn = findViewById(R.id.btn_signOut);

        signOutDialog = new BottomSheetDialog(this);
        createBottomDialog();
        signOutDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    private void listenViewsEvents() {
        aboutUserBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, AboutUserActivity.class));
            finish();
        });

        followUpLogBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, FollowUpLogActivity.class));
            finish();
        });

        correctionDoseBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, CorrectionDoseActivity.class));
            finish();
        });

        signOutBtn.setOnClickListener(v -> {
            signOutDialog.show();
        });
    }

    private void createBottomDialog() {
        View signOutView = getLayoutInflater().inflate(R.layout.sign_out_dialog, null, false);

        Button yesBtn = signOutView.findViewById(R.id.btn_yes);
        Button noBtn = signOutView.findViewById(R.id.btn_no);

        yesBtn.setOnClickListener(v -> {
            signOut();
            signOutDialog.dismiss();
        });

        noBtn.setOnClickListener(v -> {
            signOutDialog.dismiss();
        });

        signOutDialog.setContentView(signOutView);
    }

    private void signOut() {
        SignInInfo.reset(this);
        AlarmManagerBuilder.getInstance().destroy(this, Request.BASAL_ID);
        AlarmManagerBuilder.getInstance().destroy(this, Request.EXERCISE);
        AlarmManagerBuilder.getInstance().destroy(this, Request.WATER);

        startActivity(new Intent(this, StartActivity.class));
        finish();
    }
}