package com.example.my_own_kitchen_java_funcional.exception;

public class CommandNotRecognizedException extends Exception{

    public CommandNotRecognizedException(String message) {
        super(message);
    }
}
