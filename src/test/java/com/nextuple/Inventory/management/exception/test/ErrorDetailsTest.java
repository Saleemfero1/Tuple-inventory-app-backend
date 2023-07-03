package com.nextuple.Inventory.management.exception.test;

import com.nextuple.Inventory.management.exception.ErrorDetails;
import org.junit.Assert;
import org.junit.Test;
import java.time.LocalDateTime;

public class ErrorDetailsTest {

    @Test
    public void testErrorDetailsConstructorAndGetters() {
        // Create a LocalDateTime object for the timestamp
        LocalDateTime timestamp = LocalDateTime.of(2023, 6, 8, 12, 0, 0);

        // Create an error message
        String message = "Test error message";

        // Create an instance of ErrorDetails using the constructor
        ErrorDetails errorDetails = new ErrorDetails(timestamp, message);

        // Verify that the timestamp and message are set correctly
        Assert.assertEquals(timestamp, errorDetails.getTimestamp());
        Assert.assertEquals(message, errorDetails.getMessage());
    }
}
