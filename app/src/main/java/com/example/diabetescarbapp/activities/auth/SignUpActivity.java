package com.example.diabetescarbapp.activities.auth;

import static com.example.diabetescarbapp.utils.EmailMatch.isValidEmail;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.diabetescarbapp.R;

public class SignUpActivity extends AppCompatActivity {

    // declare views vars..
    private EditText userNameEt, emailEt, passwordEt;
    private Button nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // initialize views vars..
        initViews();

        // listening to views events..
        listenViewsEvents();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, AuthActivity.class));
        finish();
    }

    private void initViews() {
        userNameEt = findViewById(R.id.et_userName);
        emailEt = findViewById(R.id.et_email);
        passwordEt = findViewById(R.id.et_password);
        nextBtn = findViewById(R.id.btn_next);
    }

    private void listenViewsEvents() {
        nextBtn.setOnClickListener(v -> {
            checkInputsData();
        });
    }

    private void checkInputsData() {
        String userName = userNameEt.getText().toString();
        String email = emailEt.getText().toString();
        String password = passwordEt.getText().toString();

        if (userName.trim().isEmpty()) {
            userNameEt.setError("this field is required..");
            userNameEt.requestFocus();
            return;
        }

        if (email.trim().isEmpty()) {
            emailEt.setError("this field is required..");
            emailEt.requestFocus();
            return;
        }

        if (!isValidEmail(email)) {
            emailEt.setError("invalid email..");
            emailEt.requestFocus();
            return;
        }

        if (password.trim().isEmpty()) {
            passwordEt.setError("this field is required..");
            passwordEt.requestFocus();
            return;
        }

        if (password.length() < 8) {
            passwordEt.setError("password must be at least 8 characters..");
            passwordEt.requestFocus();
            return;
        }

        Intent intent = new Intent(this, PerInfoActivity.class);
        Bundle bundle = new Bundle();

        bundle.putString("name", userName);
        bundle.putString("email", email);
        bundle.putString("password", password);

        intent.putExtras(bundle);

        startActivity(intent);
        finish();
    }
}