package com.nextuple.Inventory.management.controller.test;


import com.nextuple.Inventory.management.controller.LocationController;
import com.nextuple.Inventory.management.model.Location;
import com.nextuple.Inventory.management.service.LocationService;
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
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class LocationControllerTest {

    @Mock
    private LocationService locationService;

    @InjectMocks
    private LocationController locationController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        HttpServletRequest mockRequest = new MockHttpServletRequest();
        ServletRequestAttributes servletRequestAttributes = new ServletRequestAttributes(mockRequest);
        RequestContextHolder.setRequestAttributes(servletRequestAttributes);
    }

    @Test
    void testLocationDetails() {
        String userId = "USER001";
        List<Location> locationList = new ArrayList<>();
        locationList.add(new Location());
        locationList.add(new Location());

        when(locationService.locationDetails(userId)).thenReturn(locationList);

        ResponseEntity<List<Location>> response = locationController.locationDetails(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(locationList, response.getBody());

        verify(locationService, times(1)).locationDetails(userId);
        verifyNoMoreInteractions(locationService);
    }

    @Test
    void testFindLocation() {
        String userId = "USER001";
        String locationId = "LOC001";
        Location location = new Location();

        when(locationService.findLocation(userId, locationId)).thenReturn(location);

        ResponseEntity<Location> response = locationController.findLocation(userId, locationId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(location, response.getBody());

        verify(locationService, times(1)).findLocation(userId, locationId);
        verifyNoMoreInteractions(locationService);
    }

   @Test
    void testCreateLocation() throws URISyntaxException {
        String userId = "USER001";
        Location location = new Location();
        location.setLocationId("LOC001");

        when(locationService.createLocation(userId, location)).thenReturn(location);

        ResponseEntity<Location> response = locationController.createLocation(userId, location);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(location, response.getBody());

        URI expectedLocation = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(location.getLocationId())
                .toUri();
        assertEquals(expectedLocation, response.getHeaders().getLocation());

        verify(locationService, times(1)).createLocation(userId, location);
        verifyNoMoreInteractions(locationService);
    }

    @Test
    void testDeleteLocation() {
        String userId = "USER001";
        String locationId = "LOC001";

        when(locationService.deleteLocationIfNotPresentInReferenceCollection(locationId, userId)).thenReturn("Location deleted successfully");

        ResponseEntity<String> response = locationController.deleteLocation(userId, locationId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals("Location deleted successfully", response.getBody());

        verify(locationService, times(1)).deleteLocationIfNotPresentInReferenceCollection(locationId, userId);
        verifyNoMoreInteractions(locationService);
    }

    @Test
    void testUpdateLocation() {
        String userId = "USER001";
        String locationId = "LOC001";
        Location location = new Location();

        when(locationService.updateLocation(location, locationId, userId)).thenReturn(Optional.of(location));

        ResponseEntity<Location> response = locationController.updateLocation(location, locationId, userId);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(location, response.getBody());

        verify(locationService, times(1)).updateLocation(location, locationId, userId);
        verifyNoMoreInteractions(locationService);
    }

}
