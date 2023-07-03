package com.nextuple.Inventory.management.model.test;

import com.nextuple.Inventory.management.dto.LowStockItemDTO;
import com.nextuple.Inventory.management.dto.TopTenItemsVsOtherItems;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.bson.assertions.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LowStockItemDTOTest {

    @Test
    public void testConstructorAndGetters() {
        String itemId = "12345";
        String locationId = "A1";
        String stockType = "Normal";
        int quantity = 10;

        LowStockItemDTO lowStockItemDTO = new LowStockItemDTO(itemId, locationId, stockType, quantity);

        Assert.assertEquals(itemId, lowStockItemDTO.getItemId());
        Assert.assertEquals(locationId, lowStockItemDTO.getLocationId());
        Assert.assertEquals(stockType, lowStockItemDTO.getStockType());
        Assert.assertEquals(quantity, lowStockItemDTO.getQuantity());
    }

    @Test
    public void testSetters() {
        LowStockItemDTO lowStockItemDTO = new LowStockItemDTO();

        String itemId = "12345";
        String locationId = "A1";
        String stockType = "Normal";
        int quantity = 10;

        lowStockItemDTO.setItemId(itemId);
        lowStockItemDTO.setLocationId(locationId);
        lowStockItemDTO.setStockType(stockType);
        lowStockItemDTO.setQuantity(quantity);

        Assert.assertEquals(itemId, lowStockItemDTO.getItemId());
        Assert.assertEquals(locationId, lowStockItemDTO.getLocationId());
        Assert.assertEquals(stockType, lowStockItemDTO.getStockType());
        Assert.assertEquals(quantity, lowStockItemDTO.getQuantity());
    }
    @Test
    public void testGetTotalDemandOfTopTenItems() {
        // Create sample data for the test
        long expectedTotalDemand = 100;
        long totalDemandOfOtherItems = 50;
        Map<String, Integer> topTenItemsList = new HashMap<>();
        topTenItemsList.put("Item1", 20);
        topTenItemsList.put("Item2", 30);
        TopTenItemsVsOtherItems topTenItemsVsOtherItems = new TopTenItemsVsOtherItems(expectedTotalDemand, totalDemandOfOtherItems, topTenItemsList);

        // Perform the test
        long actualTotalDemand = topTenItemsVsOtherItems.getTotalDemandOfTopTenItems();

        // Verify the result
        assertEquals(expectedTotalDemand, actualTotalDemand);
    }

    @Test
    public void testGetTotalDemandOfOtherItems() {
        // Create sample data for the test
        long totalDemandOfTopTenItems = 100;
        long expectedTotalDemand = 50;
        Map<String, Integer> topTenItemsList = new HashMap<>();
        topTenItemsList.put("Item1", 20);
        topTenItemsList.put("Item2", 30);
        TopTenItemsVsOtherItems topTenItemsVsOtherItems = new TopTenItemsVsOtherItems(totalDemandOfTopTenItems, expectedTotalDemand, topTenItemsList);

        // Perform the test
        long actualTotalDemand = topTenItemsVsOtherItems.getGetTotalDemandOfOtherItems();

        // Verify the result
        assertEquals(expectedTotalDemand, actualTotalDemand);
    }

    @Test
    public void testGetTopTenItemsList() {
        // Create sample data for the test
        long totalDemandOfTopTenItems = 100;
        long totalDemandOfOtherItems = 50;
        Map<String, Integer> expectedTopTenItemsList = new HashMap<>();
        expectedTopTenItemsList.put("Item1", 20);
        expectedTopTenItemsList.put("Item2", 30);
        TopTenItemsVsOtherItems topTenItemsVsOtherItems = new TopTenItemsVsOtherItems(totalDemandOfTopTenItems, totalDemandOfOtherItems, expectedTopTenItemsList);

        // Perform the test
        Map<String, Integer> actualTopTenItemsList = topTenItemsVsOtherItems.getTopTenItemsList();

        // Verify the result
        assertNotNull(actualTopTenItemsList);
        assertEquals(expectedTopTenItemsList.size(), actualTopTenItemsList.size());
        assertEquals(expectedTopTenItemsList, actualTopTenItemsList);
    }
}
