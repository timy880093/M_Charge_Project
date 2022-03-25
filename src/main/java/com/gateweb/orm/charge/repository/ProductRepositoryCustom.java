package com.gateweb.orm.charge.repository;

import java.util.List;

import com.gateweb.charge.frontEndIntegration.datatablePagination.PageInfo;
import com.gateweb.orm.charge.entity.Product;
import com.gateweb.orm.charge.entity.ProductSource;

public interface ProductRepositoryCustom {
    List<Product> productCriteriaSearch(ProductSource productSourceVo, Product productVo, PageInfo pageInfo);
}