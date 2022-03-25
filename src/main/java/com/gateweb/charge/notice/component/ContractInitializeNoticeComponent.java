package com.gateweb.charge.notice.component;

import com.gateweb.charge.config.BillingSystemMailSender;
import com.gateweb.charge.contract.component.ContractValidationComponent;
import com.gateweb.charge.notice.builder.MailMimeMessageBuilder;
import com.gateweb.orm.charge.entity.Company;
import com.gateweb.orm.charge.entity.Contract;
import com.gateweb.orm.charge.entity.Notice;
import com.gateweb.orm.charge.repository.CompanyRepository;
import com.gateweb.orm.charge.repository.ContractRepository;
import com.gateweb.orm.charge.repository.NoticeRepository;
import com.gateweb.utils.LocalDateTimeUtils;
import freemarker.template.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class ContractInitializeNoticeComponent implements NoticeMimeMessageHelperProvider {
    protected final Logger logger = LogManager.getLogger(getClass());
    private final String AUTOMATIC_CONTRACT_ENABLE_MAIL_TEMPLATE = "automaticContractEnableMail.ftl";
    @Resource
    Configuration freemarkerConfig;
    @Autowired
    BillingSystemMailSender billingSystemMailSender;
    @Autowired
    ContractValidationComponent contractValidationComponent;
    @Autowired
    ContractRepository contractRepository;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    NoticeRepository noticeRepository;
    @Autowired
    NoticeRequestGenerator noticeRequestGenerator;

    /**
     * 通知邏輯表
     * A: isFirstContract
     * B: needFulfillEffectiveDate
     * C: isChargeByRemainingCountContract
     * AB+C
     *
     * @param contract
     * @return
     */
    public boolean needToBeNoticeAfterInitialized(Contract contract) {
        boolean isAutoFulfillmentType = contractValidationComponent.isAutoFulfillmentTypeContract(contract);
        boolean isChargeByRemainingCountContract
                = contractRepository.findChargeByRemainingCountFlagByContractId(contract.getContractId());
        return isAutoFulfillmentType || isChargeByRemainingCountContract;
    }

    @Override
    public Optional<MimeMessageHelper> createHelper(Notice notice) {
        Optional<MimeMessageHelper> resultMimeMessageOpt = Optional.empty();
        if (notice.getContractId() != null) {
            Optional<Contract> contractOptional = contractRepository.findById(notice.getContractId());
            if (contractOptional.isPresent()) {
                Optional<Company> companyOptional = companyRepository.findById(contractOptional.get().getCompanyId().intValue());
                if (companyOptional.isPresent()) {
                    resultMimeMessageOpt = genContractInitialMail(companyOptional.get(), contractOptional.get());
                }
            }
        }
        return resultMimeMessageOpt;
    }

    public Optional<MimeMessageHelper> genContractInitialMail(Company company, Contract contract) {
        Optional<MimeMessageHelper> result = Optional.empty();
        try {
            HashMap<String, Object> dataMap = new HashMap<>();
            dataMap.put("companyName", company.getName());
            Optional<String> effectiveDateOpt = LocalDateTimeUtils.parseLocalDateTimeToString(
                    contract.getEffectiveDate()
                    , "yyyy/MM/dd"
            );
            if (effectiveDateOpt.isPresent()) {
                dataMap.put("effectiveDate", effectiveDateOpt.get());
            }
            Optional<String> expirationDateOpt = LocalDateTimeUtils.parseLocalDateTimeToString(
                    contract.getExpirationDate()
                    , "yyyy/MM/dd"
            );
            if (expirationDateOpt.isPresent()) {
                dataMap.put("expirationDate", expirationDateOpt.get());
            }

            Map<String, Object> freeMarkerTemplateMap = new HashMap<String, Object>();
            freeMarkerTemplateMap.put("data", dataMap);
            String resultString = FreeMarkerTemplateUtils.processTemplateIntoString(
                    freemarkerConfig.getTemplate(AUTOMATIC_CONTRACT_ENABLE_MAIL_TEMPLATE)
                    , freeMarkerTemplateMap
            );
            if (billingSystemMailSender.javaMailSenderOpt.isPresent()) {
                MailMimeMessageBuilder mailBuilder = new MailMimeMessageBuilder();
                MimeMessageHelper mimeMessageHelper = mailBuilder
                        .initBuilder(billingSystemMailSender.javaMailSenderOpt.get())
                        .withRecipientAndName("se01@gateweb.com.tw", "se01")
                        .withSubject("【合約啟用通知】關網電子發票服務合約啟用通知，請詳內文")
                        .withHtmlContent(resultString)
                        .getMimeHelper();
                result = Optional.of(mimeMessageHelper);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return result;
    }

    /**
     * 系統概念上，enable及initialize是一樣的
     *
     * @param contract
     */
    public void sendContractEnabledNoticeMail(final Contract contract, Long callerId) {
        Notice notice = noticeRequestGenerator.genContractEnabledMailNotice(contract, callerId);
        noticeRepository.save(notice);
    }

}
