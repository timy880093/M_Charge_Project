package com.gateweb.orm.charge.repository;

import com.gateweb.orm.charge.entity.view.ChargeRuleFetchView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChargeRuleFetchViewRepository extends JpaRepository<ChargeRuleFetchView, Long> {
    
}
