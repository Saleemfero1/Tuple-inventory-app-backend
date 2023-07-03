package com.nextuple.Inventory.management.dto;

import lombok.*;

@Data
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class LowStockItemDTO {
    private String ItemId;
    private String LocationId;
    private String StockType;
    private int Quantity;

}
