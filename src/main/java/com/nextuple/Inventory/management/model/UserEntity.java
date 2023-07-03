package com.nextuple.Inventory.management.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@Setter @Getter @Data
@Document(collection = "userEntity")
public class UserEntity {
    @Id @Indexed(unique = true)
    private String id;
    private String username;
    private String userEmail;
    private String password;
    private String organizationId;
    private List<Role> roles =  new ArrayList<>();

    public UserEntity(String id, String username, String userEmail, String password, String organizationId) {
        this.id = id;
        this.username = username;
        this.userEmail = userEmail;
        this.password = password;
        this.organizationId = organizationId;
    }
}
