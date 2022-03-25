package com.gateweb.charge.service.impl;

import com.gateweb.utils.TimeUtils;
import com.gateweb.bridge.service.ChargeSourceService;
import com.gateweb.charge.chargePolicy.grade.service.GradeService;
import com.gateweb.charge.config.BillingSystemMailSender;
import com.gateweb.charge.service.MailService;
import com.gateweb.orm.charge.entity.BillingItem;
import com.gateweb.orm.charge.entity.PackageRef;
import com.gateweb.orm.charge.repository.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;

@Deprecated
@Service
public class MailServiceImpl implements MailService {
    protected final Logger logger = LogManager.getLogger(getClass());

    @Autowired
    CashDetailRepository cashDetailRepository;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    ChargeModeCycleRepository chargeModeCycleRepository;
    @Autowired
    ChargeModeGradeRepository chargeModeGradeRepository;
    @Autowired
    ChargeModeCycleAddRepository chargeModeCycleAddRepository;
    @Autowired
    CashMasterRepository cashMasterRepository;
    @Autowired
    BillCycleRepository billCycleRepository;
    @Autowired
    GradeService gradeService;
    @Autowired
    TimeUtils timeUtils;
    @Autowired(required = false)
    ServletContext servletContext;
    @Autowired
    PackageRefRepository packageRefRepository;
    @Autowired
    ChargeRuleRepository chargeRuleRepository;
    @Autowired
    ProductCategoryRepository productCategoryRepository;
    @Autowired
    ChargeSourceService chargeSourceService;
    @Autowired
    BillingSystemMailSender billingSystemMailSender;

    private static final String TEMPLATE = "collectionReminderMail.ftl";
    private static final String PAYMENT_REQUEST_MAIL_TEMPLATE = "paymentRequestMail.ftl";
    private static final String SCSB_PAY_TUTORIAL = "WEB-INF/classes/template/scsbPayBillTutorial.pdf";
    private static final String CTBC_PAY_ACCOUNT = "WEB-INF/classes/template/gwCtbcBankAccount.pdf";

    @Override
    public String parseCashType(Integer cashType) throws Exception {
        //1.月租2.月租超額3.代印代計4.加值型服務5.儲值
        String str = "";
        switch (cashType) {
            case 1:
                str = "月租型預繳";
                break;
            case 2:
                str = "超額費用";
                break;
            case 6:
                str = "扣抵預繳";
                break;
            case 7:
                str = "扣抵費用";
                break;
            default:
                str = "請洽客服人員";
                break;
        }
        return str;
    }

    public HashMap<Long, PackageRef> genPackageRefIdMap(Set<BillingItem> billingItemSet) {
        HashMap<Long, PackageRef> packageRefHashMap = new HashMap<>();
        billingItemSet.stream().forEach(billingItem -> {
            if (billingItem.getPackageRefId() != null) {
                Optional<PackageRef> packageRefOptional = packageRefRepository.findById(billingItem.getPackageRefId());
                if (packageRefOptional.isPresent()) {
                    packageRefHashMap.put(billingItem.getPackageRefId(), packageRefOptional.get());
                }
            }
        });
        return packageRefHashMap;
    }

}
