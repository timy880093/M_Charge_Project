package com.gateweb.charge.service;

import com.gateweb.charge.frontEndIntegration.datatablePagination.PageInfo;
import com.gateweb.orm.charge.entity.view.ProductFetchView;
import com.gateweb.orm.charge.entity.BillingItem;
import com.gateweb.orm.charge.entity.ProductPricing;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    String getProductName(BillingItem billingItem);

    ProductPricing createOrGetProductPricing(BigDecimal basePrice);

    List<ProductFetchView> serverSideProcessingSearchByPageInfo(PageInfo pageInfo);
}
