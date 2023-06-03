package com.example.diabetescarbapp.activities.main;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.diabetescarbapp.R;

import java.util.ArrayList;

public class Data {

    private static ArrayList<Restaurant> restaurants;
    private static ArrayList<Category> categories;

    private static Data instance = null;

    private Data() {}

    public static Data getInstance() {
        if (instance == null) {
            instance = new Data();
            instance.initRestaurantsData();
            instance.initCategoriesData();
        }
        return instance;
    }

    private void initRestaurantsData() {
        restaurants = new ArrayList<>();

        //region Albaik
        Restaurant restaurant1 = new Restaurant(1, "البيك", R.drawable.albaik);

        Food food1 = new Food(1, "وجبة دجاج البيك", R.drawable.broast, 300, 28);
        Food food2 = new Food(2, "ساندوتش مسحب", R.drawable.chicken_fillet_sandwich, 150, 40);
        Food food3 = new Food(3, "صحن دجاج مسحب", R.drawable.seven_piece_chicken_nuggets_meal, 250, 27);

        restaurant1.getFoods().add(food1);
        restaurant1.getFoods().add(food2);
        restaurant1.getFoods().add(food3);
        //endregion


        //region McDonald's
        Restaurant restaurant2 = new Restaurant(2, "ماكدونالدز", R.drawable.mc_donalds);

        Food food4 = new Food(4, "بيج تيستي", R.drawable.big_tasty_sandwich, 364, 59);
        Food food5 = new Food(5, "ناجت", R.drawable.chicken_mc_nuggets, 64, 12);
        Food food6 = new Food(6, "ماك تشيكن", R.drawable.mc_chicken_sandwich, 177, 47);

        restaurant2.getFoods().add(food4);
        restaurant2.getFoods().add(food5);
        restaurant2.getFoods().add(food6);
        //endregion


        //region Kudu
        Restaurant restaurant3 = new Restaurant(3, "كودو", R.drawable.meal1);

        Food food7 = new Food(7, "كودو فيليه لحم", R.drawable.lafino_chicken_sandwich, 100, 82);
        Food food8 = new Food(8, "لفينو دجاج", R.drawable.lafino_philly_steak_sandwich, 90, 62);
        Food food9 = new Food(9, "وجبه دجاج مع رز", R.drawable.chicken_meal_with_rice, 145, 40);

        restaurant3.getFoods().add(food7);
        restaurant3.getFoods().add(food8);
        restaurant3.getFoods().add(food9);
        //endregion

        restaurants.add(restaurant1);
        restaurants.add(restaurant2);
        restaurants.add(restaurant3);
    }


    private void initCategoriesData() {
        categories = new ArrayList<>();

        //region Bread
        Category category1 = new Category(1, "خبز");

        Food food1 = new Food(10, "تورتيلا", R.drawable.tortilla, 65, 35);
        Food food2 = new Food(11, "صامولي", R.drawable.finger_roll, 41, 21);
        Food food3 = new Food(12, "توست", R.drawable.toast, 22, 9);

        category1.foods.add(food1);
        category1.foods.add(food2);
        category1.foods.add(food3);
        //endregion


        //region Rice
        Category category2 = new Category(2, "رز");

        Food food4 = new Food(13, "رز بسمتي", R.drawable.basmati_rice, 32, 10);
        Food food5 = new Food(14, "رز بني", R.drawable.brown_rice, 30, 9);
        Food food6 = new Food(15, "كينوا", R.drawable.quinoa_rice, 85, 16);

        category2.foods.add(food4);
        category2.foods.add(food5);
        category2.foods.add(food6);
        //endregion


        //region Fruits
        Category category3 = new Category(3, "فواكه");

        Food food7 = new Food(16, "فراولة", R.drawable.strawberries, 80, 5);
        Food food8 = new Food(17, "موز", R.drawable.banana, 97, 13);
        Food food9 = new Food(18, "تفاح", R.drawable.apple, 85, 9);

        category3.foods.add(food7);
        category3.foods.add(food8);
        category3.foods.add(food9);
        //endregion


        //region Sandwiches
        Category category4 = new Category(4, "ساندوتش");

        Food food10 = new Food(19, "ساندوتش بيض بالمايونيز", R.drawable.egg_mayo_salad_sandwich, 115, 21);
        Food food11 = new Food(20, "ساندوتش بالمربى و الفول السوداني", R.drawable.peanut_butter_jam_sandwich, 80, 37);
        Food food12 = new Food(21, "ساندوتش لحم بالخضار", R.drawable.ham_salad_sandwich, 80, 20);

        category4.foods.add(food10);
        category4.foods.add(food11);
        category4.foods.add(food12);
        //endregion

        categories.add(category1);
        categories.add(category2);
        categories.add(category3);
        categories.add(category4);
    }



    public ArrayList<Category> getCategories() {
        return categories;
    }

    public ArrayList<Restaurant> getRestaurants() {
        return restaurants;
    }

    public ArrayList<Food> getCategoryFoods(int catId) {
        return categories.get(catId-1).foods;
    }

    public ArrayList<Food> getRestaurantFoods(int resId) {
        return restaurants.get(resId-1).foods;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Food getFood(int id) {
        for (Restaurant restaurant : restaurants) {
            for (Food food : restaurant.foods) {
                if (food.id == id) return food;
            }
        }
        for (Category category : categories) {
            for (Food food : category.foods) {
                if (food.id == id) return food;
            }
        }
        return null;
    }

    public static class Restaurant {
        private final int id;
        private final String name;
        private final int image;
        private final ArrayList<Food> foods;

        public Restaurant(int id, String name, int image) {
            this.id = id;
            this.name = name;
            this.image = image;
            this.foods = new ArrayList<>();
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public int getImage() {
            return image;
        }

        public ArrayList<Food> getFoods() {
            return foods;
        }
    }

    public static class Category {
        private final int id;
        private final String name;
        private final ArrayList<Food> foods;

        public Category(int id, String name) {
            this.id = id;
            this.name = name;
            this.foods = new ArrayList<>();
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public ArrayList<Food> getFoods() {
            return foods;
        }
    }

    public static class Food {
        private final int id;
        private final String name;
        private final int image;
        private final int weight;
        private final int carb;

        public Food(int id, String name, int image, int weight, int carb) {
            this.id = id;
            this.name = name;
            this.image = image;
            this.weight = weight;
            this.carb = carb;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public int getImage() {
            return image;
        }

        public int getWeight() {
            return weight;
        }

        public int getCarb() {
            return carb;
        }
    }
}
