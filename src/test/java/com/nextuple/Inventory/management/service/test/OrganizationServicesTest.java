package com.nextuple.Inventory.management.service.test;
import com.nextuple.Inventory.management.exception.OrganizationNotFoundException;
import com.nextuple.Inventory.management.model.Item;
import com.nextuple.Inventory.management.model.Organization;
import com.nextuple.Inventory.management.repository.*;
import com.nextuple.Inventory.management.service.OrganizationServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrganizationServicesTest {

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
    private OrganizationServices organizationServices;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOrganization_WithValidOrganization_ShouldCreateOrganization() {
        // Arrange
        Organization organization = new Organization("org1", "Organization 1", "email@example.com", "password");
        when(organizationRepository.findById(anyString())).thenReturn(Optional.empty());
        when(organizationRepository.existsByOrganizationName(anyString())).thenReturn(false);
        when(organizationRepository.save(any(Organization.class))).thenReturn(organization);

        // Act
        Organization result = organizationServices.createOrganization(organization);

        // Assert
        assertNotNull(result);
        assertEquals("org1", result.getOrganizationId());
        assertEquals("Organization 1", result.getOrganizationName());
        assertEquals("email@example.com", result.getOrganizationEmail());
        assertEquals("password", result.getPassword());
        verify(organizationRepository, times(1)).existsByOrganizationName(anyString());
        verify(organizationRepository, times(1)).save(any(Organization.class));
    }

    @Test
    void testCreateOrganization_WithNonExistingOrganizationId_ShouldCreateOrganization() {
        // Arrange
        Organization organization = new Organization("ORG001", "Organization 1", "email@example.com", "password");
        when(organizationRepository.existsByOrganizationName(anyString())).thenReturn(false);
        when(organizationRepository.save(any(Organization.class))).thenReturn(organization);

        // Act
        Organization result = organizationServices.createOrganization(organization);

        // Assert
        assertNotNull(result);
        assertEquals("ORG001", result.getOrganizationId());
        assertEquals("Organization 1", result.getOrganizationName());
        assertEquals("email@example.com", result.getOrganizationEmail());
        assertEquals("password", result.getPassword());
        verify(organizationRepository, times(1)).existsByOrganizationName(anyString());
        verify(organizationRepository, times(1)).save(any(Organization.class));
    }


    @Test
    void testCreateOrganization_WithExistingOrganizationName_ShouldThrowRuntimeException() {
        // Arrange
        Organization organization = new Organization("org1", "Organization 1", "email@example.com", "password");
        when(organizationRepository.existsByOrganizationName(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> organizationServices.createOrganization(organization));
        verify(organizationRepository, times(1)).existsByOrganizationName(anyString());
        verify(organizationRepository, never()).save(any(Organization.class));
    }

    @Test
    void testFindOrganizationById_WithExistingOrganizationId_ShouldReturnOrganization() {
        // Arrange
        Organization organization = new Organization("org1", "Organization 1", "email@example.com", "password");
        when(organizationRepository.findById(anyString())).thenReturn(Optional.of(organization));

        // Act
        Organization result = organizationServices.findOrganizationById("org1");

        // Assert
        assertNotNull(result);
        assertEquals("org1", result.getOrganizationId());
        assertEquals("Organization 1", result.getOrganizationName());
        assertEquals("email@example.com", result.getOrganizationEmail());
        assertEquals("password", result.getPassword());
        verify(organizationRepository, times(1)).findById(anyString());
    }

    @Test
    void testFindOrganizationById_WithNonExistingOrganizationId_ShouldThrowOrganizationNotFoundException() {
        // Arrange
        when(organizationRepository.findById(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(OrganizationNotFoundException.class, () -> organizationServices.findOrganizationById("org1"));
        verify(organizationRepository, times(1)).findById(anyString());
    }

    @Test
    void testFindByEmailId_WithExistingEmailId_ShouldReturnOrganization() {
        // Arrange
        Organization organization = new Organization("org1", "Organization 1", "email@example.com", "password");
        when(organizationRepository.findByOrganizationEmail(anyString())).thenReturn(Optional.of(organization));

        // Act
        Organization result = organizationServices.findByEmailId("email@example.com");

        // Assert
        assertNotNull(result);
        assertEquals("org1", result.getOrganizationId());
        assertEquals("Organization 1", result.getOrganizationName());
        assertEquals("email@example.com", result.getOrganizationEmail());
        assertEquals("password", result.getPassword());
        verify(organizationRepository, times(1)).findByOrganizationEmail(anyString());
    }

    @Test
    void testFindByEmailId_WithNonExistingEmailId_ShouldThrowOrganizationNotFoundException() {
        // Arrange
        when(organizationRepository.findByOrganizationEmail(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(OrganizationNotFoundException.class, () -> organizationServices.findByEmailId("email@example.com"));
        verify(organizationRepository, times(1)).findByOrganizationEmail(anyString());
    }

    @Test
    void testFindAllOrganization_WithExistingOrganizations_ShouldReturnOrganizationList() {
        // Arrange
        List<Organization> organizationList = new ArrayList<>();
        organizationList.add(new Organization("org1", "Organization 1", "email1@example.com", "password1"));
        organizationList.add(new Organization("org2", "Organization 2", "email2@example.com", "password2"));
        when(organizationRepository.findAll()).thenReturn(organizationList);

        // Act
        List<Organization> result = organizationServices.findAllOrganization();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("org1", result.get(0).getOrganizationId());
        assertEquals("Organization 1", result.get(0).getOrganizationName());
        assertEquals("email1@example.com", result.get(0).getOrganizationEmail());
        assertEquals("password1", result.get(0).getPassword());
        assertEquals("org2", result.get(1).getOrganizationId());
        assertEquals("Organization 2", result.get(1).getOrganizationName());
        assertEquals("email2@example.com", result.get(1).getOrganizationEmail());
        assertEquals("password2", result.get(1).getPassword());
        verify(organizationRepository, times(1)).findAll();
    }

    @Test
    void testFindAllOrganization_WithNoOrganizations_ShouldThrowOrganizationNotFoundException() {
        // Arrange
        when(organizationRepository.findAll()).thenReturn(new ArrayList<>());

        // Act & Assert
        assertThrows(OrganizationNotFoundException.class, () -> organizationServices.findAllOrganization());
        verify(organizationRepository, times(1)).findAll();
    }

    @Test
    void testDeleteOrganization_WithExistingOrganizationIdAndNoItems_ShouldDeleteOrganization() {
        // Arrange
        when(organizationRepository.findById(anyString())).thenReturn(Optional.of(new Organization("org1", "Organization 1", "email@example.com", "password")));
        when(itemRepository.findByOrganizationId(anyString())).thenReturn(new ArrayList<>());

        // Act
        String result = organizationServices.deleteOrganization("org1");

        // Assert
        assertEquals("Organization Deleted!", result);
        verify(organizationRepository, times(1)).findById(anyString());
        verify(itemRepository, times(1)).findByOrganizationId(anyString());
        verify(organizationRepository, times(1)).deleteById(anyString());
    }

    @Test
    void testDeleteOrganization_WithExistingOrganizationIdAndItems_ShouldThrowOrganizationNotFoundException() {
        // Arrange
        List<Item> items = new ArrayList<>();
        items.add(new Item("00001", "itemNameOne", "itemOneDesc", "ItemOneCategory", "itemOneType", true, 2000, true, true, true,"ORG001"));

        when(organizationRepository.findById(anyString())).thenReturn(Optional.of(new Organization("org1", "Organization 1", "email@example.com", "password")));
        when(itemRepository.findByOrganizationId(anyString())).thenReturn(items);

        // Act & Assert
        assertThrows(OrganizationNotFoundException.class, () -> organizationServices.deleteOrganization("org1"));
        verify(organizationRepository, times(1)).findById(anyString());
        verify(itemRepository, times(1)).findByOrganizationId(anyString());
        verify(organizationRepository, never()).deleteById(anyString());
    }

    @Test
    void testDeleteOrganization_WithNonExistingOrganizationId_ShouldThrowOrganizationNotFoundException() {
        // Arrange
        when(organizationRepository.findById(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(OrganizationNotFoundException.class, () -> organizationServices.deleteOrganization("org1"));
        verify(organizationRepository, times(1)).findById(anyString());
        verify(itemRepository, never()).findByOrganizationId(anyString());
        verify(organizationRepository, never()).deleteById(anyString());
    }

    @Test
    void testUpdateOrganization_WithExistingOrganizationId_ShouldUpdateOrganization() {
        // Arrange
        Organization organization = new Organization("org1", "Organization 1", "email@example.com", "password");
        when(organizationRepository.findById(anyString())).thenReturn(Optional.of(organization));
        when(organizationRepository.save(any(Organization.class))).thenReturn(organization);

        // Act
        Organization result = organizationServices.updateOrganization(organization, "org1");

        // Assert
        assertNotNull(result);
        assertEquals("org1", result.getOrganizationId());
        assertEquals("Organization 1", result.getOrganizationName());
        assertEquals("email@example.com", result.getOrganizationEmail());
        assertEquals("password", result.getPassword());
        verify(organizationRepository, times(1)).findById(anyString());
        verify(organizationRepository, times(1)).save(any(Organization.class));
    }

    @Test
    void testUpdateOrganization_WithNonExistingOrganizationId_ShouldThrowOrganizationNotFoundException() {
        // Arrange
        Organization organization = new Organization("org1", "Organization 1", "email@example.com", "password");
        when(organizationRepository.findById(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(OrganizationNotFoundException.class, () -> organizationServices.updateOrganization(organization, "org1"));
        verify(organizationRepository, times(1)).findById(anyString());
        verify(organizationRepository, never()).save(any(Organization.class));
    }
}
