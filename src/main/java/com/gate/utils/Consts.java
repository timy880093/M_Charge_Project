/**
 * 
 */
package com.gate.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mac
 *
 */
public class Consts {
	
	public static String convRoleIdToSecurityId(Integer roleId) {
        Map<Long, String> roleMap = new HashMap<Long, String>();
        roleMap.put(0L, "ROLE_USER"); //一般使用者
        roleMap.put(100L, "ROLE_ADMIN");//管理者
        roleMap.put(200L, "ROLE_COMPANY_ADMIN"); //一般公司管理者
        roleMap.put(201L, "ROLE_COMPANY_ACCOUNTER");  //一般公司會計者
        roleMap.put(202L, "ROLE_COMPANY_USER"); //一般公司使用者
        roleMap.put(203L, "ROLE_COMPANY_EINV"); //一般公司開立發票人員
        roleMap.put(250L, "ROLE_COMPANY_LOTTERY"); //一般公司中獎清冊使用者
        roleMap.put(300L, "ROLE_FIRM_USER"); //會計公司使用者
        roleMap.put(400L, "ROLE_POS_USER"); //POS公司使用者
        roleMap.put(500L, "ROLE_SYNCAGENT_USER"); //一般介接公司使用者
        roleMap.put(550L, "ROLE_SYNCAGENT_LOTTERY"); //一般介接公司中獎清冊使用者
        roleMap.put(600L, "ROLE_ROOT"); //帳務管理者
        roleMap.put(1000L,"ROLE_STOP"); //停用
        roleMap.put(1001L,"ROLE_PASSWORD_ERROR"); //密碼錯誤
        //System.out.println("roleId = " + roleId);
        return (String) roleMap.get(new Long(roleId));
    }
	
	public static String convRoleIdToSecurityName(Integer roleId) {
        Map<Long, String> roleMap = new HashMap<Long, String>();
        roleMap.put(0L, "一般使用者"); //一般使用者
        roleMap.put(100L, "管理者");//管理者
        roleMap.put(200L, "一般公司管理者"); //一般公司管理者
        roleMap.put(201L, "一般公司會計者");  //一般公司會計者
        roleMap.put(202L, "一般公司使用者"); //一般公司使用者
        roleMap.put(203L, "一般公司開立發票人員"); //一般公司開立發票人員
        roleMap.put(250L, "一般公司中獎清冊使用者"); //一般公司中獎清冊使用者
        roleMap.put(300L, "會計公司使用者"); //會計公司使用者
        roleMap.put(400L, "POS公司使用者"); //POS公司使用者
        roleMap.put(500L, "一般介接公司使用者"); //一般介接公司使用者
        roleMap.put(550L, "一般介接公司中獎清冊使用者"); //一般介接公司中獎清冊使用者
        roleMap.put(600L, "帳務管理者"); //帳務管理者
        roleMap.put(1000L,"停用"); //停用
        roleMap.put(1001L,"密碼錯誤"); //密碼錯誤
        //System.out.println("roleId = " + roleId);
        return (String) roleMap.get(new Long(roleId));
    }
	
	

}
