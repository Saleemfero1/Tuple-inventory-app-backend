package com.nextuple.Inventory.management.repository;
import com.nextuple.Inventory.management.model.Item;
import com.nextuple.Inventory.management.model.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Component
@Repository
public interface LocationRepository extends MongoRepository<Location,String>{
    Optional<Location> findByLocationIdAndOrganizationId(String locationId, String organizationId);

    List<Location> findAllByOrganizationId(String organizationId);

    Page<Location> findByOrganizationId(String organizationId, Pageable pageable);
}
