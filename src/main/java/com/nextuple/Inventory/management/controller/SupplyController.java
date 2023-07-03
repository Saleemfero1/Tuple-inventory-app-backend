package com.nextuple.Inventory.management.controller;

import com.nextuple.Inventory.management.model.Item;
import com.nextuple.Inventory.management.model.Supply;
import com.nextuple.Inventory.management.service.InventoryServices;
import com.nextuple.Inventory.management.service.SupplyServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
@CrossOrigin("*")
@RequestMapping("/supply")
@RestController
public class SupplyController {
    @Autowired
    private SupplyServices supplyServices;

    /////////////////////////////////////////////////-->Supply API<--//////////////////////////////////////////////////////////
    @GetMapping("/{organizationId}")
    public ResponseEntity<List<Supply>> supplyDetails(@PathVariable("organizationId")String organizationId){
        return new ResponseEntity<>(supplyServices.supplyDetails(organizationId), HttpStatus.OK);
    }

    @GetMapping("/page/{organizationId}")
    public ResponseEntity<Page<Supply>> pageableSupplyDetails(@PathVariable String organizationId,
                                                          @RequestParam(value = "pageNumber", defaultValue = "0",required = false) Integer pageNumber,
                                                          @RequestParam(value = "pageSize",defaultValue = "5",required = false) Integer pageSize){

        return new ResponseEntity<>(supplyServices.pageableSupplyDetails(organizationId,pageNumber,pageSize),HttpStatus.OK);
    }

    @GetMapping("/{organizationId}/{id}")
    public ResponseEntity<Supply>findSupply(@PathVariable("organizationId")String organizationId,@PathVariable("id") String supplyId){
        return new ResponseEntity<>(supplyServices.findSupply(organizationId,supplyId),HttpStatus.OK);
    }

    @GetMapping("/v1/{organizationId}/{itemId}/{locationId}")
    public ResponseEntity<List<Supply>>findSupplyIfItemExistAtLocation(@PathVariable("organizationId")String organizationId,@PathVariable("itemId") String itemId, @PathVariable("locationId") String locationId){
        return new ResponseEntity<>(supplyServices.findSupplyForItem(organizationId,itemId,locationId),HttpStatus.OK);
    }

    @GetMapping("/v2/{organizationId}/{supplyType}/{locationId}")
    public ResponseEntity<List<Supply>>findSupplyWithSupplyTypeForSpecificLocation(@PathVariable("organizationId")String organizationId,@PathVariable("supplyType") String supplyType, @PathVariable("locationId") String locationId){
        return new ResponseEntity<>(supplyServices.findSupplyWithSupplyTypeForSpecificLocation(organizationId,supplyType,locationId),HttpStatus.OK);
    }

    @PostMapping("/{organizationId}")
    public ResponseEntity<Supply>createSupply(@PathVariable("organizationId")String organizationId,@RequestBody Supply supply){
        Supply supplyCreated = supplyServices.createSupply(organizationId,supply);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{supplyId}")
                .buildAndExpand(supplyCreated.getId())
                .toUri();
        return ResponseEntity.created(uri).body(supplyCreated);
    }

    @DeleteMapping("/{organizationId}/{supplyId}")
    public ResponseEntity<String>deleteSupply(@PathVariable("organizationId")String organizationId,@PathVariable("supplyId")  String supplyId){
        return new ResponseEntity<>(supplyServices.deleteSupply(organizationId,supplyId),HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{organizationId}/{supplyId}")
    public ResponseEntity<Supply>updateSupply(@PathVariable("organizationId")String organizationId,@PathVariable("supplyId") String supplyId, @RequestBody Supply supply){
        Supply updatedSupply = supplyServices.updateSupply(organizationId,supplyId, supply);
        return new ResponseEntity<>(updatedSupply,HttpStatus.OK);
    }

}
