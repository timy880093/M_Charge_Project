package com.gate.utils;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class NullConstants {
	public static String STRING_NULL="STRING_NULL";
	public static Integer INTEGER_NULL=new Integer(-99999999);
	public static Double DOUBLE_NULL=new Double(-99999999.999999);
	public static Float FLOAT_NULL=new Float(-99999999.999999);
	public static Timestamp TIMESTAMP_NULL= TimeUtils.string2Timestamp("1800/01/01");

	public static BigDecimal getNotNull(BigDecimal val){
		if(null == val){
			return new BigDecimal(0);
		}
		return val;
	}

	public static Integer getNotNull(Integer val){
		if(null == val){
			return new Integer(0);
		}
		return val;
	}

	public static Double getNotNull(Double val){
		if(null == val){
			return new Double(0);
		}
		return val;
	}
}
