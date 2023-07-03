package com.nextuple.Inventory.management.repository;

import com.nextuple.Inventory.management.model.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<UserEntity, String> {
    Optional<UserEntity> findByUsername(String userName);
    boolean existsByUsername(String userName);
    boolean existsByUserEmail(String userEmail);
}
