package com.nextuple.Inventory.management.model.test;

import com.nextuple.Inventory.management.model.Organization;
import com.nextuple.Inventory.management.model.Threshold;
import org.junit.Assert;
import org.junit.Test;

public class ThresholdTest {

    @Test
    public void testConstructorAndGetters() {
        String id = "12345";
        String organizationId = "ORG001";
        String itemId = "123";
        String locationId = "4566";
        int minThreshold = 10;
        int maxThreshold = 100;

        Threshold threshold = new Threshold(organizationId, itemId, locationId, minThreshold, maxThreshold);
        threshold.setId(id);

        Assert.assertEquals(id, threshold.getId());
        Assert.assertEquals(organizationId, threshold.getOrganizationId());
        Assert.assertEquals(itemId, threshold.getItemId());
        Assert.assertEquals(locationId, threshold.getLocationId());
        Assert.assertEquals(minThreshold, threshold.getMinThreshold());
        Assert.assertEquals(maxThreshold, threshold.getMaxThreshold());
    }

    @Test
    public void testSetters() {
        Threshold threshold = new Threshold();

        String id = "12345";
        String organizationId = "ORG001";
        String itemId = "123";
        String locationId = "4566";
        int minThreshold = 10;
        int maxThreshold = 100;

        threshold.setId(id);
        threshold.setOrganizationId(organizationId);
        threshold.setItemId(itemId);
        threshold.setLocationId(locationId);
        threshold.setMinThreshold(minThreshold);
        threshold.setMaxThreshold(maxThreshold);

        Assert.assertEquals(id, threshold.getId());
        Assert.assertEquals(organizationId, threshold.getOrganizationId());
        Assert.assertEquals(itemId, threshold.getItemId());
        Assert.assertEquals(locationId, threshold.getLocationId());
        Assert.assertEquals(minThreshold, threshold.getMinThreshold());
        Assert.assertEquals(maxThreshold, threshold.getMaxThreshold());
    }

    @Test
    public void testOrganizationDBRef() {
        Organization organization = new Organization();
        Threshold threshold = new Threshold();
        threshold.setOrganization(organization);

        Assert.assertEquals(organization, threshold.getOrganization());
    }
}
