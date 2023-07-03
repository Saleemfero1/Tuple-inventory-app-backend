package com.nextuple.Inventory.management.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Getter @Setter @NoArgsConstructor
@Document(collection = "threshold")
public class Threshold {
   @Id
   private String id;
   private String organizationId;
   private String itemId;
   private String locationId;
   private int minThreshold;
   private int maxThreshold;
   @DBRef
   private Organization organization;

   public Threshold( String organizationId, String itemId, String locationId, int minThreshold, int maxThreshold) {

      this.organizationId = organizationId;
      this.itemId = itemId;
      this.locationId = locationId;
      this.minThreshold = minThreshold;
      this.maxThreshold = maxThreshold;
   }
}
