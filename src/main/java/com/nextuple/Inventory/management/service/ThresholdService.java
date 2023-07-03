package com.nextuple.Inventory.management.service;

import com.nextuple.Inventory.management.exception.ItemNotFoundException;
import com.nextuple.Inventory.management.exception.OrganizationNotFoundException;
import com.nextuple.Inventory.management.exception.SupplyAndDemandExistException;
import com.nextuple.Inventory.management.exception.ThresholdNotFoundException;
import com.nextuple.Inventory.management.model.Demand;
import com.nextuple.Inventory.management.model.Organization;
import com.nextuple.Inventory.management.model.Supply;
import com.nextuple.Inventory.management.model.Threshold;
import com.nextuple.Inventory.management.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ThresholdService {
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

    public ThresholdService(ItemRepository itemRepository, LocationRepository locationRepository, SupplyRepository supplyRepository, DemandRepository demandRepository, ThresholdRepository thresholdRepository, OrganizationRepository organizationRepository) {
        ThresholdService.itemRepository = itemRepository;
        ThresholdService.locationRepository = locationRepository;
        ThresholdService.supplyRepository = supplyRepository;
        ThresholdService.demandRepository = demandRepository;
        ThresholdService.thresholdRepository = thresholdRepository;
        ThresholdService.organizationRepository = organizationRepository;
    }

    ////////////////////////////////////////////-->Threshold Services<--//////////////////////////////////////////////////////////////////
    public List<Threshold> thresholdDetails(String organizationId) {
        List<Threshold> thresholdList = thresholdRepository.findAllByOrganizationId(organizationId);
        if (thresholdList.isEmpty())
            throw new ThresholdNotFoundException("Thresholds Are Not Found!");
        return thresholdList;
    }

    public Threshold findThresholdById(String organizationId, String thresholdId) {
        Optional<Threshold> threshold = thresholdRepository.findByIdAndOrganizationId(thresholdId, organizationId);
        if (threshold.isEmpty())
            throw new ThresholdNotFoundException("Threshold Not found!");
        return threshold.get();
    }


    public Threshold findThresholdByItemIdAtLocation(String organizationId, String itemId, String locationId) {
        Optional<Threshold> threshold = thresholdRepository.findByItemIdAndLocationIdAndOrganizationId(itemId, locationId, organizationId);
        if (threshold.isEmpty())
            throw new ThresholdNotFoundException("threshold not found!");
        return threshold.get();
    }

    public Threshold createThreshold(String organizationId, Threshold threshold) {
        Optional<Threshold> thresholdExist = thresholdRepository.findByItemIdAndLocationIdAndOrganizationId(threshold.getItemId(), threshold.getLocationId(), organizationId);
        Optional<Organization> organizationExist = organizationRepository.findById(organizationId);
        threshold.setOrganizationId(organizationId);
        if (thresholdExist.isPresent())
            throw new ThresholdNotFoundException("Threshold For Item:" + threshold.getItemId() + " & Location:" + threshold.getLocationId() + " Is Exist!");
        else
            return thresholdRepository.save(threshold);
    }

    public void deleteThreshold(String organizationId, String thresholdId) {
        Optional<Threshold> threshold = thresholdRepository.findByIdAndOrganizationId(thresholdId, organizationId);
        if (thresholdId.isEmpty())
            throw new ThresholdNotFoundException("Threshold Not Found!");

        List<Supply> supplyReference = supplyRepository.findByItemIdAndOrganizationId(threshold.get().getItemId(),organizationId);
        List<Demand> demandReference = demandRepository.findByItemIdAndOrganizationId(threshold.get().getItemId(),organizationId);
        if (supplyReference.isEmpty() && demandReference.isEmpty()) {
            thresholdRepository.deleteById(thresholdId);
        } else {
            throw new SupplyAndDemandExistException("Threshold:"+thresholdId+" exist with supply or demand");
        }
    }



    public Threshold updateThreshold(String organizationId, String thresholdId, Threshold threshold) {
        return thresholdRepository.findByIdAndOrganizationId(thresholdId, organizationId).map(
                newThreshold -> {
                    newThreshold.setMinThreshold(threshold.getMinThreshold());
                    newThreshold.setMaxThreshold(threshold.getMaxThreshold());
                    return thresholdRepository.save(newThreshold);
                }).orElseThrow(() -> new ThresholdNotFoundException("Threshold Not Found!"));
    }

    public Threshold updateThresholdByItemIdAndLocationId(String organizationId, String itemId, String locationId, Threshold threshold) {
        return thresholdRepository.findByItemIdAndLocationIdAndOrganizationId(itemId, locationId, organizationId).map(
                newThreshold -> {
                    newThreshold.setMinThreshold(threshold.getMinThreshold());
                    newThreshold.setMaxThreshold(threshold.getMaxThreshold());
                    return thresholdRepository.save(newThreshold);
                }).orElseThrow(() -> new ThresholdNotFoundException("Threshold Not Found!"));
    }

    public Page<Threshold> pageableThresholdDetails(String organizationId, Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Threshold> thresholdList = thresholdRepository.findByOrganizationId(organizationId, pageable);
        if (thresholdList.isEmpty())
            throw new ItemNotFoundException("There is no supply exist in the Inventory!");
        return thresholdList;
    }

}
