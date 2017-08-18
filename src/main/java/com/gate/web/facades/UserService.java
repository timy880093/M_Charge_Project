package com.gate.web.facades;

import com.gate.web.beans.QuerySettingVO;
import com.gate.web.displaybeans.UserVO;
import com.gate.web.formbeans.UserBean;

import java.util.Map;

/**
 * Created by simon on 2014/7/11.
 */
public interface UserService extends Service {
    public Integer insertUser(UserBean memberBean) throws Exception;
    public void updateUser(UserBean memberBean) throws Exception;
    public void deleteUser(Integer memberId);
    public UserVO findUserByUserId(Integer memberId) throws Exception;
    public Map getUserList(QuerySettingVO querySettingVO, Map otherMap) throws Exception;
    public Boolean checkAccount(String account, String userId) throws Exception;
}
