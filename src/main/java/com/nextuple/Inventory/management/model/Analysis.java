package com.nextuple.Inventory.management.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Getter @Setter @AllArgsConstructor
@Document(collection = "sales")
public class Analysis {

    private String organizationName;
    @Id
    private String itemName;
    private int quantity;
    private long costToCompany;
    private  long profit;
    private  int manPower;
    private int time;
    private double predictedSale;
    private long orderingCost;
    private  float eoq;
}
