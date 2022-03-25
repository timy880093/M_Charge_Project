package com.gateweb.orm.charge.repository;

import com.gateweb.orm.charge.entity.ProductSource;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface ProductSourceRepositoryCustom {
    Optional<ProductSource> matchCaseWithVo(ProductSource vo);
}
