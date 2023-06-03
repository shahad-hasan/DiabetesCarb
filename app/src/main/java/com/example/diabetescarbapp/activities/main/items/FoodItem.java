package com.example.diabetescarbapp.activities.main.items;

import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.diabetescarbapp.R;
import com.example.diabetescarbapp.activities.main.Data;
import com.example.diabetescarbapp.activities.main.FoodActivity;
import com.xwray.groupie.GroupieViewHolder;
import com.xwray.groupie.Item;

public class FoodItem extends Item {

    private final Data.Food food;
    private final int meal;

    public FoodItem(Data.Food food, int meal) {
        this.food = food;
        this.meal = meal;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void bind(@NonNull GroupieViewHolder viewHolder, int position) {
        View view = viewHolder.itemView;
        TextView nameTv = view.findViewById(R.id.nameTv);
        ImageView imageView = view.findViewById(R.id.image);

        nameTv.setText(food.getName());
        imageView.setImageResource(food.getImage());

        view.setOnClickListener(v -> {
            view.getContext().startActivity(FoodActivity.createIntent(view.getContext(),
                    meal,
                    food.getId()));
        });
    }

    @Override
    public int getLayout() {
        return R.layout.food_item;
    }
}
