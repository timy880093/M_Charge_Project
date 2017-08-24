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

import com.gateweb.charge.service.ChargeFacade;
import com.gateweb.charge.model.User;
import com.gateweb.charge.service.UserFacade;


@Service("chargeUserFacade")
public class UserFacadeImpl implements UserFacade {
	
	@Autowired
	ChargeFacade chargeFacade;
	
	protected Log log = LogFactory.getLog(this.getClass());
	
	@Override
	public User getUserByLogin(String email) {
		System.out.println("getUserByLogin email:"+email);
		System.out.println("getUserByLogin TESTONE");
		if (null == email) {
			throw new IllegalArgumentException("Login is mandatory. Null value is forbidden.");
		}
		User user = new User();
		user.setEmail(email);
		List<User> usersList = chargeFacade.searchBy(user);
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
	public UserDetails loadUserByUsername(String email)
			throws UsernameNotFoundException, DataAccessException {
		System.out.println("loadUserByUsername email:"+email);
		User user = null;
		System.out.println("loadUserByUsername TESTONE");
		
		try {
			user = getUserByLogin(email);
			if (null == user) {
				log.error("User with login: " + email+ " not found in database.");
				log.error("Authentication failed for user " + email);
				throw new UsernameNotFoundException("Username not found");
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			
		}
		//return user;
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), 
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
		User user = chargeFacade.findUserById(uid);
		return user;
	}

}
