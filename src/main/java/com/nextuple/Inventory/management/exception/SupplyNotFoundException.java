package com.nextuple.Inventory.management.exception;

public class SupplyNotFoundException extends  RuntimeException{
    public SupplyNotFoundException(String message){
        super(message);
    }
}
