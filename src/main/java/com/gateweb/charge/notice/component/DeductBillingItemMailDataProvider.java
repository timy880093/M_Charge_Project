package com.gateweb.charge.notice.component;

import com.gateweb.charge.enumeration.DeductType;
import com.gateweb.charge.notice.bean.PaymentRequestMailBillingItem;
import com.gateweb.orm.charge.entity.BillingItem;
import com.gateweb.orm.charge.entity.Deduct;
import com.gateweb.orm.charge.entity.ProductCategory;
import com.gateweb.orm.charge.repository.DeductRepository;
import com.gateweb.orm.charge.repository.ProductCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Component
public class DeductBillingItemMailDataProvider {
    @Autowired
    DeductRepository deductRepository;
    @Autowired
    ProductCategoryRepository productCategoryRepository;

    public Set<PaymentRequestMailBillingItem> genDeductBillingItemMailData(Set<BillingItem> billingItemSet) {
        Set<PaymentRequestMailBillingItem> paymentRequestMailBillingItemSet = new HashSet<>();
        //濾出為deduct類型的billingItem
        Set<BillingItem> deductBillingItemSet = billingItemSet.stream().filter(billingItem -> {
            return billingItem.getDeductId() != null;
        }).collect(Collectors.toSet());
        deductBillingItemSet.stream().forEach(billingItem -> {
            Optional<Deduct> deductOptional = deductRepository.findByDeductId(billingItem.getDeductId());
            if (deductOptional.isPresent()) {
                PaymentRequestMailBillingItem paymentRequestMailBillingItem
                        = genDeductBillingItemMailData(deductOptional.get());
                paymentRequestMailBillingItemSet.add(paymentRequestMailBillingItem);
            }
        });
        return paymentRequestMailBillingItemSet;
    }

    public PaymentRequestMailBillingItem genDeductBillingItemMailData(Deduct deduct) {
        PaymentRequestMailBillingItem paymentRequestMailBillingItem = new PaymentRequestMailBillingItem();
        paymentRequestMailBillingItem.setItemName(genDeductBillingItemName(deduct));
        paymentRequestMailBillingItem.setQuota("");
        paymentRequestMailBillingItem.setChargeType("預繳");
        paymentRequestMailBillingItem.setTaxIncludedAmount(deduct.getSalesPrice());
        return paymentRequestMailBillingItem;
    }

    public String genDeductBillingItemName(Deduct deduct) {
        StringBuilder stringBuilder = new StringBuilder();
        Optional<ProductCategory> productCategoryOptional = productCategoryRepository.findById(deduct.getProductCategoryId());
        if (productCategoryOptional.isPresent()) {
            stringBuilder.append(productCategoryOptional.get().getCategoryName());
        }
        if (deduct.getDeductType().equals(DeductType.FEE_ONLY)) {
            stringBuilder.append("金額預繳");
        }
        return stringBuilder.toString();
    }
}
