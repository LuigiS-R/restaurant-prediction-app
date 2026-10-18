package com.example.TechWeek25.util.exceptions;

public class NeighborhoodNotFoundException extends Exception{

    public NeighborhoodNotFoundException(){
        super();
    }

    public NeighborhoodNotFoundException(String message){
        super(message);
    }
}
