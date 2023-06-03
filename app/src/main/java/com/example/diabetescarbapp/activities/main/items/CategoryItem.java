package com.example.diabetescarbapp.activities.main.items;

import android.os.Build;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.diabetescarbapp.R;
import com.example.diabetescarbapp.activities.main.Data;
import com.example.diabetescarbapp.activities.main.FoodsActivity;
import com.example.diabetescarbapp.activities.main.HomeFragment;
import com.xwray.groupie.GroupieViewHolder;
import com.xwray.groupie.Item;

public class CategoryItem extends Item {

    private final Data.Category category;
    private final int meal;

    public CategoryItem(Data.Category category, int meal) {
        this.category = category;
        this.meal = meal;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void bind(@NonNull GroupieViewHolder viewHolder, int position) {
        View view = viewHolder.itemView;
        TextView nameTv = view.findViewById(R.id.nameTv);

        nameTv.setText(category.getName());

        view.setOnClickListener(v -> {
            view.getContext().startActivity(FoodsActivity.createIntent(view.getContext(),
                    meal,
                    HomeFragment.Type.CATEGORY.ordinal(),
                    category.getId()));
        });
    }

    @Override
    public int getLayout() {
        return R.layout.category_item;
    }
}
