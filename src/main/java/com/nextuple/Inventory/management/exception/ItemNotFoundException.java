package com.nextuple.Inventory.management.exception;

public class ItemNotFoundException extends RuntimeException{
    public  ItemNotFoundException(String message){
        super(message);
    }
}
