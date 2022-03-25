/*
 * $Header: $
 * This java source file is generated by pkliu on Tue Jan 30 14:38:14 CST 2018
 * For more information, please contact pkliu@sysfoundry.com
 */
package com.gateweb.orm.charge.repository;

import com.gateweb.orm.charge.entity.ChargeModeCycleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author pkliu
 * <p>
 * This class provides methods to populate DB Table of ChargeModeCycle
 */
@Repository
public interface ChargeModeCycleRepository extends JpaRepository<ChargeModeCycleEntity, Integer> {

    ChargeModeCycleEntity findByChargeId(Integer chargeId);

    List<ChargeModeCycleEntity> findByStatus(Integer status);


}

	