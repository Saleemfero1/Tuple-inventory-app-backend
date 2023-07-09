package com.nextuple.Inventory.management.service;

import com.nextuple.Inventory.management.dto.LowStockItemDTO;
import com.nextuple.Inventory.management.dto.TopTenItemsVsOtherItems;
import com.nextuple.Inventory.management.exception.ItemNotFoundException;
import com.nextuple.Inventory.management.exception.SupplyNotFoundException;
import com.nextuple.Inventory.management.model.*;
import com.nextuple.Inventory.management.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.aggregation.VariableOperators;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Map.Entry.comparingByValue;

@Component
@Service

public class    InventoryServices {

    @Autowired
    static ItemRepository itemRepository;
    @Autowired
    static LocationRepository locationRepository;
    @Autowired
    static SupplyRepository supplyRepository;
    @Autowired
    static DemandRepository demandRepository;
    @Autowired
    static ThresholdRepository thresholdRepository;
    @Autowired
    static OrganizationRepository organizationRepository;
    @Autowired
    static TransactionRepository transactionRepository;

    public InventoryServices(ItemRepository itemRepository, LocationRepository locationRepository, SupplyRepository supplyRepository, DemandRepository demandRepository, ThresholdRepository thresholdRepository, OrganizationRepository organizationRepository,TransactionRepository transactionRepository){
        InventoryServices.itemRepository = itemRepository;
        InventoryServices.locationRepository = locationRepository;
        InventoryServices.supplyRepository = supplyRepository;
        InventoryServices.demandRepository = demandRepository;
        InventoryServices.thresholdRepository = thresholdRepository;
        InventoryServices.organizationRepository = organizationRepository;
        InventoryServices.transactionRepository = transactionRepository;
    }
    @Autowired
    private SupplyServices supplyServices;
    @Autowired
    private DemandService demandService;
    //////////////////////////////////////////////-->Availability Services<--///////////////////////////////////////////////////////////////////

    public Map<String,Object> AvailableQtyOfTheItemAtTheGivenLocation(String organizationId,String itemId, String locationId){
        Map<String,Object>result = new HashMap<>();

        int totalSupply = supplyServices.totalSupplyForItemAtParticularLocation(organizationId,itemId,locationId);
        int totalDemand = demandService.totalDemandForItemAtParticularLocation(organizationId,itemId,locationId);
        int availableQty = totalSupply-totalDemand;

        result.put("organizationId:",organizationId);
        result.put("itemId",itemId);
        result.put("locationId",locationId);
        result.put("totalSupply",totalSupply);
        result.put("totalDemand",totalDemand);
        result.put("availableQty",availableQty);

       Optional<Threshold> threshold= thresholdRepository.findByItemIdAndLocationIdAndOrganizationId(itemId,locationId,organizationId);
       if(threshold.isPresent()){
        int minThreshold = threshold.get().getMinThreshold();
        if(availableQty<minThreshold)
            result.put("Sock level","Red");
        else if (availableQty == minThreshold) {
            result.put("Sock level","Yellow");
        }else{
            result.put("Sock level","Green");
        }
       }
       return result;

    }


    public Map<String,Object> AvailableQtyOfTheItemAtAllTheLocation(String organizationId,String itemId){
        Map<String,Object>result = new HashMap<>();
        int totalSupply = supplyServices.totalSupplyForItemAtAllLocation(organizationId,itemId);
        int totalDemand = demandService.totalDemandForItemAtAllLocation(organizationId,itemId);

        result.put("organizationId",organizationId);
        result.put("itemId",itemId);
        result.put("totalSupply",totalSupply);
        result.put("totalDemand",totalDemand);
        result.put("locationId","NETWORK");
        result.put("availableQty",totalSupply-totalDemand);
        return result;

    }

 public List<Map<String,Object>> getDetailsOfAllItemWithAvailability(String organizationId){
     List<Map<String,Object>> deatailsList = new ArrayList<>();
     List<Item> ListOfItems = itemRepository.findByOrganizationId(organizationId);

     for(Item item: ListOfItems){
         Map<String,Object> checkSupply = AvailableQtyOfTheItemAtAllTheLocation(organizationId,item.getItemId());
         if(checkSupply.get("totalSupply").equals(0))
             continue;
         deatailsList.add(checkSupply);
     }
     return deatailsList;
 }

 public  Map<String, Integer> getTotalNumbersOfDashboard(String organizationId){
        Map<String,Integer> dataNumber = new HashMap<>();
        List<Item> itemList = itemRepository.findByOrganizationId(organizationId);
        List<Location> locationList = locationRepository.findAllByOrganizationId(organizationId);
        List<Supply> supplyList = supplyRepository.findAllByOrganizationId(organizationId);
        List<Demand> demandList = demandRepository.findAllByOrganizationId(organizationId);
        List<Item> ActiveItem = itemRepository.findByStatusAndOrganizationId(true,organizationId);

     Integer totalDemand = demandList.size();
     Integer totalActiveItem = ActiveItem.size();
     Integer totalItems = itemList.size();
     Integer totalLocation = locationList.size();
     Integer totalSupply = supplyList.size();
     List<LowStockItemDTO> stockDetails = getLowStockItems(organizationId);
     Integer totalTrendingItems = getMostTrendingItems(organizationId).size();
     List<LowStockItemDTO> highStockList = new ArrayList<>();
     List<LowStockItemDTO> lowStockList = new ArrayList<>();

     Set<String> categories = new HashSet<>();
     for (Item item: itemList){
         categories.add(item.getCategory());
     }


     for ( LowStockItemDTO stock : stockDetails) {
         if (stock.getStockType().equals("High Stock")) {
             highStockList.add(stock);
         } else if (stock.getStockType().equals("Low Stock")) {
             lowStockList.add(stock);
         }
     }
     Integer numberOfLowStockItems =lowStockList.size();
     Integer numberHighStockItems =highStockList.size();

         dataNumber.put("TotalItems",totalItems);
         dataNumber.put("TotalSupply",totalSupply);
         dataNumber.put("TotalDemand",totalDemand);
         dataNumber.put("totalLocation",totalLocation);
         dataNumber.put("totalActiveItems",totalActiveItem);
         dataNumber.put("totalTrendingItems",totalTrendingItems);
         dataNumber.put("totalLowStockItems ",numberOfLowStockItems);
         dataNumber.put("totalHighStockItems",numberHighStockItems);
         dataNumber.put("totalCategories",categories.size());
        return dataNumber;
 }

//---------------------------------Transaction----------------------------------
   public void createTransaction(Transaction transaction){
    transactionRepository.save(transaction);
   }

