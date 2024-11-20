package com.example.my_own_kitchen_java_funcional.model;

import com.example.my_own_kitchen_java_funcional.exception.FoodIsNotInCreateException;
import com.example.my_own_kitchen_java_funcional.exception.FoodNotFoundException;
import com.example.my_own_kitchen_java_funcional.exception.RecipeDoesNotExistException;
import com.example.my_own_kitchen_java_funcional.utils.FoodView;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class GreenGrocery {

    private static GreenGrocery single_instance = null;

    private static List<Food> foods;
    private static List<Recipe> recipes;

    public static final Integer  time_value = 10;

    private static List<Create> creates;

    public GreenGrocery(){
        foods = new ArrayList<Food>();
        recipes = new ArrayList<Recipe>();
        creates = new ArrayList<Create>();
    }

    public static GreenGrocery getInstance(){
        if(single_instance == null )
            single_instance = new GreenGrocery();
        return single_instance;
    }

    //------------------------Food----------------------------------------

    public void addFood(String view, Integer price) throws FoodNotFoundException {
        Optional<Food> optFood = FoodView.doesExist(view,price);
        if(optFood.isPresent()){
            //foods.add(optFood); esto no se puede porque estoy pasando un Optional
            var food = optFood.get(); // tengo que hacelro de esta manera
            foods.add(food);
            System.out.println("La verdura fue agregada exitosamente");
        }
        else throw new FoodNotFoundException("Este alimento no se encuentra dentro de los permitidos");
    }

    public List<Food> getFoods(){
        return foods;
    }

    //-----------------------------Recipe------------------------------------------

    public void addRecipe(String name, @NotNull List<String> ingridients){
        HashMap<String, Integer> ingredientsMap = makeRecepie(ingridients);
        Recipe recipe = new Recipe(name,ingredientsMap);
        System.out.println(recipe.getIngredientes());
        recipes.add(recipe);
    }

    // método que me permite contar la cantidad de veces que se repite un ingrediente
    public HashMap<String,Integer> makeRecepie(List<String> ingridients){
        HashMap<String,Integer> newRecipe = new HashMap<>();
        for (String ingridient : ingridients){ // ingridient por cada iteración toma el valor de cada elemento de la lista ingridients
            // getOrDefault(ingrediente, 0) obtiene el valor actual asociado con el ingrediente en el mapa o 0 si el ingrediente no existe en el mapa.
            // // Si el ingrediente ya existe en el mapa, incrementa el contador
            newRecipe.put(ingridient, newRecipe.getOrDefault(ingridient,0) + 1);
        }
        return newRecipe;
    }

    public List<Recipe> getRecipes(){
        return recipes;
    }

    // ------------------- Version anterior
    /*
    public void recipesViewShow(){
        recipes.forEach(recipe -> {
            float price = 0;
            HashMap<String, Integer> ingredients =  recipe.getIngredientes();
            System.out.println(recipe.getName());
            for (Map.Entry<String, Integer> entry : recipe.getIngredientes().entrySet()) {
                Food food = foods.get(entry.getValue());
                price += food.getPrice();
                System.out.println(entry.getKey() + " : " + entry.getValue() + " > " + food.getPrice());
            }
            System.out.println("Price > " + price);

        });
    }
    */


    //--------------------- Version mejorada
    public void recipesViewShow() {
        recipes.forEach(recipe -> {
            System.out.println(recipe.getName());

            // por cada ingrediente de la receta, devuelve su costo
            float totalPrice = recipe.getIngredientes().entrySet().stream()
                    .map(entry -> { // map lo que hace es dado un elemento (entry) lo convierte al resultado de la funcion lambda
                        Food food = findFood(entry.getKey());
                        System.out.println(entry.getKey() + " : " + entry.getValue() + " > " + food.getPrice());
                        return food.getPrice(); // el return corresponde al resultado devuelto de la función lanmda
                    })
                    .reduce(0f, Float::sum); // float::sum === (a,b) -> a+b
                    // 0f es el valor inicial, como la función lamnda retorna el precio, este lo va acumulando
                    // uso reduce para operaciones matematicas

            System.out.println("Price > " + totalPrice);
        });
    }


    //----------------------Receta con tiempo ----------------------------------

    public void addRecipeWithTime(String name, @NotNull List<String> ingridients,Integer time){
        HashMap<String, Integer> ingredientsMap = makeRecepie(ingridients);
        Recipe recipe = new Recipe(name,ingredientsMap,time);
        recipes.add(recipe);
    }


    public void showAllRecipes(){
        recipes.stream()
                .forEach(recipe -> {
                    System.out.println(recipe.getName());

                    float totalPrice = recipe.getIngredientes().entrySet().stream()
                            .map(entry -> {
                                Food food = findFood(entry.getKey());
                                System.out.println(entry.getKey() + " : " + entry.getValue() + " > " + food.getPrice());
                                return food.getPrice();
                            })
                            .reduce(0f, Float::sum);

                    System.out.println("Price > " + totalPrice);
                    System.out.println("time:" + recipe.getTime() + " mins > " + recipe.getPrice_time());
                });

    }

    public void newRecipeViewShow(Recipe r){

        System.out.println(r.getName());

        float totalPrice = r.getIngredientes().entrySet().stream()
                .map(entry -> {
                    Food food = findFood(entry.getKey());
                    System.out.println(entry.getKey() + " : " + entry.getValue() + " > " + food.getPrice());
                    return food.getPrice();
                })
                .reduce(0f, Float::sum);

        System.out.println("Price > " + totalPrice);
        System.out.println("time:" + r.getTime() + " mins > " + r.getPrice_time());


    }


    // método para buscar en la lista de verduras
    public Food findFood(String ingridient){
        return foods.stream()
                .filter(elemento -> elemento.getView().equals(ingridient))
                .findFirst()
                .orElse(null); // devuelve null si no hay coincidencia
    }


    //-----------------------------Crete----------------------------

    public void addCreate(List<String> foods){
        Create create = new Create(foods);
        creates.add(create);
        System.out.println("Se agrego el siguiente cajon de verdura: " + create.toStringSinCorchetes());
    }

    public void showCreates(){
        if (!creates.isEmpty()) {
            System.out.println("Se tiene los siguientes cajones: ");
            int n = 0;
            for (Create c : creates) {
                System.out.println(n + c.toStringSinCorchetes());
                n++;
            }
        }
        else System.out.println("No hay cajones disponibles aún");
    }


    public void deleteFoodFromCreate(String food) throws FoodIsNotInCreateException {
        boolean found = false; // necesito de la variable booleana porque si solo uso c, una vez que termine de revisar el primer cajon y no encuentre la verdura, tira la excepcion sin mirar el proximo cajon
        for(Create c: creates){
            if (c.getFoods().contains(food)){
                // los siguientes 3 pasos son porque la lista es inmutable y tengo que pisar el valor con una lista extra
                List<String> modificableFood = new ArrayList<>(c.getFoods());
                modificableFood.remove(food);
                c.setFoods(modificableFood);
                //c.removeFood(food); esto no puedo porque la lista es inmutable
                found = true;
                break;
            }
        }
        if (!found)
            throw new FoodIsNotInCreateException("El cajon no dispone de la verdura: " + food + " para crear la receta");
    }


    public Recipe findRecipe(String recipeName) throws RecipeDoesNotExistException {
        for (Recipe r : recipes){
            if (r.getName().compareTo(recipeName) == 0){
                return r;
            }
        }
        throw new RecipeDoesNotExistException("Esta receta no existe");
    }


    public void makeRecipeFromCreate(String recipeName) throws FoodIsNotInCreateException, RecipeDoesNotExistException {

        Recipe r = findRecipe(recipeName);

        HashMap<String,Integer> foods = r.getIngredientes();
        for (String ingridient : foods.keySet())
                deleteFoodFromCreate(ingridient);

        System.out.println("Se preparo la siguiente receta: ");
        newRecipeViewShow(r);
        System.out.println("\n Se gasto:");
        for (Map.Entry<String, Integer> entry: foods.entrySet()){
            System.out.println(entry.getValue() + " " + entry.getKey() );
        }
    }

}
