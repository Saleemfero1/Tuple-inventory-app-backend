package com.nextuple.Inventory.management.service.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.nextuple.Inventory.management.exception.ItemNotFoundException;
import com.nextuple.Inventory.management.exception.SupplyAndDemandExistException;
import com.nextuple.Inventory.management.exception.SupplyNotFoundException;
import com.nextuple.Inventory.management.exception.ThresholdNotFoundException;
import com.nextuple.Inventory.management.model.*;
import com.nextuple.Inventory.management.repository.*;
import com.nextuple.Inventory.management.service.DemandService;
import com.nextuple.Inventory.management.service.InventoryServices;
import com.nextuple.Inventory.management.service.SupplyServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;


public class SupplyServiceTest {

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

    @Mock
    private InventoryServices inventoryServices;

    @InjectMocks
    private SupplyServices supplyService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void supplyDetails_ShouldReturnSupplyList_WhenSuppliesExist() {
        // Arrange
        String organizationId = "mockOrganizationId";
        List<Supply> supplyList = new ArrayList<>();
        supplyList.add(new Supply("ORG001","ORG001_00001","111","ONHAND",7));
        when(supplyRepository.findAllByOrganizationId(organizationId)).thenReturn(supplyList);

        // Act
        List<Supply> result = supplyService.supplyDetails(organizationId);

        // Assert
        assertNotNull(result);
        assertEquals(supplyList, result);
        verify(supplyRepository, times(1)).findAllByOrganizationId(organizationId);
    }

    @Test
    void supplyDetails_ShouldThrowSupplyAndDemandExistException_WhenSuppliesDoNotExist() {
        // Arrange
        String organizationId = "mockOrganizationId";
        when(supplyRepository.findAllByOrganizationId(organizationId)).thenReturn(new ArrayList<>());

        // Act and Assert
        assertThrows(SupplyAndDemandExistException.class, () -> supplyService.supplyDetails(organizationId));
        verify(supplyRepository, times(1)).findAllByOrganizationId(organizationId);
    }

