package com.example.diabetescarbapp.activities.main;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.SearchView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diabetescarbapp.R;
import com.example.diabetescarbapp.activities.main.items.FoodItem;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder;

import java.util.ArrayList;
import java.util.Locale;

public class FoodsActivity extends AppCompatActivity {

    private SearchView searchView;
    private RecyclerView recyclerView;

    private GroupAdapter<GroupieViewHolder> adapter;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foods);

        searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.recyclerView);

        adapter = new GroupAdapter<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        int meal = getIntent().getExtras().getInt(ARG_MEAL, -1);
        int type = getIntent().getExtras().getInt(ARG_TYPE, -1);
        int id = getIntent().getExtras().getInt(ARG_ID, -1);

        getData(meal, type, id, "");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                getData(meal, type, id, newText);
                return false;
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getData(int meal, int type, int id, String query) {
        if (type == HomeFragment.Type.CATEGORY.ordinal()) {
            ArrayList<Data.Food> foods = Data.getInstance().getCategoryFoods(id);

            adapter.clear();
            foods.stream()
                    .filter(food -> food.getName().toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT)))
                    .forEach(food -> adapter.add(new FoodItem(food, meal)));
        } else if (type == HomeFragment.Type.RESTAURANT.ordinal()) {
            ArrayList<Data.Food> foods = Data.getInstance().getRestaurantFoods(id);

            adapter.clear();
            foods.stream()
                    .filter(food -> food.getName().toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT)))
                    .forEach(food -> adapter.add(new FoodItem(food, meal)));
        }
    }

    private static final String ARG_MEAL = "meal";
    private static final String ARG_TYPE = "type";
    private static final String ARG_ID = "id";

    public static Intent createIntent(Context context, int meal, int type, int id) {
        return new Intent(context, FoodsActivity.class)
                .putExtra(ARG_MEAL, meal)
                .putExtra(ARG_TYPE, type)
                .putExtra(ARG_ID, id);
    }
}