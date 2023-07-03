package com.nextuple.Inventory.management.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nextuple.Inventory.management.dto.LowStockItemDTO;
import com.nextuple.Inventory.management.dto.TopTenItemsVsOtherItems;
import com.nextuple.Inventory.management.model.Item;
import com.nextuple.Inventory.management.model.Transaction;
import com.nextuple.Inventory.management.service.InventoryServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@CrossOrigin("*")
@RequestMapping("/availability")
@RestController
public class InventoryController {
    @Autowired
    private InventoryServices inventoryServices;

    ////////////////////////////////////////////////////-->Availability API<--////////////////////////////////////////////////

    @GetMapping("/v1/{organizationId}/{itemId}/{locationId}")
    public ResponseEntity<Map<String,Object>> AvailableQtyOfTheItemAtTheGivenLocation(@PathVariable("organizationId")String organizationId, @PathVariable("itemId") String itemId, @PathVariable("locationId") String locationId) throws JsonProcessingException {
        Map<String,Object> result  = inventoryServices.AvailableQtyOfTheItemAtTheGivenLocation(organizationId,itemId,locationId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/v2/{organizationId}/{itemId}")
    public ResponseEntity<Map<String,Object>>AvailableQtyOfTheItemAtAllTheLocation(@PathVariable("organizationId")String organizationId,@PathVariable("itemId") String itemId) throws JsonProcessingException {
        Map<String,Object> result  = inventoryServices.AvailableQtyOfTheItemAtAllTheLocation(organizationId,itemId);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }
    @GetMapping("/v3/{organizationId}")
    public ResponseEntity <List<Map<String,Object>>> getDetailsOfAllItemWithAvailability(@PathVariable("organizationId")String organizationId) throws JsonProcessingException {
        List<Map<String,Object>>result  = inventoryServices.getDetailsOfAllItemWithAvailability(organizationId);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @GetMapping("/v4/{organizationId}")
    public ResponseEntity <Map<String,Integer>> getTotalNumbersOfDashboard(@PathVariable("organizationId")String organizationId) throws JsonProcessingException {
        Map<String,Integer>result  = inventoryServices.getTotalNumbersOfDashboard(organizationId);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }
    @GetMapping("/v5/{organizationId}")
    public ResponseEntity<List<Transaction>>getTransaction(@PathVariable("organizationId") String organizationId){
        return new ResponseEntity<>(inventoryServices.getTransactions(organizationId),HttpStatus.OK);
    }
    @GetMapping("/v10/{organizationId}")
    public ResponseEntity<Page<Transaction>> pageableGetTransaction(@PathVariable String organizationId,
                                                          @RequestParam(value = "pageNumber", defaultValue = "0",required = false) Integer pageNumber,
                                                          @RequestParam(value = "pageSize",defaultValue = "5",required = false) Integer pageSize,
                                                                    @RequestParam(value = "sortBy", defaultValue ="date",required = false) String date){

        return new ResponseEntity<>(inventoryServices.pageableTransactionDetails(organizationId,pageNumber,pageSize,date),HttpStatus.OK);
    }

    //to get most trending items
    @GetMapping("/v6/{organizationId}")
    public ResponseEntity<TopTenItemsVsOtherItems>topTenItemsVsOtherItems(@PathVariable("organizationId") String organizationId){
        return new ResponseEntity<>(inventoryServices.topTenItemsVsotherItems(organizationId),HttpStatus.OK);
    }

    //to get low stockItems globally
    @GetMapping("/v9/{organizationId}")
    public ResponseEntity<List<LowStockItemDTO>> getLowStockItems(@PathVariable("organizationId") String organizationId){
        return new ResponseEntity<>(inventoryServices.getLowStockItems(organizationId),HttpStatus.OK);
    }

}
