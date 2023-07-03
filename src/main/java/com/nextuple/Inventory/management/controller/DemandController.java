package com.nextuple.Inventory.management.controller;
import com.nextuple.Inventory.management.model.Demand;
import com.nextuple.Inventory.management.model.Location;
import com.nextuple.Inventory.management.service.DemandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
@CrossOrigin("*")
@RequestMapping("/demand")
@RestController
public class DemandController {
    @Autowired
    private DemandService demandService;

    //////////////////////////////////////////////////-->Demand API<--////////////////////////////////////////////////////////
    @GetMapping("/{organizationId}")
    public ResponseEntity<List<Demand>> demandDetails(@PathVariable("organizationId")String organizationId){
        return new ResponseEntity<>(demandService.demandDetails(organizationId), HttpStatus.OK);
    }

    @GetMapping("/page/{organizationId}")
    public ResponseEntity<Page<Demand>> pageableDemandDetails(@PathVariable String organizationId,
                                                              @RequestParam(value = "pageNumber", defaultValue = "0",required = false) Integer pageNumber,
                                                              @RequestParam(value = "pageSize",defaultValue = "5",required = false) Integer pageSize){

        return new ResponseEntity<>(demandService.pageableDemandDetails(organizationId,pageNumber,pageSize),HttpStatus.OK);
    }
    @GetMapping("/{organizationId}/{id}")
    public ResponseEntity<Demand>findDemand(@PathVariable("id") String demandId, @PathVariable("organizationId")String organizationId){
        return new ResponseEntity<>(demandService.findDemand(organizationId,demandId),HttpStatus.OK);
    }

    @GetMapping("/v1/{organizationId}/{item}/{location}")
    public ResponseEntity<List<Demand>> findDemandForItemAndLocation(@PathVariable("organizationId") String organizationId,@PathVariable("item") String itemId, @PathVariable("location") String loactionId){
        return new ResponseEntity<>(demandService.findDemandForItem(organizationId,itemId,loactionId),HttpStatus.OK);
    }

    @GetMapping("/v2/{organizationId}/{demandType}/{locationId}")
    public ResponseEntity<List<Demand>>findDemandWithDemandTypeForSpecificLocation(@PathVariable("organizationId")String organizationId,@PathVariable("demandType") String demandType, @PathVariable("locationId") String locationId){
        return new ResponseEntity<>(demandService.findDemandWithDemandTypeForSpecificLocation(organizationId,demandType,locationId),HttpStatus.OK);
    }


    @PostMapping("/{organizationId}")
    public ResponseEntity<Demand>createDemand(@PathVariable("organizationId") String organizationId,@RequestBody Demand demand){
        Demand demandCreated = demandService.createDemand(organizationId,demand);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{supplyId}")
                .buildAndExpand(demandCreated.getId())
                .toUri();
        return ResponseEntity.created(uri).body(demandCreated);
    }

    @DeleteMapping("/{organizationId}/{demandId}")
    public ResponseEntity<String>deleteDemand(@PathVariable("organizationId")String organizationId,@PathVariable("demandId")  String demandId){
        return new ResponseEntity<>(demandService.deleteDemand(organizationId,demandId),HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{organizationId}/{demandId}")
    public ResponseEntity<Demand>updateSupply(@PathVariable("organizationId")String organizationId, @PathVariable("demandId") String demandId, @RequestBody Demand demand){
        Demand updatedDemand = demandService.updateDemand(organizationId,demandId, demand);
        return new ResponseEntity<>(updatedDemand,HttpStatus.OK);
    }

}
