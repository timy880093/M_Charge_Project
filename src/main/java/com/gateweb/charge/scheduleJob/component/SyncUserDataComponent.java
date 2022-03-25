package com.gateweb.charge.scheduleJob.component;

import com.gateweb.orm.charge.entity.ChargeUser;
import com.gateweb.orm.charge.entity.Company;
import com.gateweb.orm.charge.entity.PrinterEntity;
import com.gateweb.orm.charge.repository.ChargeUserRepository;
import com.gateweb.orm.charge.repository.CompanyRepository;
import com.gateweb.orm.charge.repository.PrinterRepository;
import com.gateweb.orm.einv.entity.User;
import com.gateweb.orm.einv.repository.EinvUserRepository;
import com.google.gson.Gson;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

/**
 * Created by Eason on 3/9/2018.
 */
@Service
public class SyncUserDataComponent {
    protected static final Logger logger = LoggerFactory.getLogger(SyncUserDataComponent.class);
    Gson gson = new Gson();

    @Autowired
    ChargeUserRepository userRepository;
    @Autowired
    EinvUserRepository einvUserRepository;
    @Autowired
    PrinterRepository printerRepository;
    @Autowired
    CompanyRepository companyRepository;

    public void syncUserDataFromEinvDatabase() {
        List<User> einvUserList = einvUserRepository.findAll();

        for (User user : einvUserList) {
            try {
                ChargeUser existsUserEntity = userRepository.findByUserId(user.getUserId().intValue());
                if (existsUserEntity != null) {
                    transactionUpdateUserDataFromEinvDatabase(existsUserEntity, user);
                } else {
                    transactionDetachPrinterFromChargeDatabaseByAccount(user.getAccount());
                    transactionInsertUserDataFromEinvDatabase(user);
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
                logger.error(gson.toJson(user));
            }
        }
    }

    public void transactionDetachPrinterFromChargeDatabaseByAccount(String account) {
        Optional<ChargeUser> existsAccountEntityOpt = userRepository.findByAccount(account);
        if (existsAccountEntityOpt.isPresent()) {
            transactionDetachPrinter(existsAccountEntityOpt.get());
            userRepository.delete(existsAccountEntityOpt.get());
        }
    }

    public void transactionDetachPrinter(ChargeUser chargeUser) {
        List<PrinterEntity> printerEntityList = printerRepository.findByCreatorId(chargeUser.getUserId().intValue());
        printerEntityList.addAll(printerRepository.findByModifierId(chargeUser.getUserId().intValue()));
        printerEntityList.stream().forEach(printerEntity -> {
            List<ChargeUser> printerRelatedUserList = userRepository.findByPrinterId(printerEntity.getPrinterId());
            printerRelatedUserList.stream().forEach(printerRelatedUser -> {
                printerRelatedUser.setPrinterId(null);
                userRepository.save(printerRelatedUser);
            });
            printerRepository.delete(printerEntity);
        });
    }

    public void transactionUpdateUserDataFromEinvDatabase(ChargeUser existsUserEntity, User einvUserEntity) throws InvocationTargetException, IllegalAccessException {
        BeanUtils.copyProperties(existsUserEntity, einvUserEntity);
        companyModifier(existsUserEntity);
        printerModifier(existsUserEntity);
        modifierChange(existsUserEntity);
        logger.debug("Update User :" + existsUserEntity.getUserId());
        userRepository.save(existsUserEntity);
    }

    public void transactionInsertUserDataFromEinvDatabase(User einvUserEntity) throws InvocationTargetException, IllegalAccessException {
        ChargeUser newUserEntity = new ChargeUser();
        BeanUtils.copyProperties(newUserEntity, einvUserEntity);
        companyModifier(newUserEntity);
        printerModifier(newUserEntity);
        modifierChange(newUserEntity);
        duplicateCheck(newUserEntity);
        logger.debug("Insert User : {}", newUserEntity.getUserId());
        userRepository.save(newUserEntity);
    }

    public void companyModifier(ChargeUser newUserEntity) {
        if (newUserEntity.getCompanyId() != null) {
            Optional<Company> companyOptional = companyRepository.findById(newUserEntity.getCompanyId().intValue());
            if (!companyOptional.isPresent()) {
                newUserEntity.setCompanyId(null);
            }
        }
    }

    public void printerModifier(ChargeUser newUserEntity) {
        if (newUserEntity.getPrinterId() != null) {
            //檢查印表機存不存在
            Optional<PrinterEntity> printerOptional = printerRepository.findById(newUserEntity.getPrinterId().intValue());
            //過程中我們並不關心他的印表機是誰。
            if (!printerOptional.isPresent()) {
                newUserEntity.setPrinterId(null);
            }
        }
    }

    public void modifierChange(ChargeUser newUserEntity) {
        if (newUserEntity.getModifierId() != null) {
            Optional<ChargeUser> chargeUserOptional = userRepository.findById(newUserEntity.getUserId());
            if (!chargeUserOptional.isPresent()) {
                newUserEntity.setModifierId(null);
            }
        }
    }

    /**
     * 我們不關心printer是什麼，有衝突就刪掉
     *
     * @param newUserEntity
     */
    public void duplicateCheck(ChargeUser newUserEntity) {
        if (newUserEntity.getPrinterId() != null) {
            List<PrinterEntity> printerEntityList = printerRepository.findByCreatorId(newUserEntity.getUserId());
            if (!printerEntityList.isEmpty()) {
                printerRepository.delete(printerEntityList.get(0));
                logger.info("delete object: {}", gson.toJson(printerEntityList.get(0)));
            }
        }
    }

}
