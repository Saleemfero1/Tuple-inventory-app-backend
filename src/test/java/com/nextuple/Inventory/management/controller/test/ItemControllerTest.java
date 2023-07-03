package com.nextuple.Inventory.management.controller.test;

import com.nextuple.Inventory.management.controller.ItemController;
import com.nextuple.Inventory.management.model.Item;
        import com.nextuple.Inventory.management.service.ItemService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
        import org.junit.jupiter.api.Test;
        import org.mockito.InjectMocks;
        import org.mockito.Mock;
        import org.mockito.MockitoAnnotations;
        import org.springframework.data.domain.Page;
        import org.springframework.http.HttpStatus;
        import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.URI;
        import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

        import static org.junit.jupiter.api.Assertions.assertEquals;
        import static org.mockito.ArgumentMatchers.*;
        import static org.mockito.Mockito.*;

public class ItemControllerTest {

    @Mock
    private ItemService itemService;

    @InjectMocks
    private ItemController itemController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        HttpServletRequest mockRequest = new MockHttpServletRequest();
        ServletRequestAttributes servletRequestAttributes = new ServletRequestAttributes(mockRequest);
        RequestContextHolder.setRequestAttributes(servletRequestAttributes);
    }


    @After
    public void teardown() {
        RequestContextHolder.resetRequestAttributes();
    }
    @Test
    public void testItemDetails() {
        // Mock data
        String organizationId = "ORG001";
        List<Item> items = new ArrayList<>();
        items.add(new Item("00001", "itemNameOne", "itemOneDesc", "ItemOneCategory", "itemOneType", true, 2000, true, true, true,"ORG001"));
        items.add(new Item("00002", "itemNameOne", "itemOneDesc", "ItemOneCategory", "itemOneType", true, 2000, true, true, true,"ORG001"));

        // Mock the service method
        when(itemService.itemDetails(organizationId)).thenReturn(items);

        // Call the controller method
        ResponseEntity<List<Item>> response = itemController.itemDetails(organizationId);

        // Verify the result
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(items, response.getBody());
        verify(itemService, times(1)).itemDetails(organizationId);
    }

    @Test
    public void testPageableItemDetails() {
        // Mock data
        String organizationId = "ORG001";
        int pageNumber = 0;
        int pageSize = 5;
        Page<Item> itemPage = mock(Page.class);

        // Mock the service method
        when(itemService.pageableItemDetails(organizationId, pageNumber, pageSize)).thenReturn(itemPage);

        // Call the controller method
        ResponseEntity<Page<Item>> response = itemController.pageableItemDetails(organizationId, pageNumber, pageSize);

        // Verify the result
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(itemPage, response.getBody());
        verify(itemService, times(1)).pageableItemDetails(organizationId, pageNumber, pageSize);
    }

    @Test
    public void testFindItem() {
        // Mock data
        String organizationId = "ORG001";
        String itemId = "00001";
        Item item = new Item("00001", "itemNameOne", "itemOneDesc", "ItemOneCategory", "itemOneType", true, 2000, true, true, true,"ORG001");


        // Mock the service method
        when(itemService.findItem(itemId, organizationId)).thenReturn(item);

        // Call the controller method
        ResponseEntity<Item> response = itemController.findItem(organizationId, itemId);

        // Verify the result
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(item, response.getBody());
        verify(itemService, times(1)).findItem(itemId, organizationId);
    }

    @Test
    public void testCreateItem() {
        // Mock data
        String organizationId = "ORG001";
        Item item = new Item("00001", "itemNameOne", "itemOneDesc", "ItemOneCategory", "itemOneType", true, 2000, true, true, true,"ORG001");

        Item itemCreated= new Item("00001", "itemNameOne", "itemOneDesc", "ItemOneCategory", "itemOneType", true, 2000, true, true, true,"ORG001");


        // Mock the service method
        when(itemService.createItem(item, organizationId)).thenReturn(itemCreated);

        // Call the controller method
        ResponseEntity<Item> response = itemController.createItem(organizationId, item);

        // Verify the result
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(itemCreated, response.getBody());
        verify(itemService, times(1)).createItem(item, organizationId);
    }


    @Test
    public void testDeleteItem() {
        // Mock data
        String organizationId = "ORG001";
        String itemId = "1";
        String message = "Item deleted successfully";

        // Mock the service method
        when(itemService.deleteItemIfNotPresentInReferenceCollection(itemId, organizationId)).thenReturn(message);

        // Call the controller method
        ResponseEntity<String> response = itemController.deleteItem(itemId, organizationId);

        // Verify the result
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals(message, response.getBody());
        verify(itemService, times(1)).deleteItemIfNotPresentInReferenceCollection(itemId, organizationId);
    }

    @Test
    public void testUpdateItem() {
        // Mock data
        String organizationId = "ORG001";
        String itemId = "1";
        Item item= new Item("00001", "itemNameOne", "itemOneDesc", "ItemOneCategory", "itemOneType", true, 2000, true, true, true,"ORG001");
        Item updatedItem = new Item("00001", "itemNameOne", "itemOneDesc", "ItemOneCategory", "itemOneType", true, 2000, true, true, true,"ORG001");


        // Mock the service method
        when(itemService.updateItem(organizationId, item, itemId)).thenReturn(updatedItem);

        // Call the controller method
        ResponseEntity<Item> response = itemController.updateItem(item, itemId, organizationId);

        // Verify the result
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(updatedItem, response.getBody());
        verify(itemService, times(1)).updateItem(organizationId, item, itemId);
    }

    @Test
    public void testGetActiveItems() {
        // Mock data
        String organizationId = "ORG001";
        List<Item> activeItems = Arrays.asList(new Item("00001", "itemNameOne", "itemOneDesc", "ItemOneCategory", "itemOneType", true, 2000, true, true, true,"ORG001"));


        // Mock the service method
        when(itemService.getActiveItems(organizationId)).thenReturn(activeItems);

        // Call the controller method
        ResponseEntity<List<Item>> response = itemController.getActiveItem(organizationId);

        // Verify the result
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(activeItems, response.getBody());
        verify(itemService, times(1)).getActiveItems(organizationId);
    }


}
