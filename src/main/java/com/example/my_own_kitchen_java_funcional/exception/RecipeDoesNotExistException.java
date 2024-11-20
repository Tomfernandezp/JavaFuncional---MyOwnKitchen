package com.example.my_own_kitchen_java_funcional.exception;

public class RecipeDoesNotExistException extends Exception{

    public RecipeDoesNotExistException(String message) {
        super(message);
    }
}
