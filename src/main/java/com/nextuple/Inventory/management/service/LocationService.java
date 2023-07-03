package com.nextuple.Inventory.management.service;

import com.nextuple.Inventory.management.exception.ItemNotFoundException;
import com.nextuple.Inventory.management.exception.LocationNotFoundException;
import com.nextuple.Inventory.management.exception.OrganizationNotFoundException;
import com.nextuple.Inventory.management.exception.SupplyAndDemandExistException;
import com.nextuple.Inventory.management.model.*;
import com.nextuple.Inventory.management.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationService {
    @Autowired
    private static ItemRepository itemRepository;
    @Autowired
    private static LocationRepository locationRepository;
    @Autowired
    private static SupplyRepository supplyRepository;
    @Autowired
    private static DemandRepository demandRepository;
    @Autowired
    private static ThresholdRepository thresholdRepository;
    @Autowired
    private static OrganizationRepository organizationRepository;

    public LocationService(ItemRepository itemRepository, LocationRepository locationRepository, SupplyRepository supplyRepository, DemandRepository demandRepository, ThresholdRepository thresholdRepository, OrganizationRepository organizationRepository){
        LocationService.itemRepository = itemRepository;
        LocationService.locationRepository = locationRepository;
        LocationService.supplyRepository = supplyRepository;
        LocationService.demandRepository = demandRepository;
        LocationService.thresholdRepository = thresholdRepository;
        LocationService.organizationRepository = organizationRepository;
    }

    //////////////////////////////////////////////////////-->Location API<--//////////////////////////////////////////////////////
    public List<Location>locationDetails(String organizationId) {
        List<Location>locationList = locationRepository.findAllByOrganizationId(organizationId);
        if(locationList.isEmpty())
            throw new LocationNotFoundException("There is no locations exist in the Inventory");
        return locationList;
    }

    public Location findLocation(String organizationId,String locationId) {
        Optional<Location> foundLocation = locationRepository.findByLocationIdAndOrganizationId(locationId,organizationId);
        if(foundLocation.isEmpty())
            throw new LocationNotFoundException("Location:"+locationId+" Not Found!");
        return foundLocation.get();
    }

    public Location createLocation(String organizationId, Location location) {
            location.setLocationId("NXT"+location.getLocationId());
            location.setOrganizationId(organizationId);
            Optional<Location> foundLocation = locationRepository.findByLocationIdAndOrganizationId(location.getLocationId(), organizationId);
            if (foundLocation.isEmpty()) {
                locationRepository.save(location);
                return location;
            } else {
                throw new LocationNotFoundException("Location:" + location.getLocationId() + "Already Exist");
            }
    }

    public String deleteLocationIfNotPresentInReferenceCollection(String locationId,String organizationId) {
        Optional<Location> locationOptional = locationRepository.findByLocationIdAndOrganizationId(locationId,organizationId);
        if (locationOptional.isPresent()) {
            Location location = locationOptional.get();
            List<Supply> supplyReference = supplyRepository.findByLocationIdAndOrganizationId(location.getLocationId(),organizationId);
            List<Demand> demandReference = demandRepository.findByLocationIdAndOrganizationId(location.getLocationId(),organizationId);
            if (supplyReference.isEmpty() && demandReference.isEmpty()) {
                locationRepository.delete(location);
                return "Location Deleted!";
            } else {
                throw new SupplyAndDemandExistException("Location:"+locationId+" exist with supply or demand");
            }
        } else {
            throw new LocationNotFoundException("Location:"+locationId+" not found!");
        }
    }

    public Optional<Location> updateLocation(Location location, String locationId,String organizationId) {
        return Optional.ofNullable(locationRepository.findByLocationIdAndOrganizationId(locationId,organizationId).map(
                newLocation -> {
                    newLocation.setLocationDesc(location.getLocationDesc());
                    newLocation.setLocationType(location.getLocationType());
                    newLocation.setPickupAllowed(location.isPickupAllowed());
                    newLocation.setDeliveryAllowed(location.isDeliveryAllowed());
                    newLocation.setShippingAllowed(location.isShippingAllowed());
                    newLocation.setAddressLine1(location.getAddressLine1());
                    newLocation.setAddressLine2(location.getAddressLine2());
                    newLocation.setAddressLine3(location.getAddressLine3());
                    newLocation.setCity(location.getCity());
                    newLocation.setState(location.getState());
                    newLocation.setCountry(location.getCountry());
                    newLocation.setPinCode(location.getPinCode());
                    return locationRepository.save(newLocation);
                }
        ).orElseThrow(() -> new LocationNotFoundException("Location :" + locationId + " Not Found")));
    }

    public Page<Location> pageableLocationDetails(String organizationId, Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Location> locationList = locationRepository.findByOrganizationId(organizationId,pageable);
        if(locationList.isEmpty())
            throw new ItemNotFoundException("There is no supply  exist in the Inventory!");
        return locationList;
    }
}
