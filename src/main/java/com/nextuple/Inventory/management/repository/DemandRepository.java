package com.nextuple.Inventory.management.repository;

import com.nextuple.Inventory.management.model.Demand;
import com.nextuple.Inventory.management.model.Supply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface DemandRepository extends MongoRepository<Demand,String> {
    List<Demand> findByItemId(String itemId);

    List<Demand> findByItemIdAndLocationId(String itemId, String locationId);

    List<Demand> findByDemandTypeAndLocationId(String demandType, String locationId);


    List<Demand> findAllByOrganizationId(String organizationId);

    Optional<Demand> findByIdAndOrganizationId(String demandId, String organizationId);

    List<Demand> findByItemIdAndLocationIdAndOrganizationId(String itemId, String locationId, String organizationId);

    List<Demand> findByDemandTypeAndLocationIdAndOrganizationId(String demandType, String locationId, String organizationId);

    List<Demand> findByItemIdAndOrganizationId(String itemId, String organizationId);

    List<Demand> findByLocationIdAndOrganizationId(String locationId, String organizationId);

    Page<Demand> findByOrganizationId(String organizationId, Pageable pageable);
}
