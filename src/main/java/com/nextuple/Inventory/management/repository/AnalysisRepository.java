package com.nextuple.Inventory.management.repository;

import com.nextuple.Inventory.management.model.Analysis;
import com.nextuple.Inventory.management.model.Demand;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface AnalysisRepository extends  MongoRepository<Analysis,String> {

List<Analysis> findAllByItemName(String itemName);

    List<Analysis> findByItemName(String itemName);

    List<Analysis> findAllByOrganizationName(String organizationName);
}
