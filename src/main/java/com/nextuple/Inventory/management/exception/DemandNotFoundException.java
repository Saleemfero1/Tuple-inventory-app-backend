package com.nextuple.Inventory.management.exception;

public class DemandNotFoundException extends RuntimeException{
    public DemandNotFoundException(String message){
        super(message);
    }
}
