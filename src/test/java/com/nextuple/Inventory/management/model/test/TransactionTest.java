package com.nextuple.Inventory.management.model.test;

import com.nextuple.Inventory.management.model.Transaction;
import org.junit.Assert;
import org.junit.Test;

public class TransactionTest {

    @Test
    public void testConstructorAndGetters() {
        String id = "12345";
        String itemId = "56789";
        String locationId = "A1";
        String type = "Inbound";
        int quantity = 10;
        String date = "2023-06-08";
        String organizationId = "98765";

        Transaction transaction = new Transaction( itemId, locationId, type, quantity, date, organizationId);


        Assert.assertEquals(itemId, transaction.getItemId());
        Assert.assertEquals(locationId, transaction.getLocationId());
        Assert.assertEquals(type, transaction.getType());
        Assert.assertEquals(quantity, transaction.getQuantity());
        Assert.assertEquals(date, transaction.getDate());
        Assert.assertEquals(organizationId, transaction.getOrganizationId());
    }

    @Test
    public void testSetters() {
        Transaction transaction = new Transaction();

        String id = "12345";
        String itemId = "56789";
        String locationId = "A1";
        String type = "Inbound";
        int quantity = 10;
        String date = "2023-06-08";
        String organizationId = "98765";

        transaction.setId(id);
        transaction.setItemId(itemId);
        transaction.setLocationId(locationId);
        transaction.setType(type);
        transaction.setQuantity(quantity);
        transaction.setDate(date);
        transaction.setOrganizationId(organizationId);

        Assert.assertEquals(id, transaction.getId());
        Assert.assertEquals(itemId, transaction.getItemId());
        Assert.assertEquals(locationId, transaction.getLocationId());
        Assert.assertEquals(type, transaction.getType());
        Assert.assertEquals(quantity, transaction.getQuantity());
        Assert.assertEquals(date, transaction.getDate());
        Assert.assertEquals(organizationId, transaction.getOrganizationId());
    }
}
