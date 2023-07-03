package com.nextuple.Inventory.management.model.test;

import com.nextuple.Inventory.management.model.Organization;
import com.nextuple.Inventory.management.model.Role;
import com.nextuple.Inventory.management.model.UserEntity;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class UserEntityTest {

    @Test
    public void testConstructorAndGetters() {
        String id = "12345";
        String username = "johnDoe";
        String userEmail = "john.doe@example.com";
        String password = "password123";
        String organizationId = "ORG001";

        UserEntity userEntity = new UserEntity(id, username, userEmail, password, organizationId);

        Assert.assertEquals(id, userEntity.getId());
        Assert.assertEquals(username, userEntity.getUsername());
        Assert.assertEquals(userEmail, userEntity.getUserEmail());
        Assert.assertEquals(password, userEntity.getPassword());
        Assert.assertEquals(organizationId, userEntity.getOrganizationId());
        Assert.assertNotNull(userEntity.getRoles());
        Assert.assertEquals(0, userEntity.getRoles().size());
    }

    @Test
    public void testSetters() {
        UserEntity userEntity = new UserEntity();

        String id = "12345";
        String username = "johnDoe";
        String userEmail = "john.doe@example.com";
        String password = "password123";
        String organizationId = "ORG001";
        List<Role> roles = new ArrayList<>();

        userEntity.setId(id);
        userEntity.setUsername(username);
        userEntity.setUserEmail(userEmail);
        userEntity.setPassword(password);
        userEntity.setOrganizationId(organizationId);
        userEntity.setRoles(roles);

        Assert.assertEquals(id, userEntity.getId());
        Assert.assertEquals(username, userEntity.getUsername());
        Assert.assertEquals(userEmail, userEntity.getUserEmail());
        Assert.assertEquals(password, userEntity.getPassword());
        Assert.assertEquals(organizationId, userEntity.getOrganizationId());
        Assert.assertEquals(roles, userEntity.getRoles());
    }


}
