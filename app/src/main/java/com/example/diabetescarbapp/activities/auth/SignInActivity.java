package com.example.diabetescarbapp.activities.auth;

import static com.example.diabetescarbapp.utils.EmailMatch.isValidEmail;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.diabetescarbapp.R;
import com.example.diabetescarbapp.activities.main.MainActivity;
import com.example.diabetescarbapp.activities.options.OptionsActivity;
import com.example.diabetescarbapp.daos.BasalIDDao;
import com.example.diabetescarbapp.daos.UserDao;
import com.example.diabetescarbapp.db.MyDatabase;
import com.example.diabetescarbapp.services.SignInInfo;
import com.example.diabetescarbapp.utils.AlarmManagerBuilder;
import com.example.diabetescarbapp.utils.Converter;
import com.example.diabetescarbapp.utils.Request;

import java.time.LocalTime;

import kotlin.text.Regex;

public class SignInActivity extends AppCompatActivity {

    // declare views vars..
    private EditText emailEt, passwordEt;
    private Button nextBtn;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

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
        emailEt = findViewById(R.id.et_email);
        passwordEt = findViewById(R.id.et_password);
        nextBtn = findViewById(R.id.btn_next);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void listenViewsEvents() {
        nextBtn.setOnClickListener(v -> {
            checkInputsData();
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void checkInputsData() {
        String email = emailEt.getText().toString();
        String password = passwordEt.getText().toString();

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

        signInUser(email, password);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void signInUser(String email, String password) {
        UserDao userDao = MyDatabase.getInstance(this).userDao();

        userDao.signIn(email, password).observe(this, id -> {
            if (id != null && id != 0) {
                // User exists, do something with userId
                SignInInfo.saveSignInData(this, id);

                initAlarmTime();

                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
            else {
                // User doesn't exist, show error message
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initAlarmTime() {
        BasalIDDao dao = MyDatabase.getInstance(this).basalIDDao();

        dao.getTimeOfUser(SignInInfo.getUserID()).observe(this, this::initAlarms);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initAlarms(String timeStr) {
        String[] time = timeStr.split(" ");
        AlarmManagerBuilder.getInstance().build(this,
                Converter.getTimeFromStr(time[0], time[1]),
                Request.BASAL_ID,
                "جرعة طويل المفعول",
                "إنه وقت أخذ جرعة طويل المفعول!!");

        LocalTime now = LocalTime.now();

        AlarmManagerBuilder.getInstance().build(this,
//                Converter.getTimeFromStr("03:35", "AM"),
                now,
                Request.EXERCISE,
                "التمارين",
                "-قوم تنشط سوي تمارينك يصح بدنك"+"\n"+"-لصحتك مارس رياضتك");

        AlarmManagerBuilder.getInstance().build(this,
//                Converter.getTimeFromStr("03:35", "AM"),
                now,
                Request.WATER,
                "الماء",
                "بالماء تستمر الحياة، حافظ على شرب الماء");

        AlarmManagerBuilder.getInstance().build(this,
//                Converter.getTimeFromStr("03:35", "AM"),
                now,
                Request.WATER,
                "مواعيد",
                "حافظ على صحتك ، و احجز موعد للفحوصات");
    }
}
