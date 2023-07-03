package com.nextuple.Inventory.management.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "organization")
public class Organization {
    @Id @Indexed(unique = true)
    private String organizationId;
    private String organizationName;
    private String organizationEmail;
    private String password;
}
