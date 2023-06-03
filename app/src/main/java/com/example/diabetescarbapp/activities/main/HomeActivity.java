package com.example.diabetescarbapp.activities.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.diabetescarbapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        int meal = getIntent().getExtras().getInt(ARG_MEAL);

        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_category:
                    showCategoryFragment(meal, HomeFragment.Type.CATEGORY.ordinal());
                    return true;
                case R.id.menu_restaurant:
                    showCategoryFragment(meal, HomeFragment.Type.RESTAURANT.ordinal());
                    return true;
            }
            return false;
        });

        showCategoryFragment(meal, HomeFragment.Type.CATEGORY.ordinal());
    }

    private void showCategoryFragment(int meal, int type) {
        HomeFragment fragment = HomeFragment.newInstance(meal, type);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    private static final String ARG_MEAL = "meal";

    public static Intent createIntent(Context context, int meal) {
        return new Intent(context, HomeActivity.class)
                .putExtra(ARG_MEAL, meal);
    }
}