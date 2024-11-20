package com.example.my_own_kitchen_java_funcional.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.SplittableRandom;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Create {

    private List<String> foods;

    public Create(List<String> foods) {
        this.foods = foods;
    }

    @Override
    public String toString(){
        String s = "";
        s+= this.foods + " ";
        return s;
    }

    public String toStringSinCorchetes(){
        String s = "";
        for (String food : foods)
            s = s +  " " + food;
        return s;
    }

    public void removeFood(String food){
        this.foods.remove(food);
    }

    public List<String> getFoods() {
        return foods;
    }

    public void setFoods(List<String> foods) {
        this.foods = foods;
    }
}
