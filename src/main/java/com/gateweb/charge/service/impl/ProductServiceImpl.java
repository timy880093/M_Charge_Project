package com.gateweb.charge.service.impl;

import com.gateweb.charge.frontEndIntegration.datatablePagination.PageInfo;
import com.gateweb.charge.service.ProductService;
import com.gateweb.charge.service.dataGateway.ContractDataGateway;
import com.gateweb.charge.service.dataGateway.ProductDataGateway;
import com.gateweb.orm.charge.entity.*;
import com.gateweb.orm.charge.entity.view.ProductFetchView;
import com.gateweb.orm.charge.repository.*;
import com.gateweb.utils.bean.BeanConverterUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    final BeanConverterUtils beanConverterUtils = new BeanConverterUtils();

    @Autowired
    PackageRefRepository packageRefRepository;
    @Autowired
    ChargeRuleRepository chargeRuleRepository;
    @Autowired
    ProductCategoryRepository productCategoryRepository;
    @Autowired
    ProductPricingRepository productPricingRepository;
    @Autowired
    ProductFetchViewRepository productFetchViewRepository;
    @Autowired
    ProductDataGateway productDataGatewayImpl;
    @Autowired
    ContractDataGateway contractDataGateway;

    /**
     * 從已知的待結清單中找到產品名稱。
     * 我們並不關心他的日期區間
     *
     * @param
     * @return
     */
    @Override
    public String getProductName(BillingItem billingItem) {
        StringBuffer productNameBuffer = new StringBuffer();
        if (billingItem.getPackageRefId() != null) {
            // 若有contractId則加入packageName
            Optional<PackageRef> packageRefOptional = packageRefRepository.findById(billingItem.getPackageRefId());
            if (packageRefOptional.isPresent()) {
                if (packageRefOptional.get().getFromPackageId() != null) {
                    Optional<Contract> contractOptional = contractDataGateway.findByContractId(billingItem.getContractId());
                    if (contractOptional.isPresent()) {
                        productNameBuffer.append(contractOptional.get().getName());
                    }
                }
                if (packageRefOptional.get().getToChargeRuleId() != null) {
                    Optional<ChargeRule> chargeModeOptional = chargeRuleRepository.findByChargeRuleId(packageRefOptional.get().getToChargeRuleId());
                    if (chargeModeOptional.isPresent()) {
                        Optional<ProductCategory> productCategoryOptional = productCategoryRepository.findById(chargeModeOptional.get().getProductCategoryId());
                        if (productCategoryOptional.isPresent()) {
                            productNameBuffer.append("-" + productCategoryOptional.get().getCategoryName());
                        }
                    }
                }
            }
        }
        if (billingItem.getRemark() != null
                && productNameBuffer.toString().isEmpty()
                && billingItem.getRemark().indexOf("(") != -1) {
            //去掉序號標記
            int indexOfSeq = billingItem.getRemark().indexOf("(");
            productNameBuffer.append(billingItem.getRemark().substring(0, indexOfSeq));
        }
        return productNameBuffer.toString();
    }

    @Override
    public ProductPricing createOrGetProductPricing(BigDecimal basePrice) {
        ProductPricing productPricing = new ProductPricing();
        Optional<ProductPricing> pricingOptional = productPricingRepository.findByBasePrice(basePrice);
        if (!pricingOptional.isPresent()) {
            productPricing.setInActive(true);
            productPricing.setCreateDate(new Timestamp(new Date().getTime()));
            productPricing.setBasePrice(basePrice);
            productPricingRepository.save(productPricing);
        } else {
            productPricing = pricingOptional.get();
        }
        return productPricing;
    }

    @Override
    public List<ProductFetchView> serverSideProcessingSearchByPageInfo(PageInfo pageInfo) {
        List<ProductFetchView> productFetchViewList = new ArrayList<ProductFetchView>();

        beanConverterUtils.mapToBean(pageInfo.getCondition(), Product.class);
        beanConverterUtils.mapToBean(pageInfo.getCondition(), ProductSource.class);

        List<Product> productList = productDataGatewayImpl.searchListByPageInfo(pageInfo);

        productList.stream().forEach(product -> {
            Optional<ProductFetchView> productFetchViewOptional = productFetchViewRepository.findById(product.getProductId());
            if (productFetchViewOptional.isPresent()) {
                productFetchViewList.add(productFetchViewOptional.get());
            }
        });

        return productFetchViewList;
    }

}
