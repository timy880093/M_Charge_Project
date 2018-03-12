package com.gateweb.charge.repository;

import com.gateweb.charge.model.UserEntity;
import com.gateweb.charge.service.SyncCompanyDataFacade;
import com.gateweb.einv.model.User;
import com.gateweb.einv.repository.EinvUserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by Eason on 3/9/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:com/gateweb/einv/spring/spring_web.xml"})
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    EinvUserRepository einvUserRepository;

    @Autowired
    SyncCompanyDataFacade syncCompanyDataFacade;

    @Test
    public void getUserEntityById(){
        List<User> userList = einvUserRepository.findAll();
        for(User user: userList){
            UserEntity userEntity = userRepository.findByUserId(user.getUserId());
            if(userEntity!=null){
                System.out.println(userEntity.getName());
            }else{
                System.out.println("user doesn't exists");
            }
        }
    }

    @Test
    public void syncUserDataTest() throws InvocationTargetException, IllegalAccessException {
        syncCompanyDataFacade.transactionSyncCompanyDataFromEinvDatabase();
    }
}
