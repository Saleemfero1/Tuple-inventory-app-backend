package com.nextuple.Inventory.management.model.test;

import com.nextuple.Inventory.management.model.Demand;
import com.nextuple.Inventory.management.model.Item;
import com.nextuple.Inventory.management.model.Location;
import com.nextuple.Inventory.management.model.Organization;
import org.junit.Assert;
import org.junit.Test;

public class DemandTest {

    @Test
    public void testConstructorAndGetters() {
        String id = "12345";
        String organizationId = "ORG001";
        String demandType = "HARDPROMISED";
        int quantity = 30;
        String itemId = "6383728";
        String locationId = "01504";

        Demand demand = new Demand(organizationId, demandType, quantity, itemId, locationId);
        demand.setId(id);

        Assert.assertEquals(id, demand.getId());
        Assert.assertEquals(organizationId, demand.getOrganizationId());
        Assert.assertEquals(demandType, demand.getDemandType());
        Assert.assertEquals(quantity, demand.getQuantity());
        Assert.assertEquals(itemId, demand.getItemId());
        Assert.assertEquals(locationId, demand.getLocationId());
    }

    @Test
    public void testSetters() {
        Demand demand = new Demand();

        String id = "12345";
        String organizationId = "ORG001";
        String demandType = "PLANNED";
        int quantity = 50;
        String itemId = "987654";
        String locationId = "12345";

        demand.setId(id);
        demand.setOrganizationId(organizationId);
        demand.setDemandType(demandType);
        demand.setQuantity(quantity);
        demand.setItemId(itemId);
        demand.setLocationId(locationId);

        Assert.assertEquals(id, demand.getId());
        Assert.assertEquals(organizationId, demand.getOrganizationId());
        Assert.assertEquals(demandType, demand.getDemandType());
        Assert.assertEquals(quantity, demand.getQuantity());
        Assert.assertEquals(itemId, demand.getItemId());
        Assert.assertEquals(locationId, demand.getLocationId());
    }

    @Test
    public void testExistDemandTypes() {
        Assert.assertEquals("HARDPROMISED", Demand.existDemandTypes.HARDPROMISED.name());
        Assert.assertEquals("PLANNED", Demand.existDemandTypes.PLANNED.name());
    }
}
