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
@Document(collection = "supply")
public class Supply {
    @Id
    private String id;
    private String itemId;
    private String locationId;
    private String organizationId;
    private String supplyType;
    private int quantity;

    public Supply( String organizationId,String itemId,String locationId,String supplyType, int quantity) {
        this.organizationId = organizationId;
        this.itemId = itemId;
        this.locationId = locationId;
        this.supplyType = supplyType;
        this.quantity = quantity;
    }
    public enum existSupplyTypes {ONHAND, INTRANSIT, DAMAGED}

}
