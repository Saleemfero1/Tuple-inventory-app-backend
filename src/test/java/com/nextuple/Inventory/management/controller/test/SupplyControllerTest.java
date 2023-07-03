package com.nextuple.Inventory.management.controller.test;


import com.nextuple.Inventory.management.controller.SupplyController;
import com.nextuple.Inventory.management.model.Supply;
        import com.nextuple.Inventory.management.service.SupplyServices;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
        import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
        import org.mockito.MockitoAnnotations;
        import org.springframework.http.HttpStatus;
        import org.springframework.http.MediaType;
        import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
        import java.util.ArrayList;
        import java.util.List;

        import static org.junit.jupiter.api.Assertions.assertEquals;
        import static org.mockito.ArgumentMatchers.*;
        import static org.mockito.Mockito.*;

class SupplyControllerTest {

    @Mock
    private SupplyServices supplyServices;
    @InjectMocks
    private SupplyController supplyController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        HttpServletRequest mockRequest = new MockHttpServletRequest();
        ServletRequestAttributes servletRequestAttributes = new ServletRequestAttributes(mockRequest);
        RequestContextHolder.setRequestAttributes(servletRequestAttributes);
    }

    @Test
    void testSupplyDetails() {
        String organizationId = "ORG001";
        List<Supply> supplyList = new ArrayList<>();
        supplyList.add(new Supply());
        supplyList.add(new Supply());

        when(supplyServices.supplyDetails(organizationId)).thenReturn(supplyList);

        ResponseEntity<List<Supply>> response = supplyController.supplyDetails(organizationId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(supplyList, response.getBody());

        verify(supplyServices, times(1)).supplyDetails(organizationId);
        verifyNoMoreInteractions(supplyServices);
    }

    @Test
    void testFindSupply() {
        String organizationId = "ORG001";
        String supplyId = "SUP001";
        Supply supply = new Supply();

        when(supplyServices.findSupply(organizationId, supplyId)).thenReturn(supply);

        ResponseEntity<Supply> response = supplyController.findSupply(organizationId, supplyId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(supply, response.getBody());

        verify(supplyServices, times(1)).findSupply(organizationId, supplyId);
        verifyNoMoreInteractions(supplyServices);
    }

    @Test
    void testCreateSupply() {
        String organizationId = "ORG001";
        Supply supply = new Supply();
        supply.setId("SUP001");

        when(supplyServices.createSupply(eq(organizationId), any(Supply.class))).thenReturn(supply);

        ResponseEntity<Supply> response = supplyController.createSupply(organizationId, supply);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(supply, response.getBody());

        URI expectedLocation = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{supplyId}")
                .buildAndExpand(supply.getId())
                .toUri();
        assertEquals(expectedLocation, response.getHeaders().getLocation());

        verify(supplyServices, times(1)).createSupply(eq(organizationId), any(Supply.class));
        verifyNoMoreInteractions(supplyServices);
    }

    @Test
    void testFindSupplyIfItemExistAtLocation() {
        String organizationId = "ORG001";
        String itemId = "ITEM001";
        String locationId = "LOCATION001";
        List<Supply> supplyList = new ArrayList<>();
        supplyList.add(new Supply());
        supplyList.add(new Supply());

        when(supplyServices.findSupplyForItem(organizationId, itemId, locationId)).thenReturn(supplyList);

        ResponseEntity<List<Supply>> response = supplyController.findSupplyIfItemExistAtLocation(organizationId, itemId, locationId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(supplyList, response.getBody());

        verify(supplyServices, times(1)).findSupplyForItem(organizationId, itemId, locationId);
        verifyNoMoreInteractions(supplyServices);
    }

    @Test
    void testDeleteSupply() {
        String organizationId = "ORG001";
        String supplyId = "SUP001";

        when(supplyServices.deleteSupply(organizationId, supplyId)).thenReturn("Supply is deleted!");

        ResponseEntity<String> response = supplyController.deleteSupply(organizationId, supplyId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals("Supply is deleted!", response.getBody());

        verify(supplyServices, times(1)).deleteSupply(organizationId, supplyId);
        verifyNoMoreInteractions(supplyServices);
    }

    @Test
    void testFindSupplyWithSupplyTypeForSpecificLocation() {
        String organizationId = "ORG001";
        String supplyType = "TYPE001";
        String locationId = "LOCATION001";
        List<Supply> supplyList = new ArrayList<>();
        supplyList.add(new Supply());
        supplyList.add(new Supply());

        when(supplyServices.findSupplyWithSupplyTypeForSpecificLocation(organizationId, supplyType, locationId)).thenReturn(supplyList);

        ResponseEntity<List<Supply>> response = supplyController.findSupplyWithSupplyTypeForSpecificLocation(organizationId, supplyType, locationId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(supplyList, response.getBody());

        verify(supplyServices, times(1)).findSupplyWithSupplyTypeForSpecificLocation(organizationId, supplyType, locationId);
        verifyNoMoreInteractions(supplyServices);
    }
    @Test
    void testUpdateSupply() {
        String organizationId = "ORG001";
        String supplyId = "SUP001";
        Supply supply = new Supply();

        when(supplyServices.updateSupply(eq(organizationId), eq(supplyId), any(Supply.class))).thenReturn(supply);

        ResponseEntity<Supply> response = supplyController.updateSupply(organizationId, supplyId, supply);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(supply, response.getBody());

        verify(supplyServices, times(1)).updateSupply(eq(organizationId), eq(supplyId), any(Supply.class));
        verifyNoMoreInteractions(supplyServices);
    }
}

