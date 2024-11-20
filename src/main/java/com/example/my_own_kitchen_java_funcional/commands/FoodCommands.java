package com.example.my_own_kitchen_java_funcional.commands;


import com.example.my_own_kitchen_java_funcional.exception.CommandNotRecognizedException;
import com.example.my_own_kitchen_java_funcional.exception.FoodNotFoundException;
import com.example.my_own_kitchen_java_funcional.model.Food;
import com.example.my_own_kitchen_java_funcional.model.GreenGrocery;
import picocli.CommandLine;

import java.util.List;

/*
@CommandLine.Command: Esta anotación indica que la clase define un comando CLI.
name = "recipes": El nombre del comando es recipes.
mixinStandardHelpOptions = true: Permite agregar opciones estándar como --help y --version.
subcommands = {CommandLine.HelpCommand.class}: Agrega un subcomando de ayuda, accesible con recipes help.
description = "Show recipes": Describe el comando cuando alguien ejecuta recipes --help.
 */
@CommandLine.Command(name = "foods", mixinStandardHelpOptions = true, subcommands = {CommandLine.HelpCommand.class},
    description = "Show foods")
public class FoodCommands implements Runnable{

    /*
@CommandLine.Parameters: Indica que los argumentos pasados después del comando principal (recipes) se almacenan en esta lista.
index = "0..*": Acepta cualquier cantidad de parámetros, empezando desde el índice 0.
command: Contendrá la lista de argumentos pasados al comando recipes.
Por ejemplo:
Si el usuario ejecuta recipes list, entonces command = ["list"].
Si ejecuta recipes show salsa, entonces command = ["show", "salsa"].
     */
    @CommandLine.Parameters(index = "0..*")
    private List<String> command;


    @Override
    public void run() {
        String action = command.get(0);
        switch (action)
        {
            case "list":
                list();
                break;
            case "regist":
                regist(command);
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
        List<Food> listOfFood = GreenGrocery.getInstance().getFoods();
        if(!listOfFood.isEmpty()){
            System.out.println("Lista de alimentos en la verduleria: ");
            for (Food food : listOfFood) {
                System.out.print(food.list());
            }
        } else System.out.println("Todavia no hay comida en la verduleria");
    }

    public void regist(List<String> foodInfo){
        String food = foodInfo.get(1);
        Integer price = Integer.parseInt(foodInfo.get(2));
        try {
            GreenGrocery.getInstance().addFood(food,price);
        } catch (FoodNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
