package com.gateweb.charge.builder;

import com.gateweb.charge.model.nonMapped.CallerInfo;
import com.gateweb.charge.enumeration.ProductType;
import com.gateweb.orm.charge.entity.Product;
import com.gateweb.orm.charge.entity.ProductCategory;
import com.gateweb.orm.charge.entity.ProductPricing;
import com.gateweb.orm.charge.entity.ProductSource;
import com.gateweb.charge.component.propertyProvider.ContextComponent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype")
public class ProductBuilder extends ContextComponent {
    Product product = new Product();

    public ProductBuilder getBuilder() {
        return super.getComponent();
    }

    public ProductBuilder withCallerInfo(CallerInfo callerInfo) {
        product.setCreateDate(callerInfo.getCurrentLocalDateTime());
        product.setCreatorId(callerInfo.getUserEntity().getUserId().longValue());
        product.setModifierId(callerInfo.getUserEntity().getUserId().longValue());
        product.setModifyDate(callerInfo.getCurrentLocalDateTime());
        return this;
    }

    public ProductBuilder withName(String name) {
        product.setProductName(name);
        return this;
    }

    public ProductBuilder withSource(ProductSource productSource) {
        product.setProductSourceId(productSource.getProductSourceId());
        return this;
    }

    public ProductBuilder withProductType(ProductType productType) {
        product.setProductType(productType);
        return this;
    }

    public ProductBuilder withProductCategory(ProductCategory productCategory) {
        product.setProductCategoryId(productCategory.getProductCategoryId());
        return this;
    }

    public ProductBuilder withProductCategory(Long productCategoryId) {
        product.setProductCategoryId(productCategoryId);
        return this;
    }

    public ProductBuilder withProductPricing(ProductPricing productPricing) {
        product.setProductPricingId(productPricing.getProductPricingId());
        return this;
    }

    public Product build() {
        return product;
    }
}
