package com.gate.core.utils;

import org.apache.commons.beanutils.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created with IntelliJ IDEA.
 * User: good504
 * Date: 2014/7/17
 * Time: 上午 10:55
 * To change this template use File | Settings | File Templates.
 */
public class DateConvert implements Converter {

    public Object convert(Class arg0, Object arg1) {
        String p = (String) arg1;
        if (p == null || p.trim().length() == 0) {
            return null;
        }
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            return df.parse(p.trim());
        } catch (Exception e) {
            try {
                SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
                return df.parse(p.trim());
            } catch (ParseException ex) {
                return null;
            }
        }

    }

}
