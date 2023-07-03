package com.nextuple.Inventory.management.controller;

import com.nextuple.Inventory.management.model.Location;
import com.nextuple.Inventory.management.model.Threshold;
import com.nextuple.Inventory.management.service.InventoryServices;
import com.nextuple.Inventory.management.service.ThresholdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
@CrossOrigin("*")
@RequestMapping("/threshold")
@RestController
public class ThresholdController {
    @Autowired
    private ThresholdService thresholdService;

    //////////////////////////////////////////////////////////////-->Threshold API<--///////////////////////////////////////////

    @GetMapping("/{organizationId}")
    public ResponseEntity<List<Threshold>> thresholdDetails(@PathVariable("organizationId") String organizationId){
        return new ResponseEntity<>(thresholdService.thresholdDetails(organizationId), HttpStatus.OK);
    }

    @GetMapping("/{organizationId}/{thresholdId}")
    public ResponseEntity<Threshold> findThresholdById( @PathVariable("organizationId")String organizationId, @PathVariable("thresholdId") String thresholdId){
        return  new ResponseEntity<>(thresholdService.findThresholdById(organizationId,thresholdId),HttpStatus.OK);
    }
    @GetMapping("/{organizationId}/{itemId}/{locationId}")
    public ResponseEntity<Threshold>findThresholdByItemIdAtLocationId(@PathVariable("organizationId")String organizationId,@PathVariable("itemId") String itemId, @PathVariable("locationId") String locationId){
        return new ResponseEntity<>(thresholdService.findThresholdByItemIdAtLocation(organizationId,itemId,locationId),HttpStatus.OK);
    }
    @PostMapping("/{organizationId}")
    public ResponseEntity<Threshold>createThreshold(@PathVariable("organizationId")String organizationId,@RequestBody Threshold threshold){
        Threshold thresholdCreated = thresholdService.createThreshold(organizationId,threshold);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("{/thresholdId}")
                .buildAndExpand(thresholdCreated.getId())
                .toUri();
        return ResponseEntity.created(uri).body(thresholdCreated);
    }

    @DeleteMapping("/{organizationId}/{thresholdId}")
    public ResponseEntity<String>deleteThreshold(@PathVariable("organizationId")String organizationId,@PathVariable("thresholdId") String thresholdId){
        thresholdService.deleteThreshold(organizationId,thresholdId);
        return  new ResponseEntity<>("Threshold:"+thresholdId+" Deleted!",HttpStatus.NO_CONTENT);
    }

    @GetMapping("/page/{organizationId}")
    public ResponseEntity<Page<Threshold>> pageableThresholdDetails(@PathVariable String organizationId,
                                                                  @RequestParam(value = "pageNumber", defaultValue = "0",required = false) Integer pageNumber,
                                                                  @RequestParam(value = "pageSize",defaultValue = "5",required = false) Integer pageSize){

        return new ResponseEntity<>(thresholdService.pageableThresholdDetails(organizationId,pageNumber,pageSize),HttpStatus.OK);
    }
    @PatchMapping("v1/{organizationId}/{thresholdId}")
    public ResponseEntity<Threshold>updateThreshold(@PathVariable("organizationId")String organizationId,@PathVariable("thresholdId") String thresholdId, @RequestBody Threshold threshold){
        Threshold updatedThreshold = thresholdService.updateThreshold(organizationId,thresholdId,threshold);
        return new ResponseEntity<>(updatedThreshold,HttpStatus.OK);
    }
    @PatchMapping("/v2/{organizationId}/{itemId}/{locationId}")
    public ResponseEntity<Threshold>updateThresholdByItemAndLocation(@PathVariable("organizationId")String organizationId,@PathVariable("itemId") String itemId, @PathVariable("locationId")String locationId,  @RequestBody Threshold threshold){
        Threshold updatedThreshold = thresholdService.updateThresholdByItemIdAndLocationId(organizationId,itemId,locationId,threshold);
        return new ResponseEntity<>(updatedThreshold,HttpStatus.OK);
    }

}
