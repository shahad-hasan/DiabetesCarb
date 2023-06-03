package com.example.diabetescarbapp.activities.auth;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.diabetescarbapp.R;
import com.example.diabetescarbapp.daos.BasalIDDao;
import com.example.diabetescarbapp.daos.BasalID_ReadingDao;
import com.example.diabetescarbapp.daos.BolusID_ReadingDao;
import com.example.diabetescarbapp.daos.CarbCoefficientDao;
import com.example.diabetescarbapp.daos.MealCarbDao;
import com.example.diabetescarbapp.daos.UserDao;
import com.example.diabetescarbapp.db.MyDatabase;
import com.example.diabetescarbapp.models.BasalID;
import com.example.diabetescarbapp.models.BasalID_Reading;
import com.example.diabetescarbapp.models.CarbCoefficient;
import com.example.diabetescarbapp.models.MealCarb;
import com.example.diabetescarbapp.models.User;
import com.example.diabetescarbapp.utils.Converter;
import com.example.diabetescarbapp.utils.DatePickerBuilder;

import java.time.LocalDate;
import java.time.LocalTime;

public class PerInfoActivity extends AppCompatActivity {

    // declare views vars..
    private EditText weightEt;
    private TextView birthDateTv;
    private Button nextBtn;

    // declare data vars..
    private User user;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_per_info);

        // get extra data..
        getExtraData();

        // initialize views vars..
        initViews();

        // listening to views events..
        listenViewsEvents();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, SignUpActivity.class));
        finish();
    }

    private void getExtraData() {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            String name = bundle.getString("name");
            String email = bundle.getString("email");
            String password = bundle.getString("password");

            if (name != null && email != null && password != null) {
                user = new User();
                user.name = name;
                user.email = email;
                user.password = password;
            }
        }
    }

    private void initViews() {
        weightEt = findViewById(R.id.et_weight);
        birthDateTv = findViewById(R.id.tv_birthDate);
        nextBtn = findViewById(R.id.btn_next);

        DatePickerBuilder.getInstance().build(this, birthDateTv);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void listenViewsEvents() {
        nextBtn.setOnClickListener(v -> {
            checkInputsData();
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void checkInputsData() {
        String weight = weightEt.getText().toString();
        String birthDate = birthDateTv.getText().toString();

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

        user.weight = Float.parseFloat(weight);
        user.birthDate = birthDate;

        saveData();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void saveData() {
        UserDao dao = MyDatabase.getInstance(this).userDao();
        MealCarbDao mealCarbDao = MyDatabase.getInstance(this).mealCarbDao();
        CarbCoefficientDao carbDao = MyDatabase.getInstance(this).carbCoefficientDao();
        BasalIDDao basalIDDao = MyDatabase.getInstance(this).basalIDDao();

        MyDatabase.databaseWriteExecutor.execute(() -> {
            int userId = (int) dao.insert(user);

            CarbCoefficient carbCoefficient = new CarbCoefficient();
            carbCoefficient.userId = userId;
            carbCoefficient.value = Math.round(500f / (0.55f * user.weight));
            carbCoefficient.date = Converter.getStrFromDate(LocalDate.now().plusDays(1));

            carbDao.insert(carbCoefficient);

            BasalID basalID = new BasalID();
            basalID.userId = userId;
            basalID.value = Math.round(0.5f * (0.55f * user.weight));
            basalID.date = Converter.getStrFromDate(LocalDate.now().plusDays(1));
            basalID.time = Converter.getStrFromTime(LocalTime.of(22, 0, 0));

            basalIDDao.insert(basalID);

            MealCarb mealCarb = new MealCarb();

            mealCarb.userId = userId;
            mealCarb.breakfast = mealCarb.lunch = mealCarb.dinner = mealCarb.snack = 0;
            LocalDate now = LocalDate.now();
            mealCarb.date = now.getDayOfMonth() + " " + now.getMonth().getValue() + " " + now.getYear();

            mealCarbDao.insert(mealCarb);

            startActivity(new Intent(this, SignInActivity.class));
            finish();
        });
    }
}
