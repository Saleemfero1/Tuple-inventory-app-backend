package com.nextuple.Inventory.management.model;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Getter @Setter @NoArgsConstructor
@Document(collection = "locations")
public class Location {
    @Id @Indexed(unique = true)
    private String locationId;
    private String locationDesc;
    private String locationType;
    private boolean  pickupAllowed;
    private boolean shippingAllowed;
    private boolean deliveryAllowed;
    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String city;
    private String state;
    private String country;
    private String pinCode;
    private String organizationId;

    public Location(String locationId, String locationDesc, String locationType, boolean pickupAllowed, boolean shippingAllowed, boolean deliveryAllowed, String addressLine1, String addressLine2,String addressLine3, String city, String state, String country, String pinCode,String organizationId) {
        this.locationId = locationId;
        this.locationDesc = locationDesc;
        this.locationType = locationType;
        this.pickupAllowed = pickupAllowed;
        this.shippingAllowed = shippingAllowed;
        this.deliveryAllowed = deliveryAllowed;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.addressLine3=addressLine3;
        this.city = city;
        this.state = state;
        this.country = country;
        this.pinCode = pinCode;
        this.organizationId = organizationId;
    }
}
