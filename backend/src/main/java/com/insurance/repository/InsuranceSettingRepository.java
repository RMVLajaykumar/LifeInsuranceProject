package com.insurance.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.insurance.entities.InsuranceSetting;
import com.insurance.entities.User;

public interface InsuranceSettingRepository extends JpaRepository<InsuranceSetting, Long>{

	
	@Query("SELECT i FROM InsuranceSetting i ORDER BY i.updatedAt DESC")
    Optional<InsuranceSetting> findLatestInsuranceSetting();
	
	@Query("SELECT i FROM InsuranceSetting i ORDER BY i.updatedAt DESC")
	  Page<InsuranceSetting> findLatestInsuranceSetting(Pageable pageable);
}
