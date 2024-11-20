package com.example.my_own_kitchen_java_funcional.commands;


import com.example.my_own_kitchen_java_funcional.exception.CommandNotRecognizedException;
import com.example.my_own_kitchen_java_funcional.exception.FoodIsNotInCreateException;
import com.example.my_own_kitchen_java_funcional.exception.RecipeDoesNotExistException;
import com.example.my_own_kitchen_java_funcional.model.GreenGrocery;
import com.example.my_own_kitchen_java_funcional.model.Recipe;
import picocli.CommandLine;

import java.util.List;
import java.util.Optional;

@CommandLine.Command(name = "recipes", mixinStandardHelpOptions = true, subcommands = {CommandLine.HelpCommand.class},
        description = "Show recipes")
public class RecipeCommands implements Runnable {

    @CommandLine.Parameters(index = "0..*")
    private List<String> command;

    public void run(){
        String action = command.get(0);

        switch (action)
        {
            case "list":
                list();
                break;
            case "regist":
                regist(command);
                break;
            case "show":
                Optional<String> recipe;
                if (command.size() > 1)
                    recipe = Optional.of(command.get(1));
                else recipe = Optional.empty();

                show(recipe);
                break;
            case "showAll":

                showAll();
                break;
            case "make":
                make(command.get(1));
                break;
            default:
                try {
                    throw new CommandNotRecognizedException("Comando incorrecto, prueba de nuevo");
                } catch (CommandNotRecognizedException e) {
                    System.out.println(e.getMessage());
                }
        }
    }


    public void list(){
        System.out.println("\n Mostrar lista recetas \n");
        GreenGrocery greengrocery = GreenGrocery.getInstance();
        List<Recipe> recipes = greengrocery.getRecipes();
        recipes.stream().forEach(Recipe::listRecipe);
    }

    public void regist(List<String> saladInfo){
        String name = saladInfo.get(1);
        Integer time = Integer.parseInt(saladInfo.get(2));
        List<String> ingredients = saladInfo.subList(3,saladInfo.size()); // desde la posicion 3 hasta el final
        System.out.println("\n Registrar ensalada: " + name + "\n" +
                "Se necesitan: " + time + " minutos para finalizarla \n" +
                "Ingredientes: \t");
        ingredients.forEach(System.out::println);
        GreenGrocery.getInstance().addRecipeWithTime(name,ingredients,time);
    }

    public void showAll(){
        GreenGrocery greenGrocery = GreenGrocery.getInstance();
        List<Recipe> recipes = greenGrocery.getRecipes();
        if (!recipes.isEmpty())
            greenGrocery.showAllRecipes();
        else
            System.out.println("No hay recetas cargadas todavía");
    }

    public void show(Optional<String> recipe){

        GreenGrocery greengrocery = GreenGrocery.getInstance();
        List<Recipe> recipes = greengrocery.getRecipes();
        recipe.ifPresentOrElse(recipeName ->
                // si Optional no es vacio, ejecuto esta función
                        recipes.stream()
                                .filter(recipe1 -> recipe1.getName().equals(recipeName))
                                .forEach(recipe1 -> {
                                    greengrocery.newRecipeViewShow(recipe1);
                                }),
                // si Optional es vacio, ejecuto esta función
                ()-> recipes.stream().forEach(recipe2 -> {
                    greengrocery.newRecipeViewShow(recipe2);
                })
        );
    }

    public void make(String saladInfo){
        try {
            GreenGrocery greengrocery = GreenGrocery.getInstance();
            greengrocery.makeRecipeFromCreate(saladInfo);
        } catch (FoodIsNotInCreateException | RecipeDoesNotExistException e) {
            System.out.println(e.getMessage());
        }


    }
}
