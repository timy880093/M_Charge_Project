package com.gateweb.orm.charge.repository;

import com.gateweb.orm.charge.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {

}
