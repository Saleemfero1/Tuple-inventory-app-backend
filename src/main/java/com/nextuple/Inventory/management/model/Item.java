package com.nextuple.Inventory.management.model;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
@Data
@Getter @Setter @NoArgsConstructor
@Document(collection = "items")
public  class Item {
    @Id @Indexed(unique = true)
    private String itemId;
    private String itemName;
    private String itemDescription;
    private String category;
    private  String type;
    private  double price;
    private  boolean status;
    private boolean pickupAllowed;
    private boolean shippingAllowed;
    private boolean deliveryAllowed;
    private String  organizationId;

    public Item(String itemId, String itemName, String itemDescription, String category, String type, boolean status, double price, boolean pickupAllowed, boolean shippingAllowed, boolean deliveryAllowed,String organizationId) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.category = category;
        this.type = type;
        this.status = status;
        this.price = price;
        this.pickupAllowed = pickupAllowed;
        this.shippingAllowed = shippingAllowed;
        this.deliveryAllowed = deliveryAllowed;
        this.organizationId = organizationId;
    }


}
