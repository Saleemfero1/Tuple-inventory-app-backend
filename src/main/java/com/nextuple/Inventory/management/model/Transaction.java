package com.nextuple.Inventory.management.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Getter
@Setter
@NoArgsConstructor

@Document(collection = "transaction")
public class Transaction {
    @Id
    private String id;
    private String itemId;
    private String locationId;
    private String type;
    private int quantity;
    private String date;
    private String organizationId;
    @DBRef
    private Organization organization;

    public Transaction( String itemId, String locationId, String type, int quantity, String date, String organizationId) {
        this.id = id;
        this.itemId = itemId;
        this.locationId = locationId;
        this.type = type;
        this.quantity = quantity;
        this.date = date;
        this.organizationId = organizationId;
    }
}
