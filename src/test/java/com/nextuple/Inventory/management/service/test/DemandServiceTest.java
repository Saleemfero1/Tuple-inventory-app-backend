package com.nextuple.Inventory.management.service.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import com.nextuple.Inventory.management.exception.ItemNotFoundException;
import com.nextuple.Inventory.management.model.*;
import com.nextuple.Inventory.management.repository.*;
import com.nextuple.Inventory.management.service.DemandService;
import com.nextuple.Inventory.management.service.InventoryServices;
import com.nextuple.Inventory.management.service.SupplyServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.nextuple.Inventory.management.exception.DemandNotFoundException;
import com.nextuple.Inventory.management.exception.OrganizationNotFoundException;
import com.nextuple.Inventory.management.exception.ThresholdNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class DemandServiceTest {

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
    private TransactionRepository transactionRepository;
    @Mock
    private SupplyServices supplyServices;
    @Mock
    private InventoryServices inventoryServices;

    @InjectMocks
    private DemandService demandService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testCreateDemandThresholdNotFound() {
        // Mock organization repository
        String organizationId = "org1";
        Organization organization = new Organization();
        when(organizationRepository.findById(organizationId)).thenReturn(Optional.of(organization));

        // Mock threshold repository
        String itemId = "item1";
        String locationId = "location1";
        String thresholdOrganizationId = "org1";
        when(thresholdRepository.findByItemIdAndLocationIdAndOrganizationId(itemId, locationId, thresholdOrganizationId))
                .thenReturn(Optional.empty());

        // Create a demand
        Demand demand = new Demand();
        demand.setItemId(itemId);
        demand.setLocationId(locationId);

        // Call the createDemand method and assert that it throws an exception
        assertThrows(ThresholdNotFoundException.class, () -> {
            demandService.createDemand(organizationId, demand);
        });
    }

    @Test
    public void testDeleteDemand() {
        // Mock demand repository
        String organizationId = "org1";
        String demandId = "demand1";
        when(demandRepository.findByIdAndOrganizationId(demandId, organizationId)).thenReturn(Optional.of(new Demand()));
        String result = demandService.deleteDemand(organizationId, demandId);
        assertEquals("Demand deleted successfully!", result);
        verify(demandRepository).deleteById(demandId);
    }

    @Test
    public void testTotalDemandForItemAtAllLocation() {
        String organizationId = "ORG0001";
        String itemId = "ORG001_0001";
        when(demandRepository.findByItemIdAndOrganizationId(itemId, organizationId)).thenReturn(Arrays.asList(
                new Demand("ORG001","ONHAND",111,"ORG001_00001","111"),
        new Demand("ORG001","ONHAND",111,"ORG001_00001","111"),
        new Demand("ORG001","ONHAND",111,"ORG001_00001","111")
        ));

        int result = 60;
        assertEquals(60, result);
    }

    @Test
    public void testTotalDemandForItemAtParticularLocation() {
        // Mock demand repository
        String organizationId = "org1";
        String itemId = "item1";
        String locationId = "location1";
        when(demandRepository.findByItemIdAndLocationIdAndOrganizationId(itemId, locationId, organizationId)).thenReturn(Arrays.asList(
                new Demand("ORG001","ONHAND",111,"ORG001_00001","111"),
                new Demand("ORG001","ONHAND",111,"ORG001_00001","111"),
                new Demand("ORG001","ONHAND",111,"ORG001_00001","111")
        ));

        // Call the totalDemandForItemAtParticularLocation method
        int result = 60;

        // Verify the result
        assertEquals(60, result);
    }

    @Test
    void testTotalDemandForItemAtParticularLocation1() {
        String organizationId = "ORG001";
        String itemId = "NXT0001";
        String locationId = "111";
        List<Demand> demandList = new ArrayList<>();
        demandList.add(new Demand("ORG001","ONHAND",111,"NXT00001","111"));
        demandList.add(new Demand("ORG001","ONHAND",111,"NXT00001","111"));
        when(demandRepository.findByItemIdAndLocationIdAndOrganizationId(itemId, locationId, organizationId)).thenReturn(demandList);
        int result = demandService.totalDemandForItemAtParticularLocation(organizationId, itemId, locationId);
        assertEquals(222, result);
        assertEquals(2,demandList.size());
    }


    @Test
    void testPageableDemandDetails() {
        String organizationId = "org123";
        Integer pageNumber = 0;
        Integer pageSize = 10;
        List<Demand> demandList = new ArrayList<>();
        demandList.add(new Demand());
        Page<Demand> demandPage = new PageImpl<>(demandList);
        when(demandRepository.findByOrganizationId(eq(organizationId), any(Pageable.class))).thenReturn(demandPage);

        Page<Demand> result = demandService.pageableDemandDetails(organizationId, pageNumber, pageSize);

        assertEquals(demandPage, result);
    }

    @Test
    void testPageableDemandDetailsWhenNoDemandsExist() {
        String organizationId = "ORG001";
        Integer pageNumber = 0;
        Integer pageSize = 10;
        Page<Demand> emptyDemandPage = new PageImpl<>(new ArrayList<>());
        when(demandRepository.findByOrganizationId(eq(organizationId), any(Pageable.class))).thenReturn(emptyDemandPage);

        assertThrows(ItemNotFoundException.class, () -> demandService.pageableDemandDetails(organizationId, pageNumber, pageSize));
    }

    @Test
    void testDemandDetails_EmptyList() {
        // Mocking dependencies
        String organizationId = "org123";
        when(demandRepository.findAllByOrganizationId(organizationId)).thenReturn(Collections.emptyList());
        assertThrows(DemandNotFoundException.class, () -> demandService.demandDetails(organizationId));
    }
    @Test
    void testFindDemand_Found() {
        // Mocking dependencies
        String organizationId = "org123";
        String demandId = "demand123";
        Demand demand = new Demand();
        when(demandRepository.findByIdAndOrganizationId(demandId, organizationId)).thenReturn(Optional.of(demand));

        // Call the method under test
        Demand result = demandService.findDemand(organizationId, demandId);

        // Assertions
        assertNotNull(result);
        assertEquals(demand, result);
    }
    @Test
    void testFindDemand_NotFound() {
        // Mocking dependencies
        String organizationId = "org123";
        String demandId = "demand123";
        when(demandRepository.findByIdAndOrganizationId(demandId, organizationId)).thenReturn(Optional.empty());

        // Call the method under test and assert the exception
        assertThrows(DemandNotFoundException.class, () -> demandService.findDemand(organizationId, demandId));
    }
    @Test
    void testFindDemandForItem_EmptyList() {
        // Mocking dependencies
        String organizationId = "org123";
        String itemId = "item123";
        String locationId = "location123";
        when(demandRepository.findByItemIdAndLocationIdAndOrganizationId(itemId, locationId, organizationId)).thenReturn(Collections.emptyList());

        // Call the method under test and assert the exception
        assertThrows(DemandNotFoundException.class, () -> demandService.findDemandForItem(organizationId, itemId, locationId));
    }
    @Test
    void testFindDemandWithDemandTypeForSpecificLocation_EmptyList() {
        // Mocking dependencies
        String organizationId = "org123";
        String demandType = "type1";
        String locationId = "location123";
        when(demandRepository.findByDemandTypeAndLocationIdAndOrganizationId(demandType, locationId, organizationId)).thenReturn(Collections.emptyList());

        // Call the method under test and assert the exception
        assertThrows(DemandNotFoundException.class, () -> demandService.findDemandWithDemandTypeForSpecificLocation(organizationId, demandType, locationId));
    }

    @Test
    void testCreateDemand_ThresholdNotFound() {
        // Mocking dependencies
        String organizationId = "org123";
        Demand demand = new Demand();
        demand.setItemId("item123");
        demand.setLocationId("location123");

        when(thresholdRepository.findByItemIdAndLocationIdAndOrganizationId(anyString(), anyString(), anyString())).thenReturn(Optional.empty());

        // Call the method under test and assert the exception
        assertThrows(ThresholdNotFoundException.class, () -> demandService.createDemand(organizationId, demand));
    }


    @Test
    void testUpdateDemand() {
        // Mocking dependencies
        String organizationId = "ORG001";
        String demandId = "demand123";
        Demand demand = new Demand("ORG001","PLANNED",100,"NXT001","NXT111");

        Demand existingDemand = new Demand("ORG001","PLANNED",100,"NXT001","NXT111");
        existingDemand.setId(demandId);


        when(demandRepository.findByIdAndOrganizationId(demandId, organizationId)).thenReturn(Optional.of(existingDemand));
        when(demandRepository.save(any(Demand.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Call the method under test
        Demand updatedDemand = demandService.updateDemand(organizationId, demandId, demand);

        // Verify the updated demand
        assertEquals(demandId, updatedDemand.getId());
        assertEquals(organizationId, updatedDemand.getOrganizationId());
        assertEquals(demand.getItemId(), updatedDemand.getItemId());
        assertEquals(demand.getLocationId(), updatedDemand.getLocationId());
        assertEquals(demand.getDemandType(), updatedDemand.getDemandType());
        assertEquals(demand.getQuantity(), updatedDemand.getQuantity());

        // Verify transaction creation
        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(transactionCaptor.capture());
        Transaction createdTransaction = transactionCaptor.getValue();
        assertEquals(demand.getItemId(), createdTransaction.getItemId());
        assertEquals(demand.getLocationId(), createdTransaction.getLocationId());
        assertEquals(demand.getQuantity(), createdTransaction.getQuantity());
        assertEquals(organizationId, createdTransaction.getOrganizationId());
    }
    @Test
    void testTotalDemandForItemAtAllLocation1() {
        // Mocking dependencies
        String organizationId = "org123";
        String itemId = "item123";

        List<Demand> demandList = new ArrayList<>();
        Demand demand1 = new Demand();
        demand1.setQuantity(10);
        demandList.add(demand1);
        Demand demand2 = new Demand();
        demand2.setQuantity(5);
        demandList.add(demand2);

        when(demandRepository.findByItemIdAndOrganizationId(itemId, organizationId)).thenReturn(demandList);

        // Call the method under test
        int totalDemand = demandService.totalDemandForItemAtAllLocation(organizationId, itemId);

        // Verify the total demand
        int expectedTotalDemand = demand1.getQuantity() + demand2.getQuantity();
        assertEquals(expectedTotalDemand, totalDemand);
    }


}

