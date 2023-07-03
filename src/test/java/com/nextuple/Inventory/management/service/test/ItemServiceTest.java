package com.nextuple.Inventory.management.service.test;
import com.nextuple.Inventory.management.exception.ItemNotFoundException;
import com.nextuple.Inventory.management.exception.OrganizationNotFoundException;
import com.nextuple.Inventory.management.exception.SupplyAndDemandExistException;
import com.nextuple.Inventory.management.model.Demand;
import com.nextuple.Inventory.management.model.Item;
import com.nextuple.Inventory.management.model.Organization;
import com.nextuple.Inventory.management.model.Supply;
import com.nextuple.Inventory.management.repository.*;
import com.nextuple.Inventory.management.service.ItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ItemServiceTest {
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private LocationRepository locationRepository;
    @Mock
    private SupplyRepository supplyRepository;
    @Mock
    private DemandRepository demandRepository;
    @Mock
    private ThresholdRepository thresholdRepository;
    @Mock
    private OrganizationRepository organizationRepository;

    @InjectMocks
    private ItemService itemService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }
/*
            new Item("00001", "itemNameOne", "itemOneDesc", "ItemOneCategory", "itemOneType", true, 2000, true, true, true,"ORG001");
            new Supply("ORG001","ORG001_00001","111","ONHAND",7);
            new Demand("ORG001","ONHAND",111,"ORG001_00001","111");
            new Location("111", "locationDesc", "locationType", true,true,true,"addressLine1",
             "addressLine2","addressLine3","city","state","country","pinCode","ORG001");
             Organization organization = new Organization("ORG001", "TUPLE","Tuple@gmail.com", "tuple@123");*/
    @Test
    public void testItemDetails_WithExistingItems_ShouldReturnItemList() {
        // Arrange
        List<Item> itemList = new ArrayList<>();
        itemList.add(new Item("00001", "itemNameOne", "itemOneDesc", "ItemOneCategory", "itemOneType", true, 2000, true, true, true,"ORG001"));
        itemList.add(new Item("00002", "itemNameTwo", "itemTwoDesc", "ItemTwoCategory", "itemTwoType", true, 2000, true, true, true,"ORG001"));
        when(itemRepository.findByOrganizationId(anyString())).thenReturn(itemList);

        // Act
        List<Item> result = itemService.itemDetails("ORG001");

        // Assert
        assertEquals(2, result.size());
        assertEquals("00001", result.get(0).getItemId());
        assertEquals("00002", result.get(1).getItemId());
        verify(itemRepository, times(1)).findByOrganizationId(anyString());
    }

    @Test
    public void testItemDetails_WithNoItems_ShouldThrowItemNotFoundException() {
        // Arrange
        when(itemRepository.findByOrganizationId(anyString())).thenReturn(new ArrayList<>());

        // Act & Assert
        assertThrows(ItemNotFoundException.class, () -> itemService.itemDetails("ORG001"));
        verify(itemRepository, times(1)).findByOrganizationId(anyString());
    }

    @Test
    public void testFindItem_WithExistingItem_ShouldReturnItem() {
        // Arrange
        Item item = new Item("00001", "itemNameOne", "itemOneDesc", "ItemOneCategory", "itemOneType", true, 2000, true, true, true,"ORG001");
        when(itemRepository.findByItemIdAndOrganizationId(anyString(), anyString())).thenReturn(Optional.of(item));

        // Act
        Item result = itemService.findItem("ORG001_00001", "ORG001");

        // Assert
        assertNotNull(result);
        assertEquals("00001", result.getItemId());
        verify(itemRepository, times(1)).findByItemIdAndOrganizationId(anyString(), anyString());
    }

    @Test
    public void testFindItem_WithNonExistingItem_ShouldThrowItemNotFoundException() {
        // Arrange
        when(itemRepository.findByItemIdAndOrganizationId(anyString(), anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ItemNotFoundException.class, () -> itemService.findItem("item1", "org1"));
        verify(itemRepository, times(1)).findByItemIdAndOrganizationId(anyString(), anyString());
    }

//    @Test
//    public void testCreateItem_WithNewItem_ShouldCreateItem() {
//        // Arrange
//        Item newItem = new Item("000010", "itemNameOne", "itemOneDesc", "ItemOneCategory", "itemOneType", true, 2000, true, true, true,"ORG001");
//
//        when(itemRepository.save(any(Item.class))).thenReturn(newItem);
//
//        // Act
//        Item result = itemService.createItem(newItem, "ORG001");
//
//        // Assert
//        assertNotNull(result);
//        assertEquals("NXT00005", result.getItemId());
//        verify(itemRepository, times(1)).save(any(Item.class));
//    }

    @Test
    public void testCreateItem_WithExistingItem_ShouldThrowItemNotFoundException() {
        // Arrange
        Item existingItem = new Item("00001", "itemNameOne", "itemOneDesc", "ItemOneCategory", "itemOneType", true, 2000, true, true, true,"ORG001");

        when(itemRepository.findByOrganizationId(anyString())).thenReturn(List.of(existingItem));

        // Act & Assert
        assertThrows(ItemNotFoundException.class, () -> itemService.createItem(existingItem, "ORG001"));
        verify(itemRepository, never()).save(any(Item.class));
    }



    @Test
    public void testDeleteItemIfNotPresentInReferenceCollection_WithExistingItemAndNoReferences_ShouldDeleteItem() {
        // Arrange
        Item existingItem = new Item("00001", "itemNameOne", "itemOneDesc", "ItemOneCategory", "itemOneType", true, 2000, true, true, true,"ORG001");
        when(itemRepository.findByItemIdAndOrganizationId(anyString(), anyString())).thenReturn(Optional.of(existingItem));
        when(supplyRepository.findByItemIdAndOrganizationId(anyString(), anyString())).thenReturn(new ArrayList<>());
        when(demandRepository.findByItemIdAndOrganizationId(anyString(), anyString())).thenReturn(new ArrayList<>());

        // Act
        String result = itemService.deleteItemIfNotPresentInReferenceCollection("ORG001_00001", "ORG001");

        // Assert
        assertEquals("Item with itemId:ORG001_00001 deleted", result);
        verify(itemRepository, times(1)).findByItemIdAndOrganizationId(anyString(), anyString());
        verify(supplyRepository, times(1)).findByItemIdAndOrganizationId(anyString(), anyString());
        verify(demandRepository, times(1)).findByItemIdAndOrganizationId(anyString(), anyString());
        verify(itemRepository, times(1)).delete(any(Item.class));
    }

    @Test
    public void testDeleteItemIfNotPresentInReferenceCollection_WithExistingItemAndReferences_ShouldThrowSupplyAndDemandExistException() {
        // Arrange
        Item existingItem = new Item("00001", "itemNameOne", "itemOneDesc", "ItemOneCategory", "itemOneType", true, 2000, true, true, true,"ORG001");
        when(itemRepository.findByItemIdAndOrganizationId(anyString(), anyString())).thenReturn(Optional.of(existingItem));
        when(supplyRepository.findByItemIdAndOrganizationId(anyString(), anyString())).thenReturn(List.of(new Supply("ORG001","ORG001_00001","111","ONHAND",7)));
        when(demandRepository.findByItemIdAndOrganizationId(anyString(), anyString())).thenReturn(List.of(new Demand("ORG001","ONHAND",111,"ORG001_00001","111")));

        // Act & Assert
        assertThrows(SupplyAndDemandExistException.class, () -> itemService.deleteItemIfNotPresentInReferenceCollection("ORG001_00001", "ORG001"));
        verify(itemRepository, times(1)).findByItemIdAndOrganizationId(anyString(), anyString());
        verify(supplyRepository, times(1)).findByItemIdAndOrganizationId(anyString(), anyString());
        verify(demandRepository, times(1)).findByItemIdAndOrganizationId(anyString(), anyString());
        verify(itemRepository, never()).delete(any(Item.class));
    }

    @Test
    public void testDeleteItemIfNotPresentInReferenceCollection_WithNonExistingItem_ShouldThrowItemNotFoundException() {
        // Arrange
        when(itemRepository.findByItemIdAndOrganizationId(anyString(), anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ItemNotFoundException.class, () -> itemService.deleteItemIfNotPresentInReferenceCollection("ORG001_00001", "ORG001"));
        verify(itemRepository, times(1)).findByItemIdAndOrganizationId(anyString(), anyString());
        verify(supplyRepository, never()).findByItemIdAndOrganizationId(anyString(), anyString());
        verify(demandRepository, never()).findByItemIdAndOrganizationId(anyString(), anyString());
        verify(itemRepository, never()).delete(any(Item.class));
    }

    @Test
    public void testUpdateItem_WithExistingItem_ShouldUpdateItem() {
        // Arrange
        Item existingItem = new Item("00001", "itemNameOne", "itemOneDesc", "ItemOneCategory", "itemOneType", true, 2000, true, true, true,"ORG001");
        Item updatedItem = new Item("00001", "itemTwoOne", "itemTwoDesc", "ItemOneCategory", "itemOneType", true, 2000, true, true, true,"ORG001");
        when(itemRepository.findByItemIdAndOrganizationId(anyString(), anyString())).thenReturn(Optional.of(existingItem));
        when(itemRepository.save(any(Item.class))).thenReturn(updatedItem);

        // Act
        Item result = itemService.updateItem("ORG001", updatedItem, "ORG001_00001");

        // Assert
        assertNotNull(result);
        assertEquals("itemTwoOne", result.getItemName());
        assertEquals("ItemOneCategory", result.getCategory());
        assertEquals(2000.0, result.getPrice());
        verify(itemRepository, times(1)).findByItemIdAndOrganizationId(anyString(), anyString());
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    public void testUpdateItem_WithNonExistingItem_ShouldThrowItemNotFoundException() {
        // Arrange
        when(itemRepository.findByItemIdAndOrganizationId(anyString(), anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ItemNotFoundException.class, () -> itemService.updateItem("ORG001", new Item(), "ORG001_001"));
        verify(itemRepository, times(1)).findByItemIdAndOrganizationId(anyString(), anyString());
        verify(itemRepository, never()).save(any(Item.class));
    }
}
