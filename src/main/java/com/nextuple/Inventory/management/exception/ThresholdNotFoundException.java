package com.nextuple.Inventory.management.exception;

public class ThresholdNotFoundException extends RuntimeException{
    public ThresholdNotFoundException(String message){
        super(message);
    }
}
