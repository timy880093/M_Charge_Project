package com.gateweb.orm.charge.repository;

import com.gateweb.orm.charge.entity.view.ChargePackageView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChargePackageViewRepository extends JpaRepository<ChargePackageView, Long> {
}
