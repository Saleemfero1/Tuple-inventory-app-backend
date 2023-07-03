package com.nextuple.Inventory.management.service;

import com.nextuple.Inventory.management.exception.*;
import com.nextuple.Inventory.management.model.*;
import com.nextuple.Inventory.management.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

@Service
public class SupplyServices {
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

    @Autowired
    private  static TransactionRepository transactionRepository;

    @Autowired
    private DemandService demandService;

    public SupplyServices(ItemRepository itemRepository, LocationRepository locationRepository, SupplyRepository supplyRepository, DemandRepository demandRepository, ThresholdRepository thresholdRepository, OrganizationRepository organizationRepository,TransactionRepository transactionRepository){
        SupplyServices.itemRepository = itemRepository;
        SupplyServices.locationRepository = locationRepository;
        SupplyServices.supplyRepository = supplyRepository;
        SupplyServices.demandRepository = demandRepository;
        SupplyServices.thresholdRepository = thresholdRepository;
        SupplyServices.organizationRepository = organizationRepository;
        SupplyServices.transactionRepository = transactionRepository;
    }

    //date time formatter
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

///////////////////////////////////////////////////////-->Supply Services<--///////////////////////////////////////////////////////////

    public List<Supply> supplyDetails(String organizationId) {
        List<Supply>supplyList = supplyRepository.findAllByOrganizationId(organizationId);
        if(supplyList.isEmpty())
            throw new SupplyAndDemandExistException("Supply Not Found!");
        else
            return supplyList;
    }

    public Supply findSupply(String organizationId,String supplyId) {
            Optional<Supply> foundSupply = supplyRepository.findByIdAndOrganizationId(supplyId,organizationId);
            if(foundSupply.isEmpty())
                throw new SupplyAndDemandExistException("Supply:"+supplyId+" Not Found!");
            else
                return foundSupply.get();

    }
    public List<Supply> findSupplyForItem(String organizationId,String itemId, String locationId) {
            List<Supply> supplyList = supplyRepository.findByItemIdAndLocationIdAndOrganizationId(itemId,locationId,organizationId);
            if(supplyList.isEmpty())
                throw new SupplyAndDemandExistException("Supply Not Found For Item:"+itemId);
            return supplyList;
    }

    public List<Supply> findSupplyWithSupplyTypeForSpecificLocation(String organizationId,String supplyType, String locationId) {
            List<Supply> supplyList = supplyRepository.findBySupplyTypeAndLocationIdAndOrganizationId(supplyType,locationId,organizationId);
            if (supplyList.isEmpty())
                throw new SupplyAndDemandExistException("Supplies Not Found!");
            return supplyList;
    }

    @Transactional
    public Supply createSupply(String organizationId,Supply supply) {

        Optional<Threshold> existThreshold = thresholdRepository.findByItemIdAndLocationIdAndOrganizationId(supply.getItemId(),supply.getLocationId(),organizationId);
        if(existThreshold.isEmpty())
            throw new ThresholdNotFoundException("Threshold Not Found!");

        int totalSupply = totalSupplyForItemAtParticularLocation(organizationId,supply.getItemId(),supply.getLocationId());
        int totalDemand = demandService.totalDemandForItemAtParticularLocation(organizationId,supply.getItemId(),supply.getLocationId());
        int availableQuantity = totalSupply-totalDemand;

        if((availableQuantity+supply.getQuantity()) > existThreshold.get().getMaxThreshold())
            throw new ThresholdNotFoundException("The supply quantity + available quantity is higher than the maximum threshold pleases reduce supply");

        supply.setOrganizationId(organizationId);

        //check given supplyTypes Exist Or Not in Supply Types (Note: if organization enters supply types in lowerCase convert it into upperCase)
        try {
            supply.setSupplyType(supply.getSupplyType().toUpperCase());
            Supply.existSupplyTypes.valueOf(supply.getSupplyType());

        }catch (IllegalArgumentException e){
            throw new SupplyNotFoundException("Supply Type doesn't Exist!");
        }

        supplyRepository.save(supply);
        //create transaction
        LocalDateTime now = LocalDateTime.now();
        Transaction transaction = new Transaction(supply.getItemId(), supply.getLocationId(), "Supply", supply.getQuantity(),dtf.format(now),organizationId);
        transactionRepository.save(transaction);
        return supply;
    }


    public String deleteSupply(String organizationId,String supplyId){

            Optional<Supply> supply = supplyRepository.findByIdAndOrganizationId(supplyId, organizationId);
            if (supply.isEmpty())
                throw new SupplyNotFoundException("Supply Not Found!");
            supplyRepository.deleteById(supplyId);
            return "Supply deleted Successfully!";

    }

    public Supply updateSupply(String organizationId, String supplyId, Supply supply){
        return supplyRepository.findByIdAndOrganizationId(supplyId,organizationId).map(
                newSupply->{
                    newSupply.setItemId(supply.getItemId());
                    newSupply.setLocationId(supply.getLocationId());
                    // redundant  code u can reduce it by making another fun for this (repeated in createSupply)
                    try {
                        supply.setSupplyType(supply.getSupplyType().toUpperCase());
                        Supply.existSupplyTypes.valueOf(supply.getSupplyType());

                    }catch (IllegalArgumentException e){
                        throw new SupplyNotFoundException("Supply Type doesn't Exist!");
                    }
                    newSupply.setSupplyType(supply.getSupplyType());
                    int quantity = newSupply.getQuantity();
                    newSupply.setQuantity(supply.getQuantity());

                    LocalDateTime now = LocalDateTime.now();
                    Transaction transaction = new Transaction(supply.getItemId(), supply.getLocationId(), "Supply", supply.getQuantity(),dtf.format(now),organizationId);
                    transactionRepository.save(transaction);
                    //create transaction
                    return supplyRepository.save(newSupply);
                }
        ).orElseThrow(()->new SupplyNotFoundException("Supply Not Found"));
    }

    public int totalSupplyForItemAtParticularLocation(String organizationId, String itemId, String locationId){
        int totalSupply = 0;
        List<Supply> supplyList = supplyRepository.findByItemIdAndLocationIdAndOrganizationId(itemId, locationId,organizationId);
        if(supplyList.isEmpty())
           return 0;
        for (Supply supply:supplyList) {
            totalSupply+=supply.getQuantity();
        }
        return totalSupply;
    }
    public int totalSupplyForItemAtAllLocation(String organizationId, String itemId){
        int totalSupply = 0;
        List<Supply> supplyList = supplyRepository.findByItemIdAndOrganizationId(itemId,organizationId);

        for (Supply supply:supplyList) {
            totalSupply+=supply.getQuantity();
        }
        return totalSupply;
    }


    public Page<Supply> pageableSupplyDetails(String organizationId, Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Supply> supplyList = supplyRepository.findByOrganizationId(organizationId,pageable);
        if(supplyList.isEmpty())
            throw new ItemNotFoundException("There is no supply exist in the Inventory!");
        return supplyList;
    }
}
