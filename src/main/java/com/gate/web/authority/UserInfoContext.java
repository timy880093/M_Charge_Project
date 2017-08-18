package com.gate.web.authority;

public class UserInfoContext {

	public static final ThreadLocal<UserInfo> infoLocal = new ThreadLocal<UserInfo>();

	public static void setUserInfo(UserInfo info) {
        infoLocal.set(info);
	}

	public static UserInfo getUserInfo() {
		return infoLocal.get();
	}
	
	public static void clean(){
		infoLocal.set(null);
	}
}
