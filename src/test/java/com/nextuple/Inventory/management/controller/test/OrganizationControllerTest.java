package com.nextuple.Inventory.management.controller.test;
import com.nextuple.Inventory.management.controller.OrganizationController;
import com.nextuple.Inventory.management.model.Organization;
        import com.nextuple.Inventory.management.service.OrganizationServices;
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
        import java.util.ArrayList;
        import java.util.List;

        import static org.junit.jupiter.api.Assertions.assertEquals;
        import static org.mockito.ArgumentMatchers.*;
        import static org.mockito.Mockito.*;

class OrganizationControllerTest {

    @Mock
    private OrganizationServices organizationServices;

    @InjectMocks
    private OrganizationController organizationController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        HttpServletRequest mockRequest = new MockHttpServletRequest();
        ServletRequestAttributes servletRequestAttributes = new ServletRequestAttributes(mockRequest);
        RequestContextHolder.setRequestAttributes(servletRequestAttributes);
    }

    @Test
    void testFindAllOrganization() {
        List<Organization> organizationList = new ArrayList<>();
        organizationList.add(new Organization());
        organizationList.add(new Organization());

        when(organizationServices.findAllOrganization()).thenReturn(organizationList);

        ResponseEntity<List<Organization>> response = organizationController.findAllOrganization();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(organizationList, response.getBody());

        verify(organizationServices, times(1)).findAllOrganization();
        verifyNoMoreInteractions(organizationServices);
    }

    @Test
    void testFindOrganizationById() {
        String organizationId = "ORG001";
        Organization organization = new Organization();

        when(organizationServices.findOrganizationById(organizationId)).thenReturn(organization);

        ResponseEntity<Organization> response = organizationController.findOrganizationById(organizationId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(organization, response.getBody());

        verify(organizationServices, times(1)).findOrganizationById(organizationId);
        verifyNoMoreInteractions(organizationServices);
    }

    @Test
    void testFindOrganizationByEmail() {
        String organizationEmail = "test@example.com";
        Organization organization = new Organization();

        when(organizationServices.findByEmailId(organizationEmail)).thenReturn(organization);

        ResponseEntity<Organization> response = organizationController.findOrganizationByEmail(organizationEmail);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(organization, response.getBody());

        verify(organizationServices, times(1)).findByEmailId(organizationEmail);
        verifyNoMoreInteractions(organizationServices);
    }

   /* @Test
    void testCreateOrganization() {
        Organization organization = new Organization("ORG001","ORG","ORG@GMAIL.COM","123");

        when(organizationServices.createOrganization(organization)).thenReturn(organization);

        ResponseEntity<Organization> response = organizationController.createOrganization(organization);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(organization, response.getBody());

        URI expectedLocation = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{organizationId}")
                .buildAndExpand(organization.getOrganizationId())
                .toUri();
        assertEquals(expectedLocation, response.getHeaders().getLocation());

        verify(organizationServices, times(1)).createOrganization(organization);
        verifyNoMoreInteractions(organizationServices);
    }*/


    @Test
    void testOrganizationDelete() {
        String organizationId = "ORG001";

        when(organizationServices.deleteOrganization(organizationId)).thenReturn("Organization deleted successfully");

        ResponseEntity<String> response = organizationController.organizationDelete(organizationId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals("Organization deleted successfully", response.getBody());

        verify(organizationServices, times(1)).deleteOrganization(organizationId);
        verifyNoMoreInteractions(organizationServices);
    }

    @Test
    void testUpdateOrganization() {
        String organizationId = "ORG001";
        Organization organization = new Organization();

        when(organizationServices.updateOrganization(organization, organizationId)).thenReturn(organization);

        ResponseEntity<Organization> response = organizationController.updateOrganization(organization, organizationId);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals(organization, response.getBody());

        verify(organizationServices, times(1)).updateOrganization(organization, organizationId);
        verifyNoMoreInteractions(organizationServices);
    }
}

