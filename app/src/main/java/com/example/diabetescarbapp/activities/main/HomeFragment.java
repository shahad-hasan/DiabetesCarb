package com.example.diabetescarbapp.activities.main;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diabetescarbapp.R;
import com.example.diabetescarbapp.activities.main.items.CategoryItem;
import com.example.diabetescarbapp.activities.main.items.RestaurantItem;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder;

import java.util.ArrayList;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private SearchView searchView;
    private RecyclerView recyclerView;

    private GroupAdapter<GroupieViewHolder> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        searchView = view.findViewById(R.id.searchView);
        recyclerView = view.findViewById(R.id.recyclerView);

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new GroupAdapter<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        recyclerView.setAdapter(adapter);

        int meal = getArguments().getInt(ARG_MEAL, -1);
        int type = getArguments().getInt(ARG_TYPE, -1);

        getData(meal, type, "");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                getData(meal, type, newText);
                return false;
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getData(int meal, int type, String query) {
        if (type == Type.CATEGORY.ordinal()) {
            ArrayList<Data.Category> categories = Data.getInstance().getCategories();

            adapter.clear();
            categories.stream()
                    .filter( category -> category.getName().toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT)))
                    .forEach( category -> adapter.add(new CategoryItem(category, meal)));

        } else if (type == Type.RESTAURANT.ordinal()) {
            ArrayList<Data.Restaurant> restaurants = Data.getInstance().getRestaurants();

            adapter.clear();
            restaurants.stream()
                    .filter( restaurant -> restaurant.getName().toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT)))
                    .forEach( restaurant -> adapter.add(new RestaurantItem(restaurant, meal)));
        }
    }


    private static final String ARG_MEAL = "meal";
    private static final String ARG_TYPE = "type";

    public static HomeFragment newInstance(int meal, int type) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MEAL, meal);
        args.putInt(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    public enum Type {
        CATEGORY,
        RESTAURANT;
    }
}