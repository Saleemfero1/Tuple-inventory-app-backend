package com.nextuple.Inventory.management.exception.test;

import com.nextuple.Inventory.management.exception.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class CustomizedExceptionHandlerTest {

    @Mock
    private OrganizationNotFoundException organizationNotFoundException;
    @Mock
    private SupplyAndDemandExistException supplyAndDemandExistException;
    @Mock
    private ItemNotFoundException itemNotFoundException;
    @Mock
    private DemandNotFoundException demandNotFoundException;
    @Mock
    private SupplyNotFoundException supplyNotFoundException;
    @Mock
    private ThresholdNotFoundException thresholdNotFoundException;
    @Mock
    private LocationNotFoundException locationNotFoundException;

    private CustomizedExceptionHandler exceptionHandler;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        exceptionHandler = new CustomizedExceptionHandler();
    }

    @Test
    public void handleOrganizationNotFoundException() throws Exception {
        String errorMessage = "Organization not found";
        when(organizationNotFoundException.getMessage()).thenReturn(errorMessage);
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), errorMessage);
        ResponseEntity<ErrorDetails> expectedResponse = new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);

        ResponseEntity<ErrorDetails> actualResponse = exceptionHandler.handleOrganizationNotFoundException(organizationNotFoundException);

        Assert.assertEquals(expectedResponse, expectedResponse);
    }

    @Test
    public void handleSupplyAndDemandNotFoundException() throws Exception {
        String errorMessage = "Supply and demand not found";
        when(supplyAndDemandExistException.getMessage()).thenReturn(errorMessage);
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), errorMessage);
        ResponseEntity<ErrorDetails> expectedResponse = new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);

        ResponseEntity<ErrorDetails> actualResponse = exceptionHandler.handleSupplyAndDemandNotFoundException(supplyAndDemandExistException);

        Assert.assertEquals(expectedResponse, expectedResponse);
    }

    @Test
    public void handleItemNotFoundException() throws Exception {
        String errorMessage = "Item not found";
        when(itemNotFoundException.getMessage()).thenReturn(errorMessage);
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), errorMessage);
        ResponseEntity<ErrorDetails> expectedResponse = new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);

        ResponseEntity<ErrorDetails> actualResponse = exceptionHandler.handleItemNotFoundException(itemNotFoundException);

        Assert.assertEquals(expectedResponse, expectedResponse);
    }

    @Test
    public void handleDemandNotFoundException() throws Exception {
        String errorMessage = "Demand not found";
        when(demandNotFoundException.getMessage()).thenReturn(errorMessage);
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), errorMessage);
        ResponseEntity<ErrorDetails> expectedResponse = new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);

        ResponseEntity<ErrorDetails> actualResponse = exceptionHandler.handleDemandNotFoundException(demandNotFoundException);

        Assert.assertEquals(expectedResponse, expectedResponse);
    }

    @Test
    public void handleSupplyNotFoundException() throws Exception {
        String errorMessage = "Supply not found";
        when(supplyNotFoundException.getMessage()).thenReturn(errorMessage);
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), errorMessage);
        ResponseEntity<ErrorDetails> expectedResponse = new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);

        ResponseEntity<ErrorDetails> actualResponse = exceptionHandler.handleSupplyNotFoundException(supplyNotFoundException);

        Assert.assertEquals(expectedResponse, expectedResponse);
    }

    @Test
    public void handleThresholdNotFoundException() throws Exception {
        String errorMessage = "Threshold not found";
        when(thresholdNotFoundException.getMessage()).thenReturn(errorMessage);
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), errorMessage);
        ResponseEntity<ErrorDetails> expectedResponse = new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);

        ResponseEntity<ErrorDetails> actualResponse = exceptionHandler.handleThresholdNotFoundException(thresholdNotFoundException);

        Assert.assertEquals(expectedResponse, expectedResponse);
    }

    @Test
    public void handleLocationNotFoundException() throws Exception {
        String errorMessage = "Location not found";
        when(locationNotFoundException.getMessage()).thenReturn(errorMessage);
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), errorMessage);
        ResponseEntity<ErrorDetails> expectedResponse = new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);

        ResponseEntity<ErrorDetails> actualResponse = exceptionHandler.handleLocationNotFoundException(locationNotFoundException);

        Assert.assertEquals(expectedResponse, expectedResponse);
    }
}
