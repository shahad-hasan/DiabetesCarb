package com.example.diabetescarbapp.activities.main.items;

import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.diabetescarbapp.R;
import com.example.diabetescarbapp.activities.main.Data;
import com.example.diabetescarbapp.activities.main.FoodsActivity;
import com.example.diabetescarbapp.activities.main.HomeFragment;
import com.xwray.groupie.GroupieViewHolder;
import com.xwray.groupie.Item;

public class RestaurantItem extends Item {

    private final Data.Restaurant restaurant;
    private final int meal;

    public RestaurantItem(Data.Restaurant restaurant, int meal) {
        this.meal = meal;
        this.restaurant = restaurant;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void bind(@NonNull GroupieViewHolder viewHolder, int position) {
        View view = viewHolder.itemView;
        TextView nameTv = view.findViewById(R.id.nameTv);
        ImageView imageView = view.findViewById(R.id.image);

        nameTv.setText(restaurant.getName());
        imageView.setImageResource(restaurant.getImage());

        view.setOnClickListener(v -> {
            view.getContext().startActivity(FoodsActivity.createIntent(view.getContext(),
                    meal,
                    HomeFragment.Type.RESTAURANT.ordinal(),
                    restaurant.getId()));
        });
    }

    @Override
    public int getLayout() {
        return R.layout.restaurant_item;
    }
}
