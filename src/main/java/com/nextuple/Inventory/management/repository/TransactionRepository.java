package com.nextuple.Inventory.management.repository;

import com.nextuple.Inventory.management.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TransactionRepository extends MongoRepository<Transaction,String> {

    List<Transaction> findAllByOrganizationId(String organizationId);

    Page<Transaction> findByOrganizationId(String organizationId, Pageable pageable);
}
