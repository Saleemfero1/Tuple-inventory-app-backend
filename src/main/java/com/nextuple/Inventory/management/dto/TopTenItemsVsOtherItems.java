package com.nextuple.Inventory.management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TopTenItemsVsOtherItems {
    private long totalDemandOfTopTenItems;
    private long getTotalDemandOfOtherItems;
    private Map<String,Integer> TopTenItemsList;
}
