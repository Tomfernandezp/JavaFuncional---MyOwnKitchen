package com.example.my_own_kitchen_java_funcional.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;

import static com.example.my_own_kitchen_java_funcional.model.GreenGrocery.time_value;


@Getter
@Setter
@NoArgsConstructor
public class Recipe {

    private String name;
    private HashMap<String,Integer> ingredientes;
    private Integer time;
    private Integer price_time;


    public Recipe(String name, HashMap<String,Integer> ingredientes){
        this.name = name;
        this.ingredientes = ingredientes;
    }

    // --------- Para los casos opcionales -------
    public Recipe(String name, HashMap<String, Integer> ingredientes, Integer time) {
        this.name = name;
        this.ingredientes = ingredientes;
        this.time = time;
        this.price_time = time * time_value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String, Integer> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(HashMap<String, Integer> ingredientes) {
        this.ingredientes = ingredientes;
    }

    public Integer getPrice_time() {
        return price_time;
    }

    public void setPrice_time(Integer price_time) {
        this.price_time = price_time;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public void listRecipe(){
        System.out.println(this.name + ": \n"
                + "Tiempo de realizacion: " + this.time + " minutos\n" +
                "Ingredientes: ");
        this.ingredientes.forEach((key, value) -> {
            System.out.println(key + ": " + value);
        });
    }
}
