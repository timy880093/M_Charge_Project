package com.gateweb.charge.scheduleJob.component;

import com.gateweb.orm.charge.entity.BillingItem;
import com.gateweb.orm.charge.entity.ChargeUser;
import com.gateweb.orm.charge.entity.Company;
import com.gateweb.orm.charge.repository.BillingItemRepository;
import com.gateweb.orm.charge.repository.ChargeUserRepository;
import com.gateweb.orm.charge.repository.CompanyRepository;
import com.gateweb.orm.einv.repository.EinvCompanyRepository;
import com.google.gson.Gson;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by Eason on 3/9/2018.
 */
@Component
public class SyncCompanyDataComponent {
    protected static final Logger logger = LogManager.getLogger(SyncCompanyDataComponent.class);
    Gson gson = new Gson();

    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    EinvCompanyRepository einvCompanyRepository;
    @Autowired
    ChargeUserRepository userRepository;
    @Autowired
    BillingItemRepository billingItemRepository;

    /**
     * 用Spring的BeanUtils在Copy時，ID不會一併COPY，但apache的library沒有這個問題。
     *
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public void syncCompanyDataFromEinvDatabase() {
        Timestamp lastCompanyTableCreateDate = companyRepository.getLastCreateDate();
        //新增或修改
        List<com.gateweb.orm.einv.entity.Company> einvCompanyEntityList = einvCompanyRepository.findAll();
        List<Company> targetList = new ArrayList<>();

        einvCompanyEntityList.parallelStream().forEach(company -> {
            try {
                boolean isValid = true;
                Optional<Company> existsCompanyEntityOptional = companyRepository.findByCompanyId(company.getCompanyId().intValue());
                if (company.getParentId() != null) {
                    Optional<Company> existsParentEntityOptional = companyRepository.findByCompanyId(company.getParentId().intValue());
                    if (!existsParentEntityOptional.isPresent()) {
                        isValid = false;
                    }
                }
                if (isValid) {
                    if (existsCompanyEntityOptional.isPresent()) {
                        targetList.add(transactionUpdateCompanyDataFromEinvDatabase(existsCompanyEntityOptional.get(), company));
                    } else {
                        targetList.add(transactionInsertCompanyDataFromEinvDatabase(company));
                    }
                }
            } catch (Exception e) {
                logger.error("Process Error:{}", e.getMessage());
                logger.error("Timestamp:{}", lastCompanyTableCreateDate.getTime());
                logger.error(gson.toJson(company));
            }
        });
        targetList.stream().forEach(targetCompany -> {
            try {
                companyRepository.save(targetCompany);
            } catch (Exception e) {
                logger.error("Database Error:{}", e.getMessage());
                logger.error("Timestamp:{}", lastCompanyTableCreateDate.getTime());
                logger.error(gson.toJson(targetCompany));
            }
        });
    }

    public Company transactionUpdateCompanyDataFromEinvDatabase(Company existsCompanyEntity, com.gateweb.orm.einv.entity.Company einvCompanyEntity) throws InvocationTargetException, IllegalAccessException {
        BeanUtils.copyProperties(existsCompanyEntity, einvCompanyEntity);
        companyNameModifier(existsCompanyEntity);
        duplicateBusinessNoCheck(existsCompanyEntity);
        companyBannerModifier(existsCompanyEntity);
        ChargeUser existsUser = userRepository.findByUserId(existsCompanyEntity.getModifierId());
        if (existsUser == null) {
            existsCompanyEntity.setModifierId(existsCompanyEntity.getCreatorId());
        }
        logger.info("Update Company: {}", existsCompanyEntity.getCompanyId());
        return existsCompanyEntity;
    }

    public Company transactionInsertCompanyDataFromEinvDatabase(com.gateweb.orm.einv.entity.Company einvCompanyEntity) throws InvocationTargetException, IllegalAccessException {
        Company newCompanyEntity = new Company();
        BeanUtils.copyProperties(newCompanyEntity, einvCompanyEntity);
        companyNameModifier(newCompanyEntity);
        duplicateBusinessNoCheck(newCompanyEntity);
        companyBannerModifier(newCompanyEntity);
        ChargeUser existsUser = userRepository.findByUserId(newCompanyEntity.getModifierId());
        if (existsUser == null) {
            //為避免循環參照無法寫入，先暫時將modifier改為與creator一致，之後再update.
            newCompanyEntity.setModifierId(newCompanyEntity.getCreatorId());
        }
        logger.info("Insert Company :" + newCompanyEntity.getCompanyId());
        return newCompanyEntity;
    }

    public void companyNameModifier(Company newCompanyEntity) {
        newCompanyEntity.setName(newCompanyEntity.getName().replaceAll("\\s+", ""));
    }

    public void companyBannerModifier(Company newCompanyEntity) {
        if (Optional.ofNullable(newCompanyEntity.getTopBanner()).isPresent()) {
            newCompanyEntity.setTopBanner("");
        }
        if (Optional.ofNullable(newCompanyEntity.getBottomBanner()).isPresent()) {
            newCompanyEntity.setBottomBanner("");
        }
    }

    public void duplicateBusinessNoCheck(Company newCompanyEntity) {
        Optional<Company> duplicateCompanyOptional = companyRepository.findByBusinessNo(newCompanyEntity.getBusinessNo());

        if (duplicateCompanyOptional.isPresent() && !newCompanyEntity.getCompanyId().equals(duplicateCompanyOptional.get().getCompanyId())) {
            List<ChargeUser> chargeUserSet = userRepository.findByCompanyId(duplicateCompanyOptional.get().getCompanyId());
            Set<BillingItem> billingItemSet = new HashSet<>(
                    billingItemRepository.findByCompanyId(duplicateCompanyOptional.get().getCompanyId().longValue())
            );
            logger.error("Process Duplicate Company: {}", gson.toJson(newCompanyEntity));
            logger.error("Delete exists Company:{}", gson.toJson(duplicateCompanyOptional.get()));
            if (billingItemSet.isEmpty()) {
                chargeUserSet.stream().forEach(chargeUser -> {
                    userRepository.delete(chargeUser);
                });
                companyRepository.delete(duplicateCompanyOptional.get());
            }
        }
    }
}
