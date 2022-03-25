package com.gateweb.charge.service.dataGateway;

import com.gateweb.charge.dsl.component.DslCommonComponent;
import com.gateweb.charge.dsl.component.ProductDslComponent;
import com.gateweb.charge.frontEndIntegration.datatablePagination.PageInfo;
import com.gateweb.orm.charge.entity.Product;
import com.querydsl.core.support.QueryBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductDataGateway {
    @Autowired
    ProductDslComponent productDslServiceImpl;
    @Autowired
    DslCommonComponent dslCommonService;

    public List<Product> searchListByPageInfo(PageInfo pageInfo) {
        //依照客製的搜尋條件進行過濾
        QueryBase queryBase = productDslServiceImpl.buildCriteriaPageableSearch(pageInfo.getCondition());
        List<Product> productList = dslCommonService.fetchPageData(productDslServiceImpl, queryBase, pageInfo);
        return productList;
    }

}
