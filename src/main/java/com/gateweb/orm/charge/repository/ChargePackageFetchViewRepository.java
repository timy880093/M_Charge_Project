package com.gateweb.orm.charge.repository;

import com.gateweb.orm.charge.entity.view.ChargePackageFetchView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ChargePackageFetchViewRepository extends JpaRepository<ChargePackageFetchView, Long> {

    Collection<ChargePackageFetchView> findByEnabledIsTrue();
}
