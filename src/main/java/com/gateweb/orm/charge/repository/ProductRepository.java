package com.gateweb.orm.charge.repository;

import com.gateweb.orm.charge.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByProductSourceId(Long productSourceId);

    List<Product> findByProductCategoryId(Long productCategoryId);
}
