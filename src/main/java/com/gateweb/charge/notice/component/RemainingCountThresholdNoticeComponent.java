package com.gateweb.charge.notice.component;

import com.gateweb.charge.chargePolicy.ChargePolicyProvider;
import com.gateweb.charge.chargePolicy.bean.ChargePolicy;
import com.gateweb.charge.config.BillingSystemMailSender;
import com.gateweb.charge.contract.component.ContractRenewComponent;
import com.gateweb.charge.contract.component.RemainingContractComponent;
import com.gateweb.charge.contract.component.RemainingCountAmountProvider;
import com.gateweb.charge.deduct.component.DeductibleItemFilterComponent;
import com.gateweb.charge.notice.bean.RemainingCountThresholdNoticeReportData;
import com.gateweb.charge.notice.builder.MailMimeMessageBuilder;
import com.gateweb.charge.report.ctbc.CtbcVirtualAccountGenerator;
import com.gateweb.charge.service.DeductService;
import com.gateweb.charge.service.dataGateway.ContractDataGateway;
import com.gateweb.orm.charge.entity.*;
import com.gateweb.orm.charge.repository.*;
import com.gateweb.utils.LocalDateTimeUtils;
import freemarker.template.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class RemainingCountThresholdNoticeComponent implements NoticeMimeMessageHelperProvider {
    private String REMAINING_COUNT_NOTICE_TEMPLATE = "remainingCountNoticeMail.ftl";
    protected final Logger logger = LogManager.getLogger(getClass());

    @Autowired
    public Configuration freemarkerConfiguration;
    @Autowired
    InvoiceRemainingRepository invoiceRemainingRepository;
    @Autowired
    NoticeRepository noticeRepository;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    BillingSystemMailSender billingSystemMailSender;
    @Autowired
    RemainingContractComponent remainingContractComponent;
    @Autowired
    CtbcVirtualAccountGenerator ctbcVirtualAccountGenerator;
    @Autowired
    ChargePolicyProvider chargePolicyProvider;
    @Autowired
    ChargePackageRepository chargePackageRepository;
    @Autowired
    ContractRenewComponent contractRenewComponent;
    @Autowired
    ChargeRuleRepository chargeRuleRepository;
    @Autowired
    DeductService deductService;
    @Autowired
    DeductibleItemFilterComponent deductibleItemFilterComponent;
    @Autowired
    ContractDataGateway contractDataGateway;
    @Autowired
    RemainingCountAmountProvider remainingCountAmountProvider;
    @Autowired
    NoticeRequestGenerator noticeRequestGenerator;

    @Override
    public Optional<MimeMessageHelper> createHelper(Notice notice) {
        Optional<MimeMessageHelper> mimeMessageHelperOptional = Optional.empty();
        try {
            Optional<RemainingCountThresholdNoticeReportData> remainingCountThresholdNoticeReportDataOpt
                    = genRemainingCountThresholdNoticeReportData(notice);
            if (remainingCountThresholdNoticeReportDataOpt.isPresent()) {
                Optional<String> mailContentOpt = generateEmailContent(remainingCountThresholdNoticeReportDataOpt.get());
                MailMimeMessageBuilder mailBuilder = new MailMimeMessageBuilder();
                if (mailContentOpt.isPresent()) {
                    MimeMessageHelper mimeMessageHelper = mailBuilder.initBuilder(billingSystemMailSender.javaMailSenderOpt.get())
                            .withRecipientList(remainingCountThresholdNoticeReportDataOpt.get().getRecipients())
                            .withHtmlContent(mailContentOpt.get())
                            .withSubject(
                                    remainingCountThresholdNoticeMailSubject(
                                            remainingCountThresholdNoticeReportDataOpt.get().getCompanyName()
                                    )
                            )
                            .getMimeHelper();
                    mimeMessageHelperOptional = Optional.of(mimeMessageHelper);
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return mimeMessageHelperOptional;
    }

    public void prepareNoticeIfNecessary(InvoiceRemaining newInvoiceRemaining, Contract contract) {
        Optional<Integer> remainingCountOpt = remainingCountAmountProvider.getRemainingCountFromContractId(
                newInvoiceRemaining.getContractId()
        );
        boolean haveUsableDeduct = false;
        Optional<BigDecimal> nextPrepaymentFeeOpt = getNextPrepaymentFee(contract);
        if (nextPrepaymentFeeOpt.isPresent()) {
            haveUsableDeduct = haveUsableDeduct(contract, nextPrepaymentFeeOpt.get());
        }
        if (remainingCountOpt.isPresent() && !haveUsableDeduct) {
            //TODO:預設為10%內
            int maxRemaining = remainingCountOpt.get();
            double threshold = newInvoiceRemaining.getRemaining().doubleValue() / maxRemaining;
            if (threshold < 0.1d) {
                Notice notice = noticeRequestGenerator.genRemainingCountInventoryAlertMailNotice(newInvoiceRemaining);
                noticeRepository.save(notice);
            }
        }
    }

    public Optional<RemainingCountThresholdNoticeReportData> genRemainingCountThresholdNoticeReportData(Notice notice) {
        Optional result = Optional.empty();
        Optional<InvoiceRemaining> invoiceRemainingOptional
                = invoiceRemainingRepository.findById(notice.getInvoiceRemainingId());
        if (invoiceRemainingOptional.isPresent()) {
            Optional<Company> companyOptional = companyRepository.findByCompanyId(
                    notice.getCompanyId().intValue()
            );
            Optional<String> dateTimeDescOpt = LocalDateTimeUtils.parseLocalDateTimeToString(
                    invoiceRemainingOptional.get().getCreateDate()
                    , "yyyy-MM-dd HH:mm:ss"
            );
            if (companyOptional.isPresent()) {
                Optional<String> ctbcVirtualAccountOpt =
                        ctbcVirtualAccountGenerator.getVirtualAccount(companyOptional.get().getBusinessNo());
                Optional<Contract> contractOptional = contractDataGateway.findByContractId(notice.getContractId());
                Optional<String> nextPackageNameOpt = Optional.empty();
                Optional<BigDecimal> nextPrepaymentFeeOpt = Optional.empty();
                if (contractOptional.isPresent()) {
                    nextPackageNameOpt = getNextRenewPackageName(contractOptional.get());
                    nextPrepaymentFeeOpt = getNextPrepaymentFee(contractOptional.get());
                }
                if (dateTimeDescOpt.isPresent()
                        && ctbcVirtualAccountOpt.isPresent()
                        && nextPackageNameOpt.isPresent()
                        && nextPrepaymentFeeOpt.isPresent()) {
                    String[] recipients = new String[]{companyOptional.get().getEmail1()};
                    BigDecimal paymentFeeWithTax = getPrepaymentFeeWithTax(nextPrepaymentFeeOpt.get())
                            .setScale(0, RoundingMode.HALF_UP).stripTrailingZeros();
                    RemainingCountThresholdNoticeReportData remainingCountThresholdNoticeReportData =
                            new RemainingCountThresholdNoticeReportData(
                                    companyOptional.get().getName()
                                    , invoiceRemainingOptional.get().getRemaining().intValue()
                                    , dateTimeDescOpt.get()
                                    , recipients
                                    , ctbcVirtualAccountGenerator.customFormatVirtualAccount(ctbcVirtualAccountOpt.get())
                                    , nextPackageNameOpt.get()
                                    , paymentFeeWithTax.toPlainString()
                            );
                    result = Optional.of(remainingCountThresholdNoticeReportData);
                }
            } else {
                logger.error("Invalid CompanyId:{}", notice.getCompanyId());
            }
        } else {
            logger.error("Invalid InvoiceRemainingId:{}", notice.getInvoiceRemainingId());
        }
        return result;
    }

    public Optional<String> generateEmailContent(RemainingCountThresholdNoticeReportData remainingCountThresholdNoticeReportData) {
        Optional<String> mailContent = Optional.empty();
        Map<String, Object> freeMarkerTemplateMap = new HashMap<String, Object>();
        freeMarkerTemplateMap.put("data", remainingCountThresholdNoticeReportData);
        try {
            String resultString = FreeMarkerTemplateUtils.processTemplateIntoString(
                    freemarkerConfiguration.getTemplate(REMAINING_COUNT_NOTICE_TEMPLATE)
                    , freeMarkerTemplateMap
            );
            mailContent = Optional.of(resultString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mailContent;
    }

    public String remainingCountThresholdNoticeMailSubject(String companyName) {
        String subject = String.format("【關網電子發票存量門檻通知】 %s_關網電子發票存量示警，請詳內文。", companyName);
        return subject;
    }

    public Optional<String> getNextRenewPackageName(Contract contract) {
        Optional<String> result = Optional.empty();
        try {
            Long nextPackageId = contractRenewComponent.getRenewPackageId(contract);
            Optional<ChargePackage> chargePackageOptional = chargePackageRepository.findById(nextPackageId);
            if (chargePackageOptional.isPresent()) {
                result = Optional.of(chargePackageOptional.get().getName());
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return result;
    }

    public Optional<BigDecimal> getNextPrepaymentFee(Contract contract) {
        Optional<BigDecimal> result = Optional.empty();
        try {
            BigDecimal prepaymentFeeTotal = BigDecimal.ZERO;
            Long nextPackageId = contractRenewComponent.getRenewPackageId(contract);
            Optional<ChargeRule> chargeRuleOptional = chargeRuleRepository.getChargeRuleByContractIdAndRemainingCountIsTrue(contract.getContractId());
            if (chargeRuleOptional.isPresent()) {
                Optional<ChargePolicy> chargePolicyOpt = chargePolicyProvider.genRentalChargePolicy(nextPackageId, chargeRuleOptional.get(), contract.getPeriodMonth());
                if (chargePolicyOpt.isPresent()) {
                    BigDecimal chargeFeeByFixPrice = chargePolicyOpt.get().getCalculateRule().calculateFee(
                            0
                            , chargePolicyOpt.get().getGradeTableLinkedList()
                    );
                    prepaymentFeeTotal = prepaymentFeeTotal.add(chargeFeeByFixPrice);
                    result = result.of(prepaymentFeeTotal);
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return result;
    }

    public BigDecimal getPrepaymentFeeWithTax(BigDecimal prepaymentFee) {
        BigDecimal result = BigDecimal.ZERO;
        BigDecimal taxAmount = prepaymentFee.multiply(BigDecimal.valueOf(0.05));
        return result.add(prepaymentFee).add(taxAmount);
    }


    public boolean haveUsableDeduct(Contract contract, BigDecimal prepaymentFee) {
        boolean result = false;
        Collection<Deduct> deductibleCollection = deductibleItemFilterComponent.findUsableDeductCollection(
                contract.getCompanyId()
                , contract.getContractId()
        );
        for (Deduct deduct : deductibleCollection) {
            if (deduct.getQuota().compareTo(prepaymentFee) > 0) {
                result = true;
                break;
            }
        }
        return result;
    }

}
