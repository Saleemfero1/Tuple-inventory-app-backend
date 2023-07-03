package com.nextuple.Inventory.management.model.test;

import com.nextuple.Inventory.management.dto.LogInDTO;
import org.junit.Assert;
import org.junit.Test;

public class LogInDTOTest {

    @Test
    public void testGetterAndSetter() {
        String username = "testUser";
        String password = "testPassword";

        LogInDTO logInDTO = new LogInDTO();
        logInDTO.setUsername(username);
        logInDTO.setPassword(password);

        Assert.assertEquals(username, logInDTO.getUsername());
        Assert.assertEquals(password, logInDTO.getPassword());
    }
}
