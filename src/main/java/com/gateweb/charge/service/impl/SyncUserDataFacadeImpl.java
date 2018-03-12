package com.gateweb.charge.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gateweb.charge.model.UserEntity;
import com.gateweb.charge.repository.UserRepository;
import com.gateweb.charge.service.SyncUserDataFacade;
import com.gateweb.einv.model.User;
import com.gateweb.einv.repository.EinvUserRepository;

/**
 * Created by Eason on 3/9/2018.
 */
@Service
public class SyncUserDataFacadeImpl implements SyncUserDataFacade {
    protected static final Logger logger = LogManager.getLogger(SyncUserDataFacadeImpl.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    EinvUserRepository einvUserRepository;

    @Override
    public void syncUserDataFromEinvDatabase() throws InvocationTargetException, IllegalAccessException {
        List<User> einvUserList = einvUserRepository.findAll();

        for(User user : einvUserList){
            UserEntity existsUserEntity = userRepository.findByUserId(user.getUserId());
            if(existsUserEntity!=null){
                transactionUpdateUserDataFromEinvDatabase(existsUserEntity,user);
            }else{
                transactionInsertUserDataFromEinvDatabase(user);
            }
        }
    }

    public void transactionUpdateUserDataFromEinvDatabase(UserEntity existsUserEntity, User einvUserEntity) throws InvocationTargetException, IllegalAccessException {
        BeanUtils.copyProperties(existsUserEntity,einvUserEntity);
        logger.info("Update User :" + existsUserEntity.getUserId());
        userRepository.save(existsUserEntity);
    }

    public void transactionInsertUserDataFromEinvDatabase(User einvUserEntity) throws InvocationTargetException, IllegalAccessException {
        UserEntity newUserEntity = new UserEntity();
        BeanUtils.copyProperties(newUserEntity,einvUserEntity);
        logger.info("Insert User :" + newUserEntity.getUserId());
        userRepository.save(newUserEntity);
    }


}
