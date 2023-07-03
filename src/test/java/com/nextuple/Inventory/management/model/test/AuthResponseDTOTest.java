package com.nextuple.Inventory.management.model.test;

import com.nextuple.Inventory.management.dto.AuthResponseDTO;
import org.junit.Assert;
import org.junit.Test;

public class AuthResponseDTOTest {

    @Test
    public void testConstructor() {
        String accessToken = "exampleAccessToken";
        AuthResponseDTO authResponseDTO = new AuthResponseDTO(accessToken);

        Assert.assertEquals(accessToken, authResponseDTO.getAccessToken());
        Assert.assertEquals("Bearer ", authResponseDTO.getTokenType());
    }

    @Test
    public void testSetAccessToken() {
        AuthResponseDTO authResponseDTO = new AuthResponseDTO("oldAccessToken");

        String newAccessToken = "newAccessToken";
        authResponseDTO.setAccessToken(newAccessToken);

        Assert.assertEquals(newAccessToken, authResponseDTO.getAccessToken());
    }

    @Test
    public void testSetTokenType() {
        AuthResponseDTO authResponseDTO = new AuthResponseDTO("accessToken");

        String newTokenType = "NewToken";
        authResponseDTO.setTokenType(newTokenType);

        Assert.assertEquals(newTokenType, authResponseDTO.getTokenType());
    }
}
