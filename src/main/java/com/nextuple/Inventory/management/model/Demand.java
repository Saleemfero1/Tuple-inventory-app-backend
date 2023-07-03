package com.nextuple.Inventory.management.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Getter @Setter @NoArgsConstructor
@Document(collection = "demand")
public class Demand {
    @Id
    private  String id;
    private String organizationId;
    private String itemId;
    private String locationId;
    private String demandType;
    private int quantity;
    public Demand( String organizationId,String demandType, int quantity, String itemId, String locationId) {
        this.organizationId = organizationId;
        this.demandType = demandType;
        this.quantity = quantity;
        this.itemId = itemId;
        this.locationId = locationId;
    }
    public enum existDemandTypes{HARDPROMISED, PLANNED}
}
