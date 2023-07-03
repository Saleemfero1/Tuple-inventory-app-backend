package com.nextuple.Inventory.management.service.test;

import com.nextuple.Inventory.management.exception.ItemNotFoundException;
import com.nextuple.Inventory.management.exception.ThresholdNotFoundException;
import com.nextuple.Inventory.management.service.ThresholdService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
import org.springframework.data.domain.Pageable;

import static org.mockito.Mockito.when;

public class ThresholdServicesTest {
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
    private SupplyServices supplyServices;
    @Mock
    private InventoryServices inventoryServices;

    @InjectMocks
    private ThresholdService thresholdService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testThresholdDetails() {
        // Mock organization repository
        String organizationId = "org1";
        when(organizationRepository.findById(organizationId)).thenReturn(Optional.of(new Organization()));

        // Mock threshold repository
        when(thresholdRepository.findAllByOrganizationId(organizationId)).thenReturn(Arrays.asList(
                new Threshold(),
                new Threshold(),
                new Threshold()
        ));

        // Call the thresholdDetails method
        List<Threshold> result = thresholdService.thresholdDetails(organizationId);

        // Verify the result
        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    public void testFindThresholdById() {
        // Mock organization repository
        String organizationId = "org1";
        when(organizationRepository.findById(organizationId)).thenReturn(Optional.of(new Organization()));

        // Mock threshold repository
        String thresholdId = "threshold1";
        when(thresholdRepository.findByIdAndOrganizationId(thresholdId, organizationId)).thenReturn(Optional.of(new Threshold()));

        // Call the findThresholdById method
        Threshold result = thresholdService.findThresholdById(organizationId, thresholdId);

        // Verify the result
        assertNotNull(result);
    }

    @Test
    public void testFindThresholdByItemIdAtLocation() {
        // Mock organization repository
        String organizationId = "org1";
        when(organizationRepository.findById(organizationId)).thenReturn(Optional.of(new Organization()));

        // Mock threshold repository
        String itemId = "item1";
        String locationId = "location1";
        when(thresholdRepository.findByItemIdAndLocationIdAndOrganizationId(itemId, locationId, organizationId)).thenReturn(Optional.of(new Threshold()));

        // Call the findThresholdByItemIdAtLocation method
        Threshold result = thresholdService.findThresholdByItemIdAtLocation(organizationId, itemId, locationId);

        // Verify the result
        assertNotNull(result);
    }

    @Test
    public void testCreateThreshold() {
        // Mock organization repository
        String organizationId = "org1";
        when(organizationRepository.findById(organizationId)).thenReturn(Optional.of(new Organization()));

        // Mock threshold repository
        when(thresholdRepository.findByItemIdAndLocationIdAndOrganizationId(anyString(), anyString(), anyString())).thenReturn(Optional.empty());
        when(thresholdRepository.save(any(Threshold.class))).thenReturn(new Threshold());

        // Call the createThreshold method
        Threshold result = thresholdService.createThreshold(organizationId, new Threshold());

        // Verify the result
        assertNotNull(result);
        // Verify that thresholdRepository.save(threshold) was called
        verify(thresholdRepository).save(any(Threshold.class));
    }

    @Test
    public void testDeleteThreshold() {
        // Mock organization repository
        String organizationId = "org1";
        when(organizationRepository.findById(organizationId)).thenReturn(Optional.of(new Organization()));

        // Mock threshold repository
        String thresholdId = "threshold1";
        when(thresholdRepository.findByIdAndOrganizationId(thresholdId, organizationId)).thenReturn(Optional.of(new Threshold()));

        // Call the deleteThreshold method
        thresholdService.deleteThreshold(organizationId, thresholdId);

        // Verify that thresholdRepository.deleteById(thresholdId) was called
        verify(thresholdRepository).deleteById(thresholdId);
    }

    @Test
    void updateThreshold_ShouldReturnUpdatedThreshold_WhenThresholdExists() {
        // Arrange
        String organizationId = "mockOrganizationId";
        String thresholdId = "mockThresholdId";
        Threshold thresholdToUpdate = new Threshold("ORG001","NXT001","NXT111",10,100);
        Threshold existingThreshold = new Threshold("ORG001","NXT001","NXT111",10,100);
        existingThreshold.setId(thresholdId);
        existingThreshold.setOrganizationId(organizationId);

        when(thresholdRepository.findByIdAndOrganizationId(thresholdId, organizationId))
                .thenReturn(Optional.of(existingThreshold));
        when(thresholdRepository.save(any(Threshold.class))).thenReturn(existingThreshold);

        // Act
        Threshold updatedThreshold = thresholdService.updateThreshold(organizationId, thresholdId, thresholdToUpdate);

        // Assert
        assertNotNull(updatedThreshold);
        assertEquals(thresholdId, updatedThreshold.getId());
        assertEquals(organizationId, updatedThreshold.getOrganizationId());
        assertEquals(thresholdToUpdate.getMinThreshold(), updatedThreshold.getMinThreshold());
        assertEquals(thresholdToUpdate.getMaxThreshold(), updatedThreshold.getMaxThreshold());
        verify(thresholdRepository, times(1)).findByIdAndOrganizationId(thresholdId, organizationId);
        verify(thresholdRepository, times(1)).save(existingThreshold);
    }
    @Test
    void updateThreshold_ShouldThrowThresholdNotFoundException_WhenThresholdDoesNotExist() {
        // Arrange
        String organizationId = "mockOrganizationId";
        String thresholdId = "mockThresholdId";
        Threshold thresholdToUpdate = new Threshold();
        thresholdToUpdate.setMinThreshold(10);
        thresholdToUpdate.setMaxThreshold(100);
        when(thresholdRepository.findByIdAndOrganizationId(thresholdId, organizationId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ThresholdNotFoundException.class,
                () -> thresholdService.updateThreshold(organizationId, thresholdId, thresholdToUpdate));
        verify(thresholdRepository, times(1)).findByIdAndOrganizationId(thresholdId, organizationId);
        verify(thresholdRepository, never()).save(any(Threshold.class));
    }

    @Test
    void updateThresholdByItemIdAndLocationId_ShouldReturnUpdatedThreshold_WhenThresholdExists() {
        // Arrange
        String organizationId = "ORG001";
        String itemId = "NXT001";
        String locationId = "NXT111";
        Threshold thresholdToUpdate = new Threshold("ORG001","NXT001","NXT111",10,100);
        Threshold existingThreshold = new Threshold("ORG001","NXT001","NXT111",10,100);

        when(thresholdRepository.findByItemIdAndLocationIdAndOrganizationId(itemId, locationId, organizationId))
                .thenReturn(Optional.of(existingThreshold));
        when(thresholdRepository.save(any(Threshold.class))).thenReturn(existingThreshold);

        // Act
        Threshold updatedThreshold = thresholdService.updateThresholdByItemIdAndLocationId(organizationId, itemId, locationId, thresholdToUpdate);

        // Assert
        assertNotNull(updatedThreshold);
        assertEquals(organizationId, updatedThreshold.getOrganizationId());
        assertEquals(itemId, updatedThreshold.getItemId());
        assertEquals(locationId, updatedThreshold.getLocationId());
        assertEquals(thresholdToUpdate.getMinThreshold(), updatedThreshold.getMinThreshold());
        assertEquals(thresholdToUpdate.getMaxThreshold(), updatedThreshold.getMaxThreshold());
        verify(thresholdRepository, times(1)).findByItemIdAndLocationIdAndOrganizationId(itemId, locationId, organizationId);
        verify(thresholdRepository, times(1)).save(existingThreshold);
    }

    @Test
    void updateThresholdByItemIdAndLocationId_ShouldThrowThresholdNotFoundException_WhenThresholdDoesNotExist() {
        // Arrange
        String organizationId = "mockOrganizationId";
        String itemId = "mockItemId";
        String locationId = "mockLocationId";
        Threshold thresholdToUpdate = new Threshold();
        thresholdToUpdate.setMinThreshold(10);
        thresholdToUpdate.setMaxThreshold(100);
        when(thresholdRepository.findByItemIdAndLocationIdAndOrganizationId(itemId, locationId, organizationId))
                .thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ThresholdNotFoundException.class,
                () -> thresholdService.updateThresholdByItemIdAndLocationId(organizationId, itemId, locationId, thresholdToUpdate));
        verify(thresholdRepository, times(1)).findByItemIdAndLocationIdAndOrganizationId(itemId, locationId, organizationId);
        verify(thresholdRepository, never()).save(any(Threshold.class));
    }

    @Test
    void pageableThresholdDetails_ShouldReturnPageOfThresholds_WhenThresholdsExist() {
        // Arrange
        String organizationId = "mockOrganizationId";
        int pageNumber = 0;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Threshold threshold = new Threshold();
        threshold.setOrganizationId(organizationId);
        Page<Threshold> thresholdPage = new PageImpl<>(List.of(threshold), pageable, 1);
        when(thresholdRepository.findByOrganizationId(organizationId, pageable)).thenReturn(thresholdPage);

        // Act
        Page<Threshold> result = thresholdService.pageableThresholdDetails(organizationId, pageNumber, pageSize);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(threshold, result.getContent().get(0));
        verify(thresholdRepository, times(1)).findByOrganizationId(organizationId, pageable);
    }

    @Test
    void pageableThresholdDetails_ShouldThrowItemNotFoundException_WhenThresholdsDoNotExist() {
        // Arrange
        String organizationId = "mockOrganizationId";
        int pageNumber = 0;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Threshold> thresholdPage = new PageImpl<>(List.of());
        when(thresholdRepository.findByOrganizationId(organizationId, pageable)).thenReturn(thresholdPage);

        // Act and Assert
        assertThrows(ItemNotFoundException.class,
                () -> thresholdService.pageableThresholdDetails(organizationId, pageNumber, pageSize));
        verify(thresholdRepository, times(1)).findByOrganizationId(organizationId, pageable);
    }

}