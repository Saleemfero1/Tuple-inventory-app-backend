package com.nextuple.Inventory.management.model.test;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import com.nextuple.Inventory.management.model.*;

public class RoleTest {

    @Test
    public void testConstructorAndGetters() {
        String id = "12345";
        String name = "Admin";
        List<UserEntity> users = new ArrayList<>();

        Role role = new Role(id, name);

        Assert.assertEquals(id, role.getId());
        Assert.assertEquals(name, role.getName());
        Assert.assertEquals(users, role.getUsers());
    }

    @Test
    public void testSetters() {
        Role role = new Role();

        String id = "12345";
        String name = "Manager";
        List<UserEntity> users = new ArrayList<>();

        role.setId(id);
        role.setName(name);
        role.setUsers(users);

        Assert.assertEquals(id, role.getId());
        Assert.assertEquals(name, role.getName());
        Assert.assertEquals(users, role.getUsers());
    }
}
