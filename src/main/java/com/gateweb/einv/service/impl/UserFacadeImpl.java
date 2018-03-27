package com.gateweb.einv.service.impl;

import java.util.Collection;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.gateweb.einv.model.User;
import com.gateweb.einv.service.EinvFacade;
import com.gateweb.einv.service.UserFacade;


@Service("userFacade")
public class UserFacadeImpl implements UserFacade {
	
	@Autowired
	EinvFacade einvfacade;
	
	protected Logger logger = LogManager.getLogger(this.getClass());
	
	@Override
	public User getUserByLogin(String account) {
		System.out.println("getUserByLogin account:"+account);
		System.out.println("getUserByLogin TESTONE");
		if (null == account) {
			throw new IllegalArgumentException("Login is mandatory. Null value is forbidden.");
		}
		User user = new User();
		user.setAccount(account);
		List<User> usersList = einvfacade.searchBy(user);
		if (usersList != null && usersList.size()>0){
			//System.out.println("usersList.get(0):"+usersList.get(0));
			user = new User();
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
		User user = null;
		System.out.println("loadUserByUsername TESTONE");
		
		try {
			user = getUserByLogin(account);
			if (null == user) {
				logger.error("User with login: " + account+ " not found in database.");
				logger.error("Authentication failed for user " + account);
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
	public User getCurrentLoginUser() {
		User user = (User) RequestContextHolder.currentRequestAttributes().getAttribute("loginUser", RequestAttributes.SCOPE_SESSION);
		return user;
	}

	@Override
	public User findUserByEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User checkRepeatByUserEmail(String email) {
		return getUserByLogin(email);
	}

	@Override
	public User findUserByUid(Integer uid) {
		return findUserByUid(uid);
	}
	
	public User findUserByUid(Long uid) {
		User user = einvfacade.findUserById(uid);
		return user;
	}

}
