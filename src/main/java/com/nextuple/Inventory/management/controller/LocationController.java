package com.nextuple.Inventory.management.controller;

import com.nextuple.Inventory.management.model.Item;
import com.nextuple.Inventory.management.model.Location;
import com.nextuple.Inventory.management.service.InventoryServices;
import com.nextuple.Inventory.management.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
@CrossOrigin("*")
@RequestMapping("/location")
@RestController
public class LocationController {
   @Autowired
    private LocationService locationService;

    //////////////////////////////////////////-->Location-API-->//////////////////////////////////////////

    @GetMapping("/{userId}")
    public ResponseEntity<List<Location>> locationDetails(@PathVariable("userId") String userId){
        return new ResponseEntity<>(locationService.locationDetails(userId), HttpStatus.OK);
    }
    @GetMapping("/page/{organizationId}")
    public ResponseEntity<Page<Location>> pageableLocationDetails(@PathVariable String organizationId,
                                                          @RequestParam(value = "pageNumber", defaultValue = "0",required = false) Integer pageNumber,
                                                          @RequestParam(value = "pageSize",defaultValue = "5",required = false) Integer pageSize){

        return new ResponseEntity<>(locationService.pageableLocationDetails(organizationId,pageNumber,pageSize),HttpStatus.OK);
    }
    @GetMapping("/{userId}/{id}")
    public ResponseEntity<Location>findLocation(@PathVariable("userId")String userId,@PathVariable("id") String locationId){
        Location foundLocation = locationService.findLocation(userId,locationId);
        return ResponseEntity.ok(foundLocation);
    }
    @PostMapping("/{userId}")
    public ResponseEntity<Location>createLocation(@PathVariable("userId")String userId,@RequestBody Location location)
            throws URISyntaxException {
        Location locationCreated = locationService.createLocation(userId,location);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(locationCreated.getLocationId())
                .toUri();
        return ResponseEntity.created(uri).body(locationCreated);
    }

    @DeleteMapping("/{userId}/{id}")
    public ResponseEntity<String> deleteLocation(@PathVariable("userId") String userId,@PathVariable("id") String locationId){
        String result = locationService.deleteLocationIfNotPresentInReferenceCollection(locationId,userId);
        return new ResponseEntity<>(result,HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{userId}/{id}")
    public ResponseEntity<Location>updateLocation(@RequestBody Location location, @PathVariable("id") String locationId, @PathVariable("userId")String userId){
        Location updatedLocation = locationService.updateLocation(location,locationId, userId).get();
        return  new ResponseEntity<>(updatedLocation,HttpStatus.CREATED);
    }
}
