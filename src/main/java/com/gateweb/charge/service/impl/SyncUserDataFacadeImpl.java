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
    public void transactionSyncUserDataFromEinvDatabase() throws InvocationTargetException, IllegalAccessException {
        List<User> einvUserList = einvUserRepository.findAll();

        for(User user : einvUserList){
            UserEntity existsUserEntity = userRepository.findByUserId(user.getUserId());
            if(existsUserEntity!=null){
                BeanUtils.copyProperties(existsUserEntity,user);
                logger.info("Update User :" + existsUserEntity.getUserId());
            }else{
                existsUserEntity = new UserEntity();
                BeanUtils.copyProperties(existsUserEntity,user);
                logger.info("Insert User :" + user.getUserId());
            }
        }
    }
}
