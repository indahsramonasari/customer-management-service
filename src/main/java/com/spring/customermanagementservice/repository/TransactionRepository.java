package com.spring.customermanagementservice.repository;

import com.spring.customermanagementservice.model.TransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionHistory, String> {

}