   public  List<Transaction> getTransactions(String organizationId){
        List<Transaction>transactionList = new ArrayList<>();
        transactionList =  transactionRepository.findAllByOrganizationId(organizationId);
        if(transactionList.isEmpty())
            throw new SupplyNotFoundException("Transaction empty!");
        return transactionList;
   }

   //to Get most trending item in the inventory
   public Map<String, Integer>getMostTrendingItems(String organizationId){
       DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
       LocalDate today = LocalDate.now();
       LocalDate fiveDaysAgo = today.minusDays(16);

       Map<String, Integer> trendingItems = new HashMap<>();
       List<Item> itemList = itemRepository.findByOrganizationId(organizationId);
       List<Transaction> transactionList = transactionRepository.findAllByOrganizationId(organizationId)
               .stream()
               .filter(transaction -> transaction.getType().equalsIgnoreCase("demand"))
               .filter(transaction -> {
                   LocalDate transactionDate = LocalDate.parse(transaction.getDate(),formatter);
                   return transactionDate.isAfter(fiveDaysAgo) || transactionDate.isEqual(fiveDaysAgo);
               })
               .sorted(Comparator.comparing(Transaction::getDate))
               .toList();

       for(Transaction transaction:transactionList){
           if(trendingItems.containsKey(transaction.getItemId())){
               trendingItems.put(transaction.getItemId(),trendingItems.get(transaction.getItemId())+transaction.getQuantity());
           }else{
               trendingItems.put(transaction.getItemId(),transaction.getQuantity());
           }
       }
       for (Map.Entry<String, Integer> entry : trendingItems.entrySet()) {
           System.out.println(entry.getKey() + ": " + entry.getValue());
       }
        return trendingItems;
   }

   public List<LowStockItemDTO> getLowStockItems(String organizationId) {
       List<LowStockItemDTO> stockDetails = new ArrayList<>();
       List<Item> itemList = itemRepository.findByOrganizationId(organizationId);
       List<Location> locationList = locationRepository.findAllByOrganizationId(organizationId);

       for (Item item:itemList) {
           for (Location location:locationList) {
               Map<String,Object> result  = AvailableQtyOfTheItemAtTheGivenLocation (organizationId,item.getItemId(),location.getLocationId());
               int totalSupply = Integer.parseInt(result.get("totalSupply").toString());
               int availableQuantity= Integer.parseInt(result.get("availableQty").toString());
               Optional<Threshold>  threshold = thresholdRepository.findByItemIdAndLocationIdAndOrganizationId(item.getItemId(), location.getLocationId(), organizationId);
               if (threshold.isPresent()){
               int minThreshold = threshold.get().getMinThreshold();
               int maxThreshold = threshold.get().getMaxThreshold();
               if(totalSupply >0 && availableQuantity <= (totalSupply*5)/100+minThreshold){
                   stockDetails.add(new LowStockItemDTO(item.getItemId(),location.getLocationId(),"Low Stock",availableQuantity));
               }
               if( totalSupply >0 && availableQuantity >= maxThreshold-(totalSupply*5)/100  ){
                   stockDetails.add(new LowStockItemDTO(item.getItemId(),location.getLocationId(),"High Stock",availableQuantity));
               }
           }}
       }
       return stockDetails;
    }

    public TopTenItemsVsOtherItems topTenItemsVsotherItems(String organizationId){
    List<Item> itemList = itemRepository.findByOrganizationId(organizationId);
    long totalDemandOfAllItems = 0;
    long totalDemandOfTopTenItems=0;

    Map<String,Integer> demandList = new HashMap<>();
        for (Item item:itemList) {
            int totalDemand = demandService.totalDemandForItemAtAllLocation(organizationId,item.getItemId());
            if(totalDemand>0){
                totalDemandOfAllItems+=totalDemand;
                demandList.put(item.getItemId(),totalDemand);
            }
        }
        Map<String, Integer> sortedMap = demandList.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(
                        LinkedHashMap::new,
                        (map, entry) -> map.put(entry.getKey(), entry.getValue()),
                        LinkedHashMap::putAll
                );

        Map<String, Integer> topTenEntries = sortedMap.entrySet().stream()
                .limit(2)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));


        for (Map.Entry<String, Integer> entry : topTenEntries.entrySet()) {
           totalDemandOfTopTenItems+=entry.getValue();
        }
        return new TopTenItemsVsOtherItems(totalDemandOfTopTenItems,totalDemandOfAllItems,topTenEntries);
    }

    public Page<Transaction>pageableTransactionDetails(String organizationId, Integer pageNumber, Integer pageSize, String date) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(date).descending());
        Page<Transaction> transactionList = transactionRepository.findByOrganizationId(organizationId,pageable);
        if(transactionList.isEmpty())
            throw new ItemNotFoundException("There is no Transaction are Exist in the Inventory!");
        return transactionList;
    }

}


