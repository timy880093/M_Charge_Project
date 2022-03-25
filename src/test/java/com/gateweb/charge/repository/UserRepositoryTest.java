package com.gateweb.charge.repository;

import com.gateweb.charge.config.SpringWebMvcConfig;
import com.gateweb.orm.charge.entity.ChargeUser;
import com.gateweb.orm.charge.repository.ChargeUserRepository;
import com.gateweb.orm.einv.entity.User;
import com.gateweb.orm.einv.repository.EinvUserRepository;
import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by Eason on 3/9/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringWebMvcConfig.class})
@WebAppConfiguration
public class UserRepositoryTest {
    @Autowired
    ChargeUserRepository chargeUserRepository;

    @Autowired
    EinvUserRepository einvUserRepository;

    @Test
    public void getUserEntityById() {
        List<User> userList = einvUserRepository.findAll();
        for (User user : userList) {
            ChargeUser userEntity = chargeUserRepository.findByUserId(user.getUserId().intValue());
            if (userEntity != null) {
                System.out.println(userEntity.getName());
            } else {
                System.out.println("user doesn't exists");
            }
        }
    }

    @Test
    public void separatedSyncUserMethod() throws InvocationTargetException, IllegalAccessException {
        List<User> einvUserList = einvUserRepository.findAll();

        for (User user : einvUserList) {
            ChargeUser existsUserEntity = chargeUserRepository.findByUserId(user.getUserId().intValue());
            if (existsUserEntity != null) {
                BeanUtils.copyProperties(existsUserEntity, user);
            } else {
                existsUserEntity = new ChargeUser();
                BeanUtils.copyProperties(existsUserEntity, user);
            }
            chargeUserRepository.save(existsUserEntity);
        }
    }
}
