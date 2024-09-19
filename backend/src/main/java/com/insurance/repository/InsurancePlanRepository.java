package com.insurance.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;

import com.insurance.entities.InsurancePlan;
import com.insurance.entities.InsuranceScheme;

public interface InsurancePlanRepository extends JpaRepository<InsurancePlan, String>{

	 Page<InsurancePlan> findByInsuranceSchemeInsuranceSchemeId(String schemeId, Pageable pageable);

	InsurancePlan findByInsuranceScheme(InsuranceScheme insuranceScheme); 

}
