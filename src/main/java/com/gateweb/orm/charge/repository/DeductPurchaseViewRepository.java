package com.gateweb.orm.charge.repository;

import com.gateweb.orm.charge.entity.view.DeductPurchaseView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeductPurchaseViewRepository extends JpaRepository<DeductPurchaseView, Long> {

    List<DeductPurchaseView> findByCompanyId(Long companyId);

}
