package com.nextuple.Inventory.management.service.test;

        import com.nextuple.Inventory.management.exception.LocationNotFoundException;
        import com.nextuple.Inventory.management.exception.OrganizationNotFoundException;
        import com.nextuple.Inventory.management.exception.SupplyAndDemandExistException;
        import com.nextuple.Inventory.management.model.Demand;
        import com.nextuple.Inventory.management.model.Location;
        import com.nextuple.Inventory.management.model.Organization;
        import com.nextuple.Inventory.management.model.Supply;
        import com.nextuple.Inventory.management.repository.*;
        import com.nextuple.Inventory.management.service.LocationService;
        import org.junit.jupiter.api.BeforeEach;
        import org.junit.jupiter.api.Test;
        import org.mockito.Mock;
        import org.mockito.MockitoAnnotations;

        import java.util.ArrayList;
        import java.util.List;
        import java.util.Optional;

        import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.ArgumentMatchers.*;
        import static org.mockito.Mockito.*;

class LocationServicesTest {

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

    private LocationService locationService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        locationService = new LocationService(itemRepository, locationRepository, supplyRepository,
                demandRepository, thresholdRepository, organizationRepository);
    }

    @Test
    public void testLocationDetails_WithExistingLocations_ShouldReturnLocationList() {
        // Arrange
        List<Location> locationList = new ArrayList<>();
        locationList.add( new Location("111", "locationDesc", "locationType", true,true,true,"addressLine1", "addressLine2","addressLine3","city","state","country","pincode","ORG001"));
        locationList.add( new Location("222", "locationDesc", "locationType", true,true,true,"addressLine1", "addressLine2","addressLine3","city","state","country","pincode","ORG001"));

        when(locationRepository.findAllByOrganizationId(anyString())).thenReturn(locationList);

        // Act
        List<Location> result = locationService.locationDetails("ORG001");

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(locationRepository, times(1)).findAllByOrganizationId(anyString());
    }

    @Test
    public void testLocationDetails_WithNoLocations_ShouldThrowLocationNotFoundException() {
        // Arrange
        when(locationRepository.findAllByOrganizationId(anyString())).thenReturn(new ArrayList<>());

        // Act & Assert
        assertThrows(LocationNotFoundException.class, () -> locationService.locationDetails("ORG001"));
        verify(locationRepository, times(1)).findAllByOrganizationId(anyString());
    }

    @Test
    public void testFindLocation_WithExistingLocation_ShouldReturnLocation() {
        // Arrange
        Location location = new Location("111", "locationDesc", "locationType", true,true,true,"addressLine1",
                "addressLine2","addressLine3","city","state","country","pinCode","ORG001");
        when(locationRepository.findByLocationIdAndOrganizationId(anyString(), anyString())).thenReturn(Optional.of(location));

        // Act
        Location result = locationService.findLocation("ORG001", "111");

        // Assert
        assertNotNull(result);
        assertEquals("locationDesc", result.getLocationDesc());
        verify(locationRepository, times(1)).findByLocationIdAndOrganizationId(anyString(), anyString());
    }

    @Test
    public void testFindLocation_WithNonExistingLocation_ShouldThrowLocationNotFoundException() {
        // Arrange
        when(locationRepository.findByLocationIdAndOrganizationId(anyString(), anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(LocationNotFoundException.class, () -> locationService.findLocation("ORG001", "111"));
        verify(locationRepository, times(1)).findByLocationIdAndOrganizationId(anyString(), anyString());
    }

    @Test
    public void testCreateLocation_WithValidOrganizationAndNonExistingLocation_ShouldCreateLocation() {
        // Arrange
        Location location = new Location("111", "locationDesc", "locationType", true,true,true,"addressLine1",
                "addressLine2","addressLine3","city","state","country","pinCode","ORG001");
        Organization organization = new Organization("ORG001", "TUPLE","Tuple@gmail.com", "tuple@123");
        when(locationRepository.findByLocationIdAndOrganizationId(anyString(), anyString())).thenReturn(Optional.empty());
        when(locationRepository.save(any(Location.class))).thenReturn(location);

        // Act
        Location result = locationService.createLocation("ORG001", location);

        // Assert
        assertNotNull(result);
        assertEquals("NXT111", result.getLocationId());
        assertEquals("locationDesc", result.getLocationDesc());
        assertEquals("locationType", result.getLocationType());
        assertEquals("ORG001", result.getOrganizationId());
        verify(locationRepository, times(1)).findByLocationIdAndOrganizationId(anyString(), anyString());
        verify(locationRepository, times(1)).save(any(Location.class));
    }

    @Test
    public void testCreateLocation_WithExistingLocation_ShouldThrowLocationNotFoundException() {
        // Arrange
        Location location = new Location("111", "locationDesc", "locationType", true,true,true,"addressLine1",
                "addressLine2","addressLine3","city","state","country","pinCode","ORG001");
        Organization organization = new Organization("ORG001", "TUPLE","Tuple@gmail.com", "tuple@123");
        when(locationRepository.findByLocationIdAndOrganizationId(anyString(), anyString())).thenReturn(Optional.of(location));

        // Act & Assert
        assertThrows(LocationNotFoundException.class, () -> locationService.createLocation("ORG001", location));
        verify(locationRepository, times(1)).findByLocationIdAndOrganizationId(anyString(), anyString());
        verify(locationRepository, never()).save(any(Location.class));
    }

    @Test
    public void testDeleteLocationIfNotPresentInReferenceCollection_WithExistingLocationAndNoReferences_ShouldDeleteLocation() {
        // Arrange
        Location location = new Location("111", "locationDesc", "locationType", true,true,true,"addressLine1",
                "addressLine2","addressLine3","city","state","country","pinCode","ORG001");
        when(locationRepository.findByLocationIdAndOrganizationId(anyString(), anyString())).thenReturn(Optional.of(location));
        when(supplyRepository.findByLocationIdAndOrganizationId(anyString(), anyString())).thenReturn(new ArrayList<>());
        when(demandRepository.findByLocationIdAndOrganizationId(anyString(), anyString())).thenReturn(new ArrayList<>());

        // Act
        String result = locationService.deleteLocationIfNotPresentInReferenceCollection("111", "ORG001");

        // Assert
        assertEquals("Location Deleted!", result);
        verify(locationRepository, times(1)).findByLocationIdAndOrganizationId(anyString(), anyString());
        verify(supplyRepository, times(1)).findByLocationIdAndOrganizationId(anyString(), anyString());
        verify(demandRepository, times(1)).findByLocationIdAndOrganizationId(anyString(), anyString());
        verify(locationRepository, times(1)).delete(any(Location.class));
    }

    @Test
    public void testDeleteLocationIfNotPresentInReferenceCollection_WithExistingLocationAndReferences_ShouldThrowSupplyAndDemandExistException() {
        // Arrange
        Location location = new Location("111", "locationDesc", "locationType", true,true,true,"addressLine1",
                "addressLine2","addressLine3","city","state","country","pinCode","ORG001");
        List<Supply> supplyList = new ArrayList<>();
        supplyList.add(new Supply("ORG001","ORG001_00001","111","ONHAND",7));
        List<Demand> demandList = new ArrayList<>();
        demandList.add(new Demand("ORG001","ONHAND",111,"ORG001_00001","111"));
        when(locationRepository.findByLocationIdAndOrganizationId(anyString(), anyString())).thenReturn(Optional.of(location));
        when(supplyRepository.findByLocationIdAndOrganizationId(anyString(), anyString())).thenReturn(supplyList);
        when(demandRepository.findByLocationIdAndOrganizationId(anyString(), anyString())).thenReturn(demandList);

        // Act & Assert
        assertThrows(SupplyAndDemandExistException.class, () -> locationService.deleteLocationIfNotPresentInReferenceCollection("111", "ORG001"));
        verify(locationRepository, times(1)).findByLocationIdAndOrganizationId(anyString(), anyString());
        verify(supplyRepository, times(1)).findByLocationIdAndOrganizationId(anyString(), anyString());
        verify(demandRepository, times(1)).findByLocationIdAndOrganizationId(anyString(), anyString());
        verify(locationRepository, never()).delete(any(Location.class));
    }

    // Act & Assert
    @Test
    public void testDeleteLocationIfNotPresentInReferenceCollection_WithNonExistingLocation_ShouldThrowLocationNotFoundException() {
        // Arrange
        when(locationRepository.findByLocationIdAndOrganizationId(anyString(), anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(LocationNotFoundException.class, () -> locationService.deleteLocationIfNotPresentInReferenceCollection("111", "ORG001"));
        verify(locationRepository, times(1)).findByLocationIdAndOrganizationId(anyString(), anyString());
        verify(supplyRepository, never()).findByLocationIdAndOrganizationId(anyString(), anyString());
        verify(demandRepository, never()).findByLocationIdAndOrganizationId(anyString(), anyString());
        verify(locationRepository, never()).delete(any(Location.class));
    }

    @Test
    public void testUpdateLocation_WithExistingLocation_ShouldUpdateLocation() {
        // Arrange
        Location location = new Location("111", "locationDesc", "locationType", true,true,true,"addressLine1",
                "addressLine2","addressLine3","city","state","country","pinCode","ORG001");
        Location updatedLocation = new Location("111", "UpdatedLocationDesc", "Dc", true,true,true,"addressLine1",
                "addressLine2","addressLine3","city","state","country","pinCode","ORG001");
        when(locationRepository.findByLocationIdAndOrganizationId(anyString(), anyString())).thenReturn(Optional.of(location));
        when(locationRepository.save(any(Location.class))).thenReturn(updatedLocation);

        // Act
        Optional<Location> result = locationService.updateLocation(updatedLocation, "111", "ORG001");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("UpdatedLocationDesc", result.get().getLocationDesc());
        assertEquals("Dc", result.get().getLocationType());
        verify(locationRepository, times(1)).findByLocationIdAndOrganizationId(anyString(), anyString());
        verify(locationRepository, times(1)).save(any(Location.class));
    }

    @Test
    public void testUpdateLocation_WithNonExistingLocation_ShouldThrowLocationNotFoundException() {
        // Arrange
        when(locationRepository.findByLocationIdAndOrganizationId(anyString(), anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(LocationNotFoundException.class, () -> locationService.updateLocation(new Location(), "location1", "org1"));
        verify(locationRepository, times(1)).findByLocationIdAndOrganizationId(anyString(), anyString());
        verify(locationRepository, never()).save(any(Location.class));
    }
}
