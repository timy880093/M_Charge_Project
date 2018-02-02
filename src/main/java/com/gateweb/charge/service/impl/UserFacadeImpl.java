package com.gateweb.charge.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.gateweb.charge.model.UserEntity;
import com.gateweb.charge.service.ChargeFacade;
import com.gateweb.charge.service.UserFacade;


@Service("chargeUserFacade")
public class UserFacadeImpl implements UserFacade {
	
	@Autowired
	ChargeFacade chargeFacade;
	
	protected Log log = LogFactory.getLog(this.getClass());
	
	@Override
	public UserEntity getUserByLogin(String account) {
		System.out.println("getUserByLogin account:"+account);
		if (null == account) {
			throw new IllegalArgumentException("Login is mandatory. Null value is forbidden.");
		}
		UserEntity user = new UserEntity();
		user.setAccount(account);
		List<UserEntity> usersList = chargeFacade.searchBy(user);
		if (usersList != null && usersList.size()>0){
			//System.out.println("usersList.get(0):"+usersList.get(0));
			user = new UserEntity();
			BeanUtils.copyProperties(usersList.get(0), user);
			user.setGrantedAuthorities( user.getAuthorities().toArray(new GrantedAuthority[] {}));
		}else{
			System.out.println("No user");
			user = null;
		}
		System.out.println("user:"+user);
		return user;
		
	}

	/**
	 * load user by user name
	 * @param userName The param
	 * @throws UsernameNotFoundException The ExceptionThorw
	 * @throws DataAccessException The ExceptionThorw
	 * @return UserDetails
	 */
	@Override
	public UserDetails loadUserByUsername(String account)
			throws UsernameNotFoundException, DataAccessException {
		System.out.println("loadUserByUsername account:"+account);
		UserEntity user = null;		
		try {
			user = getUserByLogin(account);
			if (null == user) {
				log.error("User with login: " + account+ " not found in database.");
				log.error("Authentication failed for user " + account);
				throw new UsernameNotFoundException("Username not found");
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			
		}
		//return user;
		return new org.springframework.security.core.userdetails.User(user.getAccount(), user.getPassword(), 
				true, true, true, true, user.getAuthorities());
	}
	
	@Override
	public UserEntity getCurrentLoginUser() {
		UserEntity user = (UserEntity) RequestContextHolder.currentRequestAttributes().getAttribute("loginUser", RequestAttributes.SCOPE_SESSION);
		return user;
	}

	@Override
	public UserEntity findUserByEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserEntity checkRepeatByUserEmail(String email) {
		return getUserByLogin(email);
	}

	@Override
	public UserEntity findUserByUid(Integer uid) {
		return findUserByUid(uid);
	}
	
	public UserEntity findUserByUid(Long uid) {
		UserEntity user = chargeFacade.findUserById(uid.intValue());
		return user;
	}

}
