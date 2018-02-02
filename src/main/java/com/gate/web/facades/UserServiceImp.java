package com.gate.web.facades;

import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gate.web.beans.QuerySettingVO;
import com.gate.web.displaybeans.UserVO;
import com.gate.web.formbeans.UserBean;
import com.gateweb.charge.model.UserEntity;

import dao.UserDAO;

/**
 * Created by simon on 2014/7/11.
 */
@Service("userService")
public class UserServiceImp implements UserService{
	
	@Autowired
    UserDAO userDAO;

    @Override
    public Integer insertUser(UserBean userBean) throws Exception {
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userEntity, userBean);
        userDAO.saveEntity(userEntity);
        return null;
    }

    @Override
    public void updateUser(UserBean userBean) throws Exception {
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userEntity, userBean);
        userDAO.updateEntity(userEntity, userEntity.getUserId());
    }

    @Override
    public void deleteUser(Integer userId) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public UserVO findUserByUserId(Integer userId) throws Exception {
        UserEntity userEntity = (UserEntity) userDAO.getEntity(UserEntity.class,userId);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userVO, userEntity);
        return userVO;
    }

    @Override
    public Map getUserList(QuerySettingVO querySettingVO, Map otherMap) throws Exception {
        Integer companyId = null;
        Map returnMap = userDAO.getUserList(querySettingVO, companyId);
        return returnMap;
    }

    public Boolean checkAccount(String account,String userId) throws Exception {
        return userDAO.checkAccount(account, userId);
    }

}
