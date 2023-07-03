package com.nextuple.Inventory.management.controller.test;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.nextuple.Inventory.management.controller.InventoryController;
import com.nextuple.Inventory.management.dto.LowStockItemDTO;
import com.nextuple.Inventory.management.dto.TopTenItemsVsOtherItems;
import com.nextuple.Inventory.management.model.Transaction;
import com.nextuple.Inventory.management.service.InventoryServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class InventoryControllerTest{

    @Mock
    private InventoryServices inventoryServices;
    @InjectMocks
    private InventoryController inventoryController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAvailableQtyOfTheItemAtTheGivenLocation() throws JsonProcessingException {
        String organizationId = "ORG001";
        String itemId = "ITEM001";
        String locationId = "LOC001";
        Map<String, Object> expectedResult = new HashMap<>();
        // Set up the mock service method
        when(inventoryServices.AvailableQtyOfTheItemAtTheGivenLocation(organizationId, itemId, locationId))
                .thenReturn(expectedResult);

        // Call the controller method
        ResponseEntity<Map<String, Object>> response = inventoryController.AvailableQtyOfTheItemAtTheGivenLocation(
                organizationId, itemId, locationId);

        // Verify the result
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResult, response.getBody());
        verify(inventoryServices, times(1)).AvailableQtyOfTheItemAtTheGivenLocation(organizationId, itemId, locationId);
    }

    @Test
    void testAvailableQtyOfTheItemAtAllTheLocation() throws JsonProcessingException {
        String organizationId = "ORG001";
        String itemId = "ITEM001";
        Map<String, Object> expectedResult = new HashMap<>();
        // Set up the mock service method
        when(inventoryServices.AvailableQtyOfTheItemAtAllTheLocation(organizationId, itemId))
                .thenReturn(expectedResult);

        // Call the controller method
        ResponseEntity<Map<String, Object>> response = inventoryController.AvailableQtyOfTheItemAtAllTheLocation(
                organizationId, itemId);

        // Verify the result
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResult, response.getBody());
        verify(inventoryServices, times(1)).AvailableQtyOfTheItemAtAllTheLocation(organizationId, itemId);
    }

    @Test
    void testGetDetailsOfAllItemWithAvailability() throws JsonProcessingException {
        String organizationId = "ORG001";
        List<Map<String, Object>> expectedResult = new ArrayList<>();
        // Set up the mock service method
        when(inventoryServices.getDetailsOfAllItemWithAvailability(organizationId)).thenReturn(expectedResult);

        // Call the controller method
        ResponseEntity<List<Map<String, Object>>> response = inventoryController.getDetailsOfAllItemWithAvailability(
                organizationId);

        // Verify the result
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResult, response.getBody());
        verify(inventoryServices, times(1)).getDetailsOfAllItemWithAvailability(organizationId);
    }

    @Test
    void testGetTotalNumbersOfDashboard() throws JsonProcessingException {
        String organizationId = "ORG001";
        Map<String, Integer> expectedResult = new HashMap<>();
        // Set up the mock service method
        when(inventoryServices.getTotalNumbersOfDashboard(organizationId)).thenReturn(expectedResult);

        // Call the controller method
        ResponseEntity<Map<String, Integer>> response = inventoryController.getTotalNumbersOfDashboard(organizationId);

        // Verify the result
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResult, response.getBody());
        verify(inventoryServices, times(1)).getTotalNumbersOfDashboard(organizationId);
    }

    @Test
    void testGetTransaction() {
        String organizationId = "ORG001";
        List<Transaction> expectedResult = new ArrayList<>();
        // Set up the mock service method
        when(inventoryServices.getTransactions(organizationId)).thenReturn(expectedResult);

        // Call the controller method
        ResponseEntity<List<Transaction>> response = inventoryController.getTransaction(organizationId);

        // Verify the result
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResult, response.getBody());
        verify(inventoryServices, times(1)).getTransactions(organizationId);
    }

    @Test
    void testTopTenItemsVsOtherItems() {
        String organizationId = "ORG001";
        TopTenItemsVsOtherItems expectedResult = new TopTenItemsVsOtherItems();
        // Set up the mock service method
        when(inventoryServices.topTenItemsVsotherItems(organizationId)).thenReturn(expectedResult);

        // Call the controller method
        ResponseEntity<TopTenItemsVsOtherItems> response = inventoryController.topTenItemsVsOtherItems(organizationId);

        // Verify the result
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResult, response.getBody());
        verify(inventoryServices, times(1)).topTenItemsVsotherItems(organizationId);
    }

    @Test
    void testGetLowStockItems() {
        String organizationId = "ORG001";
        List<LowStockItemDTO> expectedResult = new ArrayList<>();
        // Set up the mock service method
        when(inventoryServices.getLowStockItems(organizationId)).thenReturn(expectedResult);

        // Call the controller method
        ResponseEntity<List<LowStockItemDTO>> response = inventoryController.getLowStockItems(organizationId);

        // Verify the result
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResult, response.getBody());
        verify(inventoryServices, times(1)).getLowStockItems(organizationId);
    }
}