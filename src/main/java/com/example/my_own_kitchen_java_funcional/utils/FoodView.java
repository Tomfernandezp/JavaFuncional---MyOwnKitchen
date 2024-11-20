package com.example.my_own_kitchen_java_funcional.utils;

import com.example.my_own_kitchen_java_funcional.model.Food;

import java.util.List;
import java.util.Optional;

public class FoodView {

    static final List<String> registerFoodView = List.of("🥑", "🥕", "🍓", "🍅", "🍆", "🌽", "🍌");

    public static Optional<Food> doesExist(String view, float price){
        if(registerFoodView.contains(view)){
            Food newFood = new Food(view,price);
            return Optional.of(newFood);
        }
        else return Optional.empty();
    }
}
