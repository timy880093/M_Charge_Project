package com.gate.utils;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class NullConstants {

	public static String STRING_NULL="STRING_NULL";
	public static Integer INTEGER_NULL=new Integer(-99999999);
	public static Double DOUBLE_NULL=new Double(-99999999.999999);
	public static Float FLOAT_NULL=new Float(-99999999.999999);

	public BigDecimal getNotNull(BigDecimal val){
		if(null == val){
			return new BigDecimal(0);
		}
		return val;
	}

	public Integer getNotNull(Integer val){
		if(null == val){
			return new Integer(0);
		}
		return val;
	}

	public Double getNotNull(Double val){
		if(null == val){
			return new Double(0);
		}
		return val;
	}
}
