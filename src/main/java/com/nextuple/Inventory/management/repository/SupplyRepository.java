package com.nextuple.Inventory.management.repository;

import com.nextuple.Inventory.management.model.Item;
import com.nextuple.Inventory.management.model.Supply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SupplyRepository extends MongoRepository<Supply,String> {
    List<Supply> findAllByOrganizationId(String userId);
    Optional<Supply> findByIdAndOrganizationId(String id,String userId);
    List<Supply> findByItemIdAndLocationIdAndOrganizationId(String itemId, String locationId, String userId);
    List<Supply> findBySupplyTypeAndLocationIdAndOrganizationId(String supplyType, String locationId, String userId);
    List<Supply> findByItemIdAndOrganizationId(String itemId, String organizationId);
    List<Supply> findByLocationIdAndOrganizationId(String locationId, String organizationId);

    Page<Supply> findByOrganizationId(String organizationId, Pageable pageable);
}
