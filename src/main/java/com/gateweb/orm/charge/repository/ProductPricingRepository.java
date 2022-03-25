package com.gateweb.orm.charge.repository;

import com.gateweb.orm.charge.entity.ProductPricing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.Optional;

public interface ProductPricingRepository extends JpaRepository<ProductPricing, Long> {

    Optional<ProductPricing> findByBasePrice(BigDecimal basePrice);
}