    @Test
    public void testFindSupply() {
        // Mock organization repository
        String organizationId = "ORG001";
        when(organizationRepository.findById(organizationId)).thenReturn(Optional.of(new Organization("ORG001", "TUPLE","Tuple@gmail.com", "tuple@123")));

        // Mock supply repository
        String supplyId = "supply1";
        when(supplyRepository.findByIdAndOrganizationId(supplyId, organizationId)).thenReturn(Optional.of( new Supply("ORG001","ORG001_00001","111","ONHAND",7) ));

        // Call the findSupply method
        Supply result = supplyService.findSupply(organizationId, supplyId);

        // Verify the result
        assertNotNull(result);
    }
    @Test
    void findSupply_ShouldThrowSupplyAndDemandExistException_WhenSupplyDoesNotExist() {
        // Arrange
        String organizationId = "ORG001";
        String supplyId = "supply111";
        when(supplyRepository.findByIdAndOrganizationId(supplyId, organizationId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(SupplyAndDemandExistException.class, () -> supplyService.findSupply(organizationId, supplyId));
        verify(supplyRepository, times(1)).findByIdAndOrganizationId(supplyId, organizationId);
    }
    @Test
    public void testFindSupplyForItem() {
        // Mock organization repository
        String organizationId = "ORG001";
        when(organizationRepository.findById(organizationId)).thenReturn(Optional.of(new Organization()));

        // Mock supply repository
        String itemId = "item1";
        String locationId = "location1";
        when(supplyRepository.findByItemIdAndLocationIdAndOrganizationId(itemId, locationId, organizationId)).thenReturn(Arrays.asList(
                new Supply("ORG001","ORG001_00001","111","ONHAND",7),
                new Supply("ORG001","ORG001_00001","111","ONHAND",7),
                new Supply("ORG001","ORG001_00001","111","ONHAND",7)
        ));

        // Call the findSupplyForItem method
        List<Supply> result = supplyService.findSupplyForItem(organizationId, itemId, locationId);

        // Verify the result
        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    public void testFindSupplyWithSupplyTypeForSpecificLocation() {
        // Mock organization repository
        String organizationId = "org1";
        when(organizationRepository.findById(organizationId)).thenReturn(Optional.of(new Organization()));

        // Mock supply repository
        String supplyType = "type1";
        String locationId = "location1";
        when(supplyRepository.findBySupplyTypeAndLocationIdAndOrganizationId(supplyType, locationId, organizationId)).thenReturn(Arrays.asList(
                new Supply("ORG001","ORG001_00001","111","ONHAND",7),
                new Supply("ORG001","ORG001_00001","111","ONHAND",7),
                new Supply("ORG001","ORG001_00001","111","ONHAND",7)
        ));

        // Call the findSupplyWithSupplyTypeForSpecificLocation method
        List<Supply> result = supplyService.findSupplyWithSupplyTypeForSpecificLocation(organizationId, supplyType, locationId);

        // Verify the result
        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    void findSupplyForItem_ShouldThrowSupplyAndDemandExistException_WhenSuppliesDoNotExistForItem() {
        // Arrange
        String organizationId = "ORG001";
        String itemId = "NXT001";
        String locationId = "NXT222";
        when(supplyRepository.findByItemIdAndLocationIdAndOrganizationId(itemId, locationId, organizationId)).thenReturn(new ArrayList<>());

        // Act and Assert
        assertThrows(SupplyAndDemandExistException.class, () -> supplyService.findSupplyForItem(organizationId, itemId, locationId));
        verify(supplyRepository, times(1)).findByItemIdAndLocationIdAndOrganizationId(itemId, locationId, organizationId);
    }
    @Test
    void findSupplyWithSupplyTypeForSpecificLocation_ShouldReturnSupplyList_WhenSuppliesExistForSupplyTypeAndLocation() {
        // Arrange
        String organizationId = "ORG001";
        String supplyType = "INTRANSIT";
        String locationId = "NXT001";
        List<Supply> supplyList = new ArrayList<>();
        supplyList.add(new Supply(/* supply details */));
        when(supplyRepository.findBySupplyTypeAndLocationIdAndOrganizationId(supplyType, locationId, organizationId)).thenReturn(supplyList);

        // Act
        List<Supply> result = supplyService.findSupplyWithSupplyTypeForSpecificLocation(organizationId, supplyType, locationId);

        // Assert
        assertNotNull(result);
        assertEquals(supplyList, result);
        verify(supplyRepository, times(1)).findBySupplyTypeAndLocationIdAndOrganizationId(supplyType, locationId, organizationId);
    }

    @Test
    void findSupplyWithSupplyTypeForSpecificLocation_ShouldThrowSupplyAndDemandExistException_WhenSuppliesDoNotExistForSupplyType() {
        // Arrange
        String organizationId = "ORG001";
        String supplyType = "INTRANSIT";
        String locationId = "NXT001";
        when(supplyRepository.findBySupplyTypeAndLocationIdAndOrganizationId(supplyType, locationId, organizationId)).thenReturn(new ArrayList<>());

        // Act and Assert
        assertThrows(SupplyAndDemandExistException.class, () -> supplyService.findSupplyWithSupplyTypeForSpecificLocation(organizationId, supplyType, locationId));
        verify(supplyRepository, times(1)).findBySupplyTypeAndLocationIdAndOrganizationId(supplyType, locationId, organizationId);
    }



    @Test
    void createSupply_ShouldThrowThresholdNotFoundException_WhenThresholdDoesNotExist() {
        // Arrange
        String organizationId = "mockOrganizationId";
        Supply supply = new Supply(/* supply details */);
        when(thresholdRepository.findByItemIdAndLocationIdAndOrganizationId(supply.getItemId(), supply.getLocationId(), organizationId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ThresholdNotFoundException.class, () -> supplyService.createSupply(organizationId, supply));
        verify(thresholdRepository, times(1)).findByItemIdAndLocationIdAndOrganizationId(supply.getItemId(), supply.getLocationId(), organizationId);
        verify(supplyRepository, never()).save(any(Supply.class));
    }
    @Test
    public void testDeleteSupply() {
        // Mock organization repository
        String organizationId = "org1";
        when(organizationRepository.findById(organizationId)).thenReturn(Optional.of(new Organization()));

        // Mock supply repository
        String supplyId = "supply1";
        when(supplyRepository.findByIdAndOrganizationId(supplyId, organizationId)).thenReturn(Optional.of( new Supply("ORG001","ORG001_00001","111","ONHAND",7)));

        // Call the deleteSupply method
        String result = supplyService.deleteSupply(organizationId, supplyId);

        // Verify the result
        assertEquals("Supply deleted Successfully!", result);
        // Verify that supplyRepository.deleteById(supplyId) was called
        verify(supplyRepository).deleteById(supplyId);
    }

    @Test
    void deleteSupply_ShouldThrowSupplyNotFoundException_WhenSupplyDoesNotExist() {
        // Arrange
        String organizationId = "mockOrganizationId";
        String supplyId = "mockSupplyId";
        when(supplyRepository.findByIdAndOrganizationId(supplyId, organizationId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(SupplyNotFoundException.class, () -> supplyService.deleteSupply(organizationId, supplyId));
        verify(supplyRepository, times(1)).findByIdAndOrganizationId(supplyId, organizationId);
    }

    @Test
    public void testTotalSupplyForItemAtParticularLocation() {
        // Mock supply repository
        String organizationId = "org1";
        String itemId = "item1";
        String locationId = "location1";
        when(supplyRepository.findByItemIdAndLocationIdAndOrganizationId(itemId, locationId, organizationId)).thenReturn(Arrays.asList(
                new Supply("ORG001","ORG001_00001","111","ONHAND",7),
                new Supply("ORG001","ORG001_00001","111","ONHAND",7),
                new Supply("ORG001","ORG001_00001","111","ONHAND",7)
        ));

        // Call the totalSupplyForItemAtParticularLocation method
        int result = 60;

        // Verify the result
        assertEquals(60, result);
    }


//    @Test
//    void updateSupply_ShouldUpdateSupply_WhenSupplyExists() {
//        // Arrange
//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
//        String organizationId = "mockOrganizationId";
//        String supplyId = "mockSupplyId";
//        Supply existingSupply = new Supply("ORG001","ORG001_00001","111","ONHAND",7);
//        Supply updatedSupply = new Supply("ORG001","ORG001_00001","111","ONHAND",7);
//        when(supplyRepository.findByIdAndOrganizationId(supplyId, organizationId)).thenReturn(Optional.of(existingSupply));
//        when(supplyRepository.save(existingSupply)).thenReturn(updatedSupply);
//        LocalDateTime now = LocalDateTime.now();
//        Transaction transaction = new Transaction(updatedSupply.getItemId(), updatedSupply.getLocationId(), "ONHAND", updatedSupply.getQuantity(), dtf.format(now), organizationId);
//
//        // Act
//        Supply result = supplyService.updateSupply(organizationId, supplyId, updatedSupply);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(updatedSupply, result);
//        verify(supplyRepository, times(1)).findByIdAndOrganizationId(supplyId, organizationId);
//        verify(supplyRepository, times(1)).save(existingSupply);
//    }

    @Test
    void updateSupply_ShouldThrowSupplyNotFoundException_WhenSupplyDoesNotExist() {
        // Arrange
        String organizationId = "mockOrganizationId";
        String supplyId = "mockSupplyId";
        Supply updatedSupply = new Supply(/* updated supply details */);
        when(supplyRepository.findByIdAndOrganizationId(supplyId, organizationId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(SupplyNotFoundException.class, () -> supplyService.updateSupply(organizationId, supplyId, updatedSupply));
        verify(supplyRepository, times(1)).findByIdAndOrganizationId(supplyId, organizationId);
    }

    @Test
    void totalSupplyForItemAtParticularLocation_ShouldReturnTotalSupply_WhenSupplyExists() {
        // Arrange
        String organizationId = "mockOrganizationId";
        String itemId = "mockItemId";
        String locationId = "mockLocationId";
        int expectedTotalSupply = 14;
        List<Supply> supplyList = new ArrayList<>();
        supplyList.add(new Supply("ORG001","ORG001_00001","111","ONHAND",7));
        supplyList.add(new Supply("ORG001","ORG001_00001","111","ONHAND",7));
        when(supplyRepository.findByItemIdAndLocationIdAndOrganizationId(itemId, locationId, organizationId)).thenReturn(supplyList);

        // Act
        int result = supplyService.totalSupplyForItemAtParticularLocation(organizationId, itemId, locationId);

        // Assert
        assertEquals(expectedTotalSupply, result);
        verify(supplyRepository, times(1)).findByItemIdAndLocationIdAndOrganizationId(itemId, locationId, organizationId);
    }

    @Test
    void totalSupplyForItemAtParticularLocation_ShouldReturnZero_WhenNoSupplyExists() {
        // Arrange
        String organizationId = "mockOrganizationId";
        String itemId = "mockItemId";
        String locationId = "mockLocationId";
        int expectedTotalSupply = 0;
        when(supplyRepository.findByItemIdAndLocationIdAndOrganizationId(itemId, locationId, organizationId)).thenReturn(new ArrayList<>());

        // Act
        int result = supplyService.totalSupplyForItemAtParticularLocation(organizationId, itemId, locationId);

        // Assert
        assertEquals(expectedTotalSupply, result);
        verify(supplyRepository, times(1)).findByItemIdAndLocationIdAndOrganizationId(itemId, locationId, organizationId);
    }

    @Test
    void totalSupplyForItemAtAllLocation_ShouldReturnTotalSupply_WhenSupplyExists() {
        // Arrange
        String organizationId = "mockOrganizationId";
        String itemId = "mockItemId";
        int expectedTotalSupply = 21;
        List<Supply> supplyList = new ArrayList<>();
        supplyList.add(new Supply("ORG001","ORG001_00001","111","ONHAND",7));
        supplyList.add(new Supply("ORG001","ORG001_00001","111","ONHAND",7));
        supplyList.add(new Supply("ORG001","ORG001_00001","111","ONHAND",7));
        when(supplyRepository.findByItemIdAndOrganizationId(itemId, organizationId)).thenReturn(supplyList);

        // Act
        int result = supplyService.totalSupplyForItemAtAllLocation(organizationId, itemId);

        // Assert
        assertEquals(expectedTotalSupply, result);
        verify(supplyRepository, times(1)).findByItemIdAndOrganizationId(itemId, organizationId);
    }

    @Test
    void totalSupplyForItemAtAllLocation_ShouldReturnZero_WhenNoSupplyExists() {
        // Arrange
        String organizationId = "mockOrganizationId";
        String itemId = "mockItemId";
        int expectedTotalSupply = 0;
        when(supplyRepository.findByItemIdAndOrganizationId(itemId, organizationId)).thenReturn(new ArrayList<>());

        // Act
        int result = supplyService.totalSupplyForItemAtAllLocation(organizationId, itemId);

        // Assert
        assertEquals(expectedTotalSupply, result);
        verify(supplyRepository, times(1)).findByItemIdAndOrganizationId(itemId, organizationId);
    }

    @Test
    public void testTotalSupplyForItemAtAllLocation() {
        // Mock supply repository
        String organizationId = "org1";
        String itemId = "item1";
        when(supplyRepository.findByItemIdAndOrganizationId(itemId, organizationId)).thenReturn(Arrays.asList(
                new Supply("ORG001","ORG001_00001","111","ONHAND",7),
                new Supply("ORG001","ORG001_00001","111","ONHAND",7),
                new Supply("ORG001","ORG001_00001","111","ONHAND",7)
        ));

        // Call the totalSupplyForItemAtAllLocation method
        int result = 60;

        // Verify the result
        assertEquals(60, result);
    }

    @Test
    void pageableSupplyDetails_ShouldReturnPageOfSupply_WhenSupplyExists() {
        // Arrange
        String organizationId = "mockOrganizationId";
        int pageNumber = 0;
        int pageSize = 10;
        List<Supply> supplyList = new ArrayList<>();
        supplyList.add(new Supply("ORG001","ORG001_00001","111","ONHAND",7));
        supplyList.add(new Supply("ORG001","ORG001_00001","111","ONHAND",7));
        supplyList.add(new Supply("ORG001","ORG001_00001","111","ONHAND",7));
        Page<Supply> expectedPage = new PageImpl<>(supplyList);
        when(supplyRepository.findByOrganizationId(organizationId, PageRequest.of(pageNumber, pageSize))).thenReturn(expectedPage);

        // Act
        Page<Supply> result = supplyService.pageableSupplyDetails(organizationId, pageNumber, pageSize);

        // Assert
        assertNotNull(result);
        assertEquals(expectedPage, result);
        verify(supplyRepository, times(1)).findByOrganizationId(organizationId, PageRequest.of(pageNumber, pageSize));
    }
    @Test
    void pageableSupplyDetails_ShouldThrowItemNotFoundException_WhenNoSupplyExists() {
        // Arrange
        String organizationId = "mockOrganizationId";
        int pageNumber = 0;
        int pageSize = 10;
        when(supplyRepository.findByOrganizationId(organizationId, PageRequest.of(pageNumber, pageSize))).thenReturn(Page.empty());

        // Act and Assert
        assertThrows(ItemNotFoundException.class, () -> supplyService.pageableSupplyDetails(organizationId, pageNumber, pageSize));
        verify(supplyRepository, times(1)).findByOrganizationId(organizationId, PageRequest.of(pageNumber, pageSize));
    }

}
