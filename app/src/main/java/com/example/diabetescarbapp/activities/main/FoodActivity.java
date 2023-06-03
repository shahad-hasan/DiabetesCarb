package com.example.diabetescarbapp.activities.main;

import static com.example.diabetescarbapp.activities.main.Data.getFood;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.diabetescarbapp.R;
import com.example.diabetescarbapp.daos.MealCarbDao;
import com.example.diabetescarbapp.db.MyDatabase;
import com.example.diabetescarbapp.services.SignInInfo;

public class FoodActivity extends AppCompatActivity {

    private ImageView imageView, plusIv, minusIv;
    private TextView nameTv, weightTv, quantityTv, carbTv;
    private Button okBtn;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        imageView = findViewById(R.id.image);
        plusIv = findViewById(R.id.plusIv);
        minusIv = findViewById(R.id.minusIv);
        nameTv = findViewById(R.id.nameTv);
        weightTv = findViewById(R.id.weightTv);
        quantityTv = findViewById(R.id.quantityTv);
        carbTv = findViewById(R.id.carbTv);
        okBtn = findViewById(R.id.btn_ok);

        int meal = getIntent().getExtras().getInt(ARG_MEAL, -1);
        int id = getIntent().getExtras().getInt(ARG_ID, -1);

        getData(meal, id);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getData(int meal, int id) {
        Data.Food food = getFood(id);

        imageView.setImageResource(food.getImage());
        nameTv.setText(food.getName());
        weightTv.setText(food.getWeight() + " جرام");
        carbTv.setText(food.getCarb() + " جرام\nكارب");


        plusIv.setOnClickListener(v -> {
            int quantity = Integer.parseInt(quantityTv.getText().toString());
            quantity += 1;
            quantityTv.setText(String.valueOf(quantity));
            carbTv.setText((food.getCarb()*quantity) + " جرام\nكارب");

        });

        minusIv.setOnClickListener(v -> {
            int quantity = Integer.parseInt(quantityTv.getText().toString());
            if (quantity > 1) quantity -= 1;
            quantityTv.setText(String.valueOf(quantity));
            carbTv.setText((food.getCarb()*quantity) + " جرام\nكارب");
        });

        okBtn.setOnClickListener(v -> {
            int quantity = Integer.parseInt(quantityTv.getText().toString());

            if (quantity == 0) {
                quantityTv.setError("");
                return;
            }

            updateData(meal, quantity*food.getCarb());
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateData(int meal, int value) {
        MealCarbDao dao = MyDatabase.getInstance(this).mealCarbDao();

        int userId = SignInInfo.getUserID();

        MyDatabase.databaseWriteExecutor.execute(() -> {
            if (meal == MainActivity.Meal.BREAKFAST.ordinal()) {
                dao.updateBreakFast(userId, value);
            } else if (meal == MainActivity.Meal.LUNCH.ordinal()) {
                dao.updateLunch(userId, value);
            } else if (meal == MainActivity.Meal.DINNER.ordinal()) {
                dao.updateDinner(userId, value);
            } else if (meal == MainActivity.Meal.SNACK.ordinal()) {
                dao.updateSnack(userId, value);
            }
            finish();
        });
    }

    private static final String ARG_MEAL = "meal";
    private static final String ARG_ID = "id";

    public static Intent createIntent(Context context, int meal, int id) {
        return new Intent(context, FoodActivity.class)
                .putExtra(ARG_MEAL, meal)
                .putExtra(ARG_ID, id);
    }
}