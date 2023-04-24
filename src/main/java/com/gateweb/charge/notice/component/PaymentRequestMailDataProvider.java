package com.gateweb.charge.notice.component;

import com.gateweb.charge.infrastructure.propertyProvider.ObankPropertyProvider;
import com.gateweb.charge.notice.bean.NoticeCustom;
import com.gateweb.charge.notice.bean.OBank;
import com.gateweb.charge.notice.bean.PaymentRequestMailData;
import com.gateweb.orm.charge.entity.Bill;
import com.gateweb.orm.charge.entity.Company;
import com.gateweb.orm.charge.entity.Notice;
import com.gateweb.orm.charge.repository.BillRepository;
import com.gateweb.orm.charge.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

import java.util.Optional;

import static com.gateweb.charge.notice.utils.CompanyRecipientUtils.noticeCompanyRecipientDecode;

@Component
public class PaymentRequestMailDataProvider {
    @Autowired
    BillRepository billRepository;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    NoticeCustomConverter noticeCustomConverter;
    @Autowired
    ObankPropertyProvider obankPropertyProvider;

    public Optional<PaymentRequestMailData> getPaymentRequestMailData(Notice notice) {
            Optional<PaymentRequestMailData> result = Optional.empty();
        Optional<Bill> billOptional = billRepository.findById(notice.getBillId());
        if (!billOptional.isPresent()) {
            return result;
        }
        Optional<Company> companyOptional = companyRepository.findById(billOptional.get().getCompanyId().intValue());
        if (!companyOptional.isPresent()) {
            return result;
        }
        if (StringUtils.isEmpty(notice.getRecipient())) {
            return result;
        }
        Optional<NoticeCustom> noticeCustomOptional = noticeCustomConverter.fromStr(notice.getCustomJson());
        return Optional.of(new PaymentRequestMailData(
                notice.getNoticeType()
                , billOptional.get()
                , companyOptional.get()
                , noticeCustomOptional
                , obankPropertyProvider.getProperty()
                , noticeCompanyRecipientDecode(notice.getRecipient())
        ));
    }
}
