package com.nextuple.Inventory.management.repository;

import com.nextuple.Inventory.management.model.Organization;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface OrganizationRepository extends MongoRepository<Organization, String> {
    Optional<Organization> findByOrganizationEmail(String organizationEmail);
    boolean existsByOrganizationName(String organizationName);
}
