package com.nextuple.Inventory.management.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@NoArgsConstructor
@Getter @Setter
@Document(collection = "roles")
public class Role {

    @Id
    private String id;
    private String name;
    @DBRef
    private List<UserEntity> users = new ArrayList<>();
    public Role(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
