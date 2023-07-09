package com.nextuple.Inventory.management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class RegionBasedDemand {
    private String itemId;
    private  String itemName;
    private String locationId;
    private int demand;
    private int supply;
}
