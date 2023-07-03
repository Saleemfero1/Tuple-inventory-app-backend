package com.nextuple.Inventory.management.controller;
import com.nextuple.Inventory.management.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.nextuple.Inventory.management.service.*;

import java.net.URI;
import java.util.List;
@CrossOrigin("*")
@RequestMapping("/organization")
@RestController
public class OrganizationController {
    @Autowired
    private OrganizationServices organizationServices;

    /////////////////////////////////////////////////organization API's///////////////////////////////////////////////////////////////////////
    @GetMapping("/")
    public ResponseEntity<List<Organization>> findAllOrganization(){
        List<Organization>organizationList = organizationServices.findAllOrganization();
        return new ResponseEntity<>(organizationList, HttpStatus.OK);
    }

    @GetMapping("/{organizationId}")
    public ResponseEntity<Organization>findOrganizationById(@PathVariable("organizationId") String organizationId){
        return new ResponseEntity<>(organizationServices.findOrganizationById(organizationId), HttpStatus.OK);
    }

    @GetMapping("/v1/{organizationEmail}")
    public ResponseEntity<Organization>findOrganizationByEmail(@PathVariable("organizationEmail") String organizationEmail){
        return new ResponseEntity<>(organizationServices.findByEmailId(organizationEmail), HttpStatus.OK);
    }


    @PostMapping("/")
    public  ResponseEntity<Organization>createOrganization(@RequestBody Organization organization){
        Organization organizationCreted = organizationServices.createOrganization(organization);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("{/thresholdId}")
                .buildAndExpand(organizationCreted.getOrganizationId())
                .toUri();
        return ResponseEntity.created(uri).body(organizationCreted);
    }

    @DeleteMapping("/{organizationId}")
    public ResponseEntity<String>organizationDelete(@PathVariable("organizationId") String organizationId){
        return new ResponseEntity<>(organizationServices.deleteOrganization(organizationId), HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{organizationId}")
    public ResponseEntity<Organization> updateOrganization(@RequestBody Organization organization, @PathVariable("organizationId")String organizationId){
        return new ResponseEntity<>(organizationServices.updateOrganization(organization,organizationId),HttpStatus.ACCEPTED);
    }

}
