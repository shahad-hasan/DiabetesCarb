package com.example.diabetescarbapp.activities.options;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.diabetescarbapp.R;
import com.example.diabetescarbapp.daos.UserDao;
import com.example.diabetescarbapp.db.MyDatabase;
import com.example.diabetescarbapp.models.User;
import com.example.diabetescarbapp.services.SignInInfo;
import com.example.diabetescarbapp.utils.DatePickerBuilder;

public class AboutUserActivity extends AppCompatActivity {

    // declare views vars..
    private EditText userNameEt, weightEt;
    private TextView birthDateTv, emailTv;
    private ImageButton editIBtn, cancelIBtn, saveIBtn;
    private Button resetPasswordBtn;

    private Dialog resetPasswordDialog;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_user);

        // initialize views vars..
        initViews();

        // retrieve data..
        retrieveData();

        // listening to views events..
        listenViewsEvents();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, OptionsActivity.class));
        finish();
    }

    private void initViews() {
        userNameEt = findViewById(R.id.et_userName);
        weightEt = findViewById(R.id.et_weight);
        birthDateTv = findViewById(R.id.tv_birthDate);
        emailTv = findViewById(R.id.tv_email);
        editIBtn = findViewById(R.id.ib_edit);
        cancelIBtn = findViewById(R.id.ib_cancel);
        saveIBtn = findViewById(R.id.ib_save);
        resetPasswordBtn = findViewById(R.id.btn_resetPassword);

        DatePickerBuilder.getInstance().build(this, birthDateTv);

        createResetPasswordDialog();

        changeEditingMode(false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void listenViewsEvents() {
        editIBtn.setOnClickListener(v -> {
            changeEditingMode(true);
        });

        cancelIBtn.setOnClickListener(v -> {
            changeEditingMode(false);
            retrieveData();
        });

        saveIBtn.setOnClickListener(v -> {
            checkInputsData();
        });

        resetPasswordBtn.setOnClickListener(v -> {
            resetPasswordDialog.show();
        });
    }

    private void changeEditingMode(boolean mode) {
        userNameEt.setEnabled(mode);
        weightEt.setEnabled(mode);
        birthDateTv.setEnabled(mode);

        editIBtn.setEnabled(!mode);
        cancelIBtn.setEnabled(mode);
        saveIBtn.setEnabled(mode);
    }

    private void refreshViews(User user) {
        userNameEt.setText(user.name);
        weightEt.setText(String.valueOf(user.weight));
        birthDateTv.setText(user.birthDate);
        emailTv.setText(user.email);
    }

    private void createResetPasswordDialog() {
        resetPasswordDialog = new Dialog(this);
        resetPasswordDialog.setCancelable(false);
        resetPasswordDialog.setContentView(R.layout.reset_password_dialog);

        EditText newPasswordEt = resetPasswordDialog.findViewById(R.id.et_newPassword);

        Button cancelBtn = resetPasswordDialog.findViewById(R.id.btn_cancel);
        Button okBtn = resetPasswordDialog.findViewById(R.id.btn_ok);

        cancelBtn.setOnClickListener(v -> {
            newPasswordEt.setText("");
            resetPasswordDialog.dismiss();
        });

        okBtn.setOnClickListener(v -> {
            String newPassword = newPasswordEt.getText().toString();

            if (newPassword.trim().isEmpty()) {
                newPasswordEt.setError("this field is required..");
                newPasswordEt.requestFocus();
                return;
            }

            if (newPassword.length() < 8) {
                newPasswordEt.setError("this field is required..");
                newPasswordEt.requestFocus();
                return;
            }

            updatePassword(newPassword);
            newPasswordEt.setText("");
            resetPasswordDialog.dismiss();
        });
    }

    private void retrieveData() {
        UserDao userDao = MyDatabase.getInstance(this).userDao();
        userDao.getUserById(SignInInfo.getUserID()).observe(this, this::refreshViews);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void checkInputsData() {
        String userName = userNameEt.getText().toString();
        String weight = weightEt.getText().toString();
        String birthDate = birthDateTv.getText().toString();

        if (userName.trim().isEmpty()) {
            userNameEt.setError("this field is required..");
            userNameEt.requestFocus();
            return;
        }

        if (weight.trim().isEmpty()) {
            weightEt.setError("this field is required..");
            weightEt.requestFocus();
            return;
        }

        if (birthDate.trim().isEmpty()) {
            birthDateTv.setError("this field is required..");
            birthDateTv.requestFocus();
            return;
        }

        saveData(userName, weight, birthDate);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void saveData(String userName, String weight, String birthDate) {
        UserDao dao = MyDatabase.getInstance(this).userDao();

        MyDatabase.databaseWriteExecutor.execute(() -> {
            dao.updateUser(SignInInfo.getUserID(), userName, Float.parseFloat(weight), birthDate);
        });

        changeEditingMode(false);
        retrieveData();
    }

    private void updatePassword(String newPassword) {
        UserDao dao = MyDatabase.getInstance(this).userDao();

        MyDatabase.databaseWriteExecutor.execute(() -> {
            dao.updatePassword(SignInInfo.getUserID(), newPassword);
            System.out.println();
        });
    }
}