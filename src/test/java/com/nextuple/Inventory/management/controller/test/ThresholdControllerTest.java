package com.nextuple.Inventory.management.controller.test;
import com.nextuple.Inventory.management.controller.ThresholdController;
import com.nextuple.Inventory.management.model.Threshold;
import com.nextuple.Inventory.management.service.ThresholdService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)

class ThresholdControllerTest {


    private MockMvc mockMvc;
    @Mock
    private ThresholdService thresholdService;

    @InjectMocks
    private ThresholdController thresholdController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        HttpServletRequest mockRequest = new MockHttpServletRequest();
        ServletRequestAttributes servletRequestAttributes = new ServletRequestAttributes(mockRequest);
        RequestContextHolder.setRequestAttributes(servletRequestAttributes);
    }

    @Test
    void testThresholdDetails() {
        String organizationId = "ORG001";
        List<Threshold> thresholdList = Arrays.asList(
                new Threshold("0001","123","111",10,100),
                new Threshold("0001","124","222",100,1000)
        );
        when(thresholdService.thresholdDetails(organizationId)).thenReturn(thresholdList);

        ResponseEntity<List<Threshold>> response = thresholdController.thresholdDetails(organizationId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(thresholdList, response.getBody());

        verify(thresholdService, times(1)).thresholdDetails(organizationId);
        verifyNoMoreInteractions(thresholdService);
    }

    @Test
    void testFindThresholdById() {
        String organizationId = "ORG001";
        String thresholdId = "THRESHOLD001";
        Threshold threshold =new Threshold("0001","123","111",10,100);

        when(thresholdService.findThresholdById(organizationId, thresholdId)).thenReturn(threshold);

        ResponseEntity<Threshold> response = thresholdController.findThresholdById(organizationId, thresholdId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(threshold, response.getBody());

        verify(thresholdService, times(1)).findThresholdById(organizationId, thresholdId);
        verifyNoMoreInteractions(thresholdService);
    }

    @Test
    void testFindThresholdByItemIdAtLocationId() {
        String organizationId = "ORG001";
        String itemId = "ITEM001";
        String locationId = "LOCATION001";
        Threshold threshold =new Threshold("ORG001","ITEM001","LOCATION001",10,100);
        when(thresholdService.findThresholdByItemIdAtLocation(organizationId, itemId, locationId))
                .thenReturn(threshold);

        ResponseEntity<Threshold> response = thresholdController.findThresholdByItemIdAtLocationId(
                organizationId, itemId, locationId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(threshold, response.getBody());

        verify(thresholdService, times(1)).findThresholdByItemIdAtLocation(organizationId, itemId, locationId);
        verifyNoMoreInteractions(thresholdService);
    }

    @Test
    void testDeleteThreshold() {
        String organizationId = "ORG001";
        String thresholdId = "THRESHOLD001";

        ResponseEntity<String> response = thresholdController.deleteThreshold(organizationId, thresholdId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals("Threshold:THRESHOLD001 Deleted!", response.getBody());

        verify(thresholdService, times(1)).deleteThreshold(organizationId, thresholdId);
        verifyNoMoreInteractions(thresholdService);
    }

    @Test
    public void testUpdateThreshold() {
        String organizationId = "ORG001";
        String thresholdId = "THRESHOLD001";
        Threshold threshold = new Threshold("ORG001", "ITEM001", "LOCATION001", 5, 50);
        Threshold updatedThreshold = new Threshold("ORG001", "ITEM001", "LOCATION001", 5, 50);
        updatedThreshold.setId("THRESHOLD001");
        threshold.setId("THRESHOLD001");


        ThresholdService thresholdService = mock(ThresholdService.class);
        when(thresholdService.updateThreshold(eq(organizationId), eq(thresholdId), any(Threshold.class))).thenReturn(updatedThreshold);


        Threshold threshold1 = thresholdService.updateThreshold(organizationId, thresholdId, threshold);
        ResponseEntity<Threshold> response = thresholdController.updateThreshold(organizationId, thresholdId, threshold);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedThreshold, threshold1);

        verify(thresholdService, times(1)).updateThreshold(eq(organizationId), eq(thresholdId), any(Threshold.class));
        verifyNoMoreInteractions(thresholdService);
    }



    @Test
    public void testUpdateThresholdByItemAndLocation() {
        String organizationId = "ORG001";
        String itemId = "ITEM001";
        String locationId = "LOCATION001";
        Threshold threshold = new Threshold("ORG001", "ITEM001", "LOCATION001", 10, 100);
        Threshold updatedThreshold = new Threshold("ORG001", "ITEM001", "LOCATION001", 5, 50);
        updatedThreshold.setId("THRESHOLD001");

        ThresholdService thresholdService = mock(ThresholdService.class);
        when(thresholdService.updateThresholdByItemIdAndLocationId(eq(organizationId), eq(itemId), eq(locationId), any(Threshold.class))).thenReturn(updatedThreshold);


        ResponseEntity<Threshold> response = thresholdController.updateThresholdByItemAndLocation(organizationId, itemId, locationId, threshold);
        Threshold threshold1 = thresholdService.updateThresholdByItemIdAndLocationId("ORG001","ITEM001","LOCATION001",threshold);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedThreshold, threshold1);

        verify(thresholdService, times(1)).updateThresholdByItemIdAndLocationId(eq(organizationId), eq(itemId), eq(locationId), any(Threshold.class));
        verifyNoMoreInteractions(thresholdService);
    }


}
