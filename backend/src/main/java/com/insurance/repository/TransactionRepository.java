package com.insurance.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import com.insurance.entities.Policy;
import com.insurance.entities.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, String>{

	List<Transaction> findByPolicy(Policy policy);

	Page<Transaction> findByPolicy(Policy policy, PageRequest pageable);

}
