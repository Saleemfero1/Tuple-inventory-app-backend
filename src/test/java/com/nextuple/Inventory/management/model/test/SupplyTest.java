package com.nextuple.Inventory.management.model.test;

import com.nextuple.Inventory.management.model.Item;
import com.nextuple.Inventory.management.model.Location;
import com.nextuple.Inventory.management.model.Organization;
import com.nextuple.Inventory.management.model.Supply;
import org.junit.Assert;
import org.junit.Test;

public class SupplyTest {

    @Test
    public void testConstructorAndGetters() {
        String id = "12345";
        String organizationId = "ORG001";
        String itemId = "001";
        String locationId = "110";
        String supplyType = "ONHAND";
        int quantity = 7;

        Supply supply = new Supply(organizationId, itemId, locationId, supplyType, quantity);
        supply.setId(id);

        Assert.assertEquals(id, supply.getId());
        Assert.assertEquals(organizationId, supply.getOrganizationId());
        Assert.assertEquals(itemId, supply.getItemId());
        Assert.assertEquals(locationId, supply.getLocationId());
        Assert.assertEquals(supplyType, supply.getSupplyType());
        Assert.assertEquals(quantity, supply.getQuantity());
    }

    @Test
    public void testSetters() {
        Supply supply = new Supply();

        String id = "12345";
        String organizationId = "ORG001";
        String itemId = "001";
        String locationId = "110";
        String supplyType = "DAMAGED";
        int quantity = 5;

        supply.setId(id);
        supply.setOrganizationId(organizationId);
        supply.setItemId(itemId);
        supply.setLocationId(locationId);
        supply.setSupplyType(supplyType);
        supply.setQuantity(quantity);

        Assert.assertEquals(id, supply.getId());
        Assert.assertEquals(organizationId, supply.getOrganizationId());
        Assert.assertEquals(itemId, supply.getItemId());
        Assert.assertEquals(locationId, supply.getLocationId());
        Assert.assertEquals(supplyType, supply.getSupplyType());
        Assert.assertEquals(quantity, supply.getQuantity());
    }

    @Test
    public void testExistSupplyTypes() {
        Assert.assertEquals("ONHAND", Supply.existSupplyTypes.ONHAND.name());
        Assert.assertEquals("INTRANSIT", Supply.existSupplyTypes.INTRANSIT.name());
        Assert.assertEquals("DAMAGED", Supply.existSupplyTypes.DAMAGED.name());
    }
}
