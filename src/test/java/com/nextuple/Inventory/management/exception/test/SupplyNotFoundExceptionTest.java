package com.nextuple.Inventory.management.exception.test;

import com.nextuple.Inventory.management.exception.SupplyNotFoundException;
import com.nextuple.Inventory.management.exception.UserNotFoundException;
import org.junit.Assert;
import org.junit.Test;

public class SupplyNotFoundExceptionTest {

    @Test
    public void testSupplyNotFoundException() {
        String errorMessage = "Supply not found";
        SupplyNotFoundException exception = new SupplyNotFoundException(errorMessage);

        Assert.assertEquals(errorMessage, exception.getMessage());
    }


    @Test
    public void testUserNotFoundException() {
        String errorMessage = "User not found";
        UserNotFoundException exception = new UserNotFoundException(errorMessage);

        Assert.assertEquals(errorMessage, exception.getMessage());
    }
}