package com.nextuple.Inventory.management.repository;

import com.nextuple.Inventory.management.model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleRepository extends MongoRepository<Role,String> {
    Role findByName(String role);
}
