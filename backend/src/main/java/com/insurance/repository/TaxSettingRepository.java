package com.insurance.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.insurance.entities.TaxSetting;
import com.insurance.entities.User;

public interface TaxSettingRepository extends JpaRepository<TaxSetting,Long> {

	 @Query("SELECT t FROM TaxSetting t ORDER BY t.updatedAt DESC")
	    Optional<TaxSetting> findLatestTaxSetting();

	 @Query("SELECT t FROM TaxSetting t ORDER BY t.updatedAt DESC")
	  Page<TaxSetting> findLatestTaxSetting(Pageable pageable);

}
