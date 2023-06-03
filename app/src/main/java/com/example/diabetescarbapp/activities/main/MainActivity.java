package com.example.diabetescarbapp.activities.main;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.diabetescarbapp.R;
import com.example.diabetescarbapp.activities.options.OptionsActivity;
import com.example.diabetescarbapp.daos.CarbCoefficientDao;
import com.example.diabetescarbapp.daos.MealCarbDao;
import com.example.diabetescarbapp.db.MyDatabase;
import com.example.diabetescarbapp.models.MealCarb;
import com.example.diabetescarbapp.services.SignInInfo;

import java.time.LocalDate;

public class MainActivity extends AppCompatActivity {

    private ImageView settingIv;
    private TextView carbCoefficientTv, totalCarbTv;
    private CardView breakfastView, lunchView, dinnerView, snackView;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settingIv = findViewById(R.id.iv_setting);
        carbCoefficientTv = findViewById(R.id.tv_carbCoefficient);
        totalCarbTv = findViewById(R.id.tv_totalCarb);
        breakfastView = findViewById(R.id.v_breakfast);
        lunchView = findViewById(R.id.v_lunch);
        dinnerView = findViewById(R.id.v_dinner);
        snackView = findViewById(R.id.v_snack);

        settingIv.setOnClickListener(v -> startActivity(new Intent(this, OptionsActivity.class)));

        retrieveData();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void retrieveData() {
        MealCarbDao mealCarbDao = MyDatabase.getInstance(this).mealCarbDao();
        CarbCoefficientDao carbCoefficientDao = MyDatabase.getInstance(this).carbCoefficientDao();

        carbCoefficientDao.getCarbCoefficientValueOfUser(SignInInfo.getUserID())
                .observe(this, carbCoefficient -> {
                    carbCoefficientTv.setText(carbCoefficient + " جرام");
                    mealCarbDao.getMealCarb(SignInInfo.getUserID())
                            .observe(this, mealCarb -> {
                                        if (!isTodayData(mealCarb.date)) {
                                            MyDatabase.databaseWriteExecutor.execute(() -> {
                                                mealCarbDao.reset(SignInInfo.getUserID());
                                            });
                                        }
                                        refreshViews(mealCarb, carbCoefficient);
                                    }
                            );
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean isTodayData(String date) {
        LocalDate now = LocalDate.now();
        LocalDate nowDate = LocalDate.of(now.getYear(), now.getMonth().getValue(), now.getDayOfMonth());
        String[] lastDateStr = date.split(" ");

        LocalDate mealCarbDate = LocalDate.of(
                Integer.parseInt(lastDateStr[2]),
                Integer.parseInt(lastDateStr[1]),
                Integer.parseInt(lastDateStr[0])
        );

        return mealCarbDate.isEqual(nowDate);
    }

    private void refreshViews(MealCarb mealCarb, int carbCoefficient) {
        refreshView(breakfastView, Meal.BREAKFAST, mealCarb.breakfast, carbCoefficient);
        refreshView(lunchView, Meal.LUNCH, mealCarb.lunch, carbCoefficient);
        refreshView(dinnerView, Meal.DINNER, mealCarb.dinner, carbCoefficient);
        refreshView(snackView, Meal.SNACK, mealCarb.snack, carbCoefficient);

        int totalCarb = mealCarb.breakfast + mealCarb.lunch + mealCarb.dinner + mealCarb.snack;
        totalCarbTv.setText(totalCarb + " جرام");
    }

    private void refreshView(View view, Meal meal, int carb, int carbCoefficient) {
        ImageView imageView = view.findViewById(R.id.image);
        TextView nameTv = view.findViewById(R.id.nameTv);
        TextView carbTv = view.findViewById(R.id.carbTv);
        TextView insulinTv = view.findViewById(R.id.insulinTv);

        imageView.setImageResource(meal.getImage());
        nameTv.setText(meal.name);
        carbTv.setText(String.valueOf(carb));

        insulinTv.setText(carb == 0
                ? "جرعة الأنسولين"
                : Math.round(Float.parseFloat(String.valueOf(carb)) / carbCoefficient) + " unit");

        view.setOnClickListener(v -> startActivity(HomeActivity.createIntent(this, meal.ordinal())));
    }

    public enum Meal {
        BREAKFAST("فطور", R.drawable.breakfast),
        LUNCH("غداء", R.drawable.lunch),
        DINNER("عشاء", R.drawable.dinner),
        SNACK("سناك", R.drawable.snack);

        private final String name;
        private final int image;

        Meal(String name, int image) {
            this.name = name;
            this.image = image;
        }

        public String getName() {
            return name;
        }

        public int getImage() {
            return image;
        }
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }
}