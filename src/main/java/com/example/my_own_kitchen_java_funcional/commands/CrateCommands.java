package com.example.my_own_kitchen_java_funcional.commands;


import com.example.my_own_kitchen_java_funcional.exception.CommandNotRecognizedException;
import com.example.my_own_kitchen_java_funcional.exception.FoodIsNotInCreateException;
import com.example.my_own_kitchen_java_funcional.exception.RecipeDoesNotExistException;
import com.example.my_own_kitchen_java_funcional.model.GreenGrocery;
import picocli.CommandLine;

import java.util.List;

@CommandLine.Command(name = "crate", mixinStandardHelpOptions = true, subcommands = {CommandLine.HelpCommand.class},
        description = "Show crate commands")
public class CrateCommands implements Runnable{

    @CommandLine.Parameters(index = "0..*")
    private List<String> command;

    public void run() {
        String action = command.get(0);

        switch (action) {
            case "list":
                list();
                break;
            case "regist":
                regist(command);
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
        GreenGrocery.getInstance().showCreates();
    }


    public void regist(List<String> foods) {
        List<String> crate = foods.subList(1,foods.size());
        GreenGrocery.getInstance().addCreate(crate);
    }

    public void make(String recipeName){
        GreenGrocery greengrocery = GreenGrocery.getInstance();
        try {
            greengrocery.makeRecipeFromCreate(recipeName);

        } catch (FoodIsNotInCreateException | RecipeDoesNotExistException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("\n Actualmente el cajon contiene las siguientes verduras:");
        greengrocery.showCreates();
    }


}
