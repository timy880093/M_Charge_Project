package com.gateweb.charge.deduct.component;

import com.gateweb.charge.deduct.bean.PurchaseDeductVo;
import com.gateweb.charge.enumeration.DeductStatus;
import com.gateweb.charge.enumeration.DeductType;
import com.gateweb.charge.enumeration.PaymentMethod;
import com.gateweb.charge.exception.MissingRequiredPropertiesException;
import com.gateweb.charge.frontEndIntegration.bean.PayInfo;
import com.gateweb.charge.model.nonMapped.CallerInfo;
import com.gateweb.orm.charge.entity.Bill;
import com.gateweb.orm.charge.entity.Deduct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;

@Component
public class PurchaseDeductVoConverter {
    protected final Logger logger = LogManager.getLogger(getClass());

    public Optional<PurchaseDeductVo> fromMapToVo(HashMap<String, Object> parameterMap, CallerInfo callerInfo) {
        Optional result = Optional.empty();
        try {
            PurchaseDeductVo purchaseDeductVo = new PurchaseDeductVo();
            if (parameterMap.containsKey("companyId")) {
                purchaseDeductVo.setCompanyId(Double.valueOf(String.valueOf(parameterMap.get("companyId"))).longValue());
            } else {
                throw new MissingRequiredPropertiesException("公司不存在");
            }
            if (parameterMap.containsKey("targetProductCategoryId")) {
                purchaseDeductVo.setTargetProductCategoryId(Double.valueOf(String.valueOf(parameterMap.get("targetProductCategoryId"))).longValue());
            } else {
                throw new MissingRequiredPropertiesException("商品類別不存在");
            }
            if (parameterMap.containsKey("quota")) {
                purchaseDeductVo.setQuota(BigDecimal.valueOf(Double.valueOf(String.valueOf(parameterMap.get("quota")))));
            } else {
                throw new MissingRequiredPropertiesException("未設定額度");
            }
            if (parameterMap.containsKey("salesPrice")) {
                purchaseDeductVo.setSalesPrice(BigDecimal.valueOf(Double.valueOf(String.valueOf(parameterMap.get("salesPrice")))));
            } else {
                purchaseDeductVo.setSalesPrice(BigDecimal.ZERO);
            }
            if (parameterMap.containsKey("paymentMethod")) {
                purchaseDeductVo.setPaymentMethod(PaymentMethod.valueOf(String.valueOf(parameterMap.get("paymentMethod"))));
            }
            if (parameterMap.containsKey("payDateMillis")) {
                purchaseDeductVo.setPayDateMillis(Double.valueOf(String.valueOf(parameterMap.get("payDateMillis"))).longValue());
            }
            if (parameterMap.containsKey("paymentRemark")) {
                purchaseDeductVo.setPaymentRemark(String.valueOf(parameterMap.get("paymentRemark")));
            }
            purchaseDeductVo.setCallerId(callerInfo.getUserEntity().getUserId().longValue());
            purchaseDeductVo.setDeductByFee(true);
            purchaseDeductVo.setDeductType(DeductType.FEE_ONLY);
            //先暫時寫死，之後有用到再換
            purchaseDeductVo.setProductCategoryId(new Long(4));
            result = Optional.of(purchaseDeductVo);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return result;
    }

    public Deduct fromVoToEntity(PurchaseDeductVo purchaseDeductVo) {
        Deduct deduct = new Deduct();
        deduct.setDeductStatus(DeductStatus.C);
        deduct.setDeductType(purchaseDeductVo.getDeductType());
        deduct.setDeductByFee(purchaseDeductVo.getDeductByFee());
        deduct.setCreateDate(LocalDateTime.now());
        deduct.setQuota(purchaseDeductVo.getQuota());
        deduct.setCompanyId(purchaseDeductVo.getCompanyId());
        deduct.setTargetProductCategoryId(purchaseDeductVo.getTargetProductCategoryId());
        deduct.setSalesPrice(purchaseDeductVo.getSalesPrice());
        deduct.setCreatorId(purchaseDeductVo.getCallerId());
        deduct.setModifierId(purchaseDeductVo.getCallerId());
        return deduct;
    }

    public Optional<PayInfo> fromVoToPayInfo(Bill bill, PurchaseDeductVo purchaseDeductVo) {
        Optional result = Optional.empty();
        try {
            PayInfo payInfo = new PayInfo();
            payInfo.setBillId(bill.getBillId());
            payInfo.setPayAmount(bill.getTaxIncludedAmount().stripTrailingZeros().toPlainString());
            payInfo.setPayTimestamp(purchaseDeductVo.getPayDateMillis());
            if (purchaseDeductVo.getPaymentMethod() != null) {
                payInfo.setPaymentMethod(purchaseDeductVo.getPaymentMethod().name());
            }
            if (purchaseDeductVo.getPaymentRemark() != null && !purchaseDeductVo.getPaymentRemark().isEmpty()) {
                payInfo.setPaymentRemark(purchaseDeductVo.getPaymentRemark());
            } else {
                payInfo.setPaymentRemark(LocalDateTime.now().toString());
            }
            result = Optional.of(payInfo);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return result;
    }

}
