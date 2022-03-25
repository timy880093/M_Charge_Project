package com.gateweb.charge.deduct.component;

import com.gateweb.charge.deduct.bean.PurchaseDeductVo;
import com.gateweb.charge.eventBus.ChargeSystemEvent;
import com.gateweb.charge.eventBus.EventAction;
import com.gateweb.charge.eventBus.EventSource;
import com.gateweb.charge.exception.MissingRequiredPropertiesException;
import com.gateweb.charge.frontEndIntegration.bean.PayInfo;
import com.gateweb.charge.model.nonMapped.CallerInfo;
import com.gateweb.charge.service.BillService;
import com.gateweb.orm.charge.entity.Bill;
import com.gateweb.orm.charge.entity.BillingItem;
import com.gateweb.orm.charge.entity.Deduct;
import com.gateweb.orm.charge.repository.BillRepository;
import com.gateweb.orm.charge.repository.BillingItemRepository;
import com.gateweb.orm.charge.repository.CompanyRepository;
import com.gateweb.orm.charge.repository.DeductRepository;
import com.google.common.eventbus.EventBus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DeductPurchaseComponent {
    protected final Logger logger = LogManager.getLogger(getClass());

    @Autowired
    PurchaseDeductVoConverter purchaseDeductVoConverter;
    @Autowired
    DeductRepository deductRepository;
    @Autowired
    BillRepository billRepository;
    @Autowired
    BillService billService;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    BillingItemRepository billingItemRepository;
    @Autowired
    @Qualifier("chargeSystemEventBus")
    EventBus chargeSystemEventBus;

    public Optional<Deduct> purchaseDeductByMap(HashMap<String, Object> parameterMap, CallerInfo callerInfo) throws MissingRequiredPropertiesException {
        Optional<Deduct> deductOpt = Optional.empty();
        Optional<PurchaseDeductVo> deductPurchaseVoOpt = purchaseDeductVoConverter.fromMapToVo(parameterMap, callerInfo);
        if (deductPurchaseVoOpt.isPresent()) {
            deductOpt = savePurchaseDeductVo(deductPurchaseVoOpt.get());
            if (deductOpt.isPresent()) {
                //直接出帳
                List<Bill> billList = outToBillByDeduct(deductOpt.get(), callerInfo.getUserEntity().getUserId().longValue());
                //直接入帳
                billList.stream().forEach(bill -> {
                    try {
                        Optional<PayInfo> payInfoOpt = purchaseDeductVoConverter.fromVoToPayInfo(bill, deductPurchaseVoOpt.get());
                        if (payInfoOpt.isPresent()) {
                            billService.transactionPayBill(payInfoOpt.get(), deductPurchaseVoOpt.get().getCallerId());
                        }
                    } catch (Exception ex) {
                        logger.error(ex.getMessage());
                    }
                });
            }
        }
        return deductOpt;
    }

    public List<Bill> outToBillByDeduct(Deduct deduct, Long callerId) {
        Bill billTemplate = new Bill();
        billTemplate.setCompanyId(deduct.getCompanyId());
        Set<BillingItem> billingItemSet = new HashSet<>(billingItemRepository.findByDeductIdAndBillIdIsNull(deduct.getDeductId()));
        return billService.outBill(billTemplate, billingItemSet, false, callerId);
    }

    public Optional<Deduct> savePurchaseDeductVo(PurchaseDeductVo purchaseDeductVo) {
        Optional<Deduct> result = Optional.empty();
        try {
            Deduct deduct = purchaseDeductVoConverter.fromVoToEntity(purchaseDeductVo);
            deductRepository.save(deduct);
            //發送訊息到eventBus中
            ChargeSystemEvent chargeSystemEvent = new ChargeSystemEvent(
                    EventSource.DEDUCT
                    , EventAction.CREATE
                    , deduct.getDeductId()
                    , purchaseDeductVo.getCallerId()
            );
            chargeSystemEventBus.post(chargeSystemEvent);
            result = Optional.of(deduct);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return result;
    }
}
