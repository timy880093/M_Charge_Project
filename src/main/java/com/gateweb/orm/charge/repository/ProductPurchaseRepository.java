package com.gateweb.orm.charge.repository;

import com.gateweb.orm.charge.entity.ProductPurchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductPurchaseRepository extends JpaRepository<ProductPurchase, Long> {
    @Query(value = "SELECT * \n" +
            "FROM product_purchase pp\n" +
            "JOIN product_source ps ON (pp.product_source_id = ps.product_source_id)\n" +
            "WHERE ps.deduct_id IS NOT NULL\n AND pp.company_id = ?1 ", nativeQuery = true)
    List<ProductPurchase> searchDeductProductPurchaseList(Long companyId);

}
