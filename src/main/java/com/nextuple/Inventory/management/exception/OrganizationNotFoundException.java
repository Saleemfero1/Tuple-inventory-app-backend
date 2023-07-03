package com.nextuple.Inventory.management.exception;

import org.springframework.http.HttpStatus;

public class OrganizationNotFoundException extends RuntimeException {

    public OrganizationNotFoundException(String message) {
        super(message);
    }


}
