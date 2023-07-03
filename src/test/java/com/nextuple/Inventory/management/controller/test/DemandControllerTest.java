package com.nextuple.Inventory.management.controller.test;

import com.nextuple.Inventory.management.controller.DemandController;
import com.nextuple.Inventory.management.model.Demand;
import com.nextuple.Inventory.management.service.DemandService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class DemandControllerTest {

    @Mock
    private DemandService demandService;

    @InjectMocks
    private DemandController demandController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        HttpServletRequest mockRequest = new MockHttpServletRequest();
        ServletRequestAttributes servletRequestAttributes = new ServletRequestAttributes(mockRequest);
        RequestContextHolder.setRequestAttributes(servletRequestAttributes);
    }

    @Test
    public void testDemandDetails() {
        // Mock data
        String organizationId = "org1";
        List<Demand> demandList = Arrays.asList(
                 new Demand( "ORG001","HARDPROMISED", 30,"6383728","01504"),
                 new Demand( "ORG001","HARDPROMISED", 30,"6383728","01504")
        );

        // Mock the service method
        when(demandService.demandDetails(organizationId)).thenReturn(demandList);

        // Call the controller method
        ResponseEntity<List<Demand>> response = demandController.demandDetails(organizationId);

        // Verify the result
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(demandList, response.getBody());
        verify(demandService, times(1)).demandDetails(organizationId);
    }

    @Test
    public void testFindDemand() {
        // Mock data
        String organizationId = "ORG001";
        String demandId = "6383728";
        Demand demand = new Demand("ORG001", "HARDPROMISED", 30, "6383728", "01504");
        demand.setId("6383728");

        // Mock the service method
        when(demandService.findDemand(organizationId, demandId)).thenReturn(demand);

        // Call the controller method
        ResponseEntity<Demand> response = demandController.findDemand(demandId, organizationId);

        // Verify the result
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(demand, response.getBody());
        verify(demandService, times(1)).findDemand(organizationId, demandId);
    }


    @Test
    public void testFindDemandForItemAndLocation() {
        // Mock data
        String organizationId = "ORG001";
        String itemId = "6383728";
        String locationId = "01504";
        List<Demand> demandList = Arrays.asList(
                new Demand( "ORG001","HARDPROMISED", 30,"6383728","01504"),
                 new Demand( "ORG001","HARDPROMISED", 30,"6383728","01504")
        );

        // Mock the service method
        when(demandService.findDemandForItem(organizationId, itemId, locationId)).thenReturn(demandList);

        // Call the controller method
        ResponseEntity<List<Demand>> response = demandController.findDemandForItemAndLocation(organizationId, itemId, locationId);

        // Verify the result
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(demandList, response.getBody());
        verify(demandService, times(1)).findDemandForItem(organizationId, itemId, locationId);
    }

    @Test
    public void testFindDemandWithDemandTypeForSpecificLocation() {

        // Mock data
        String organizationId = "ORG001";
        String itemId = "6383728";
        String demandType="HARDPROMISED";
        String locationId = "01504";
        List<Demand> demandList = Arrays.asList(
                new Demand( "ORG001","HARDPROMISED", 30,"6383728","01504"),
                new Demand( "ORG001","HARDPROMISED", 30,"6383728","01504")
        );

        // Mock the service method
        when(demandService.findDemandWithDemandTypeForSpecificLocation(organizationId, demandType, locationId)).thenReturn(demandList);

        // Call the controller method
        ResponseEntity<List<Demand>> response = demandController.findDemandWithDemandTypeForSpecificLocation(organizationId, demandType, locationId);

        // Verify the result
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(demandList, response.getBody());
        verify(demandService, times(1)).findDemandWithDemandTypeForSpecificLocation(organizationId, demandType, locationId);
    }


    @Test
    public void testCreateDemand() {
        // Mock data
        String organizationId = "ORG001";
        Demand demand = new Demand( "ORG001","HARDPROMISED", 30,"6383728","01504");

        Demand createdDemand = new Demand( "ORG001","HARDPROMISED", 30,"6383728","01504");

        // Mock the service method
        when(demandService.createDemand(organizationId, demand)).thenReturn(createdDemand);

        // Call the controller method
        ResponseEntity<Demand> response = demandController.createDemand(organizationId, demand);

        // Verify the result
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdDemand, response.getBody());

        URI expectedLocationUri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{supplyId}")
                .buildAndExpand(createdDemand.getId())
                .toUri();
        assertEquals(expectedLocationUri, response.getHeaders().getLocation());

        verify(demandService, times(1)).createDemand(organizationId, demand);
    }

    @Test
    public void testDeleteDemand() {
        // Mock data
        String organizationId = "ORG001";
        String demandId = "1";

        // Mock the service method
        when(demandService.deleteDemand(organizationId, demandId)).thenReturn("Demand deleted successfully");

        // Call the controller method
        ResponseEntity<String> response = demandController.deleteDemand(organizationId, demandId);

        // Verify the result
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals("Demand deleted successfully", response.getBody());
        verify(demandService, times(1)).deleteDemand(organizationId, demandId);
    }

    @Test
    public void testUpdateDemand() {
        // Mock data
        String organizationId = "org1";
        String demandId = "1";
        Demand demand = new Demand( "ORG001","HARDPROMISED", 30,"6383728","01504");
        Demand updatedDemand = new Demand( "ORG001","HARDPROMISED", 30,"6383728","01504");

        // Mock the service method
        when(demandService.updateDemand(organizationId, demandId, updatedDemand)).thenReturn(updatedDemand);

        // Call the controller method
        ResponseEntity<Demand> response = demandController.updateSupply(organizationId, demandId, updatedDemand);

        // Verify the result
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedDemand, response.getBody());
        verify(demandService, times(1)).updateDemand(organizationId, demandId, updatedDemand);
    }

}
