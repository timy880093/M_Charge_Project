package com.gate.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Map;

/**
 * Created by Eason on 3/19/2018.
 */
@Component
public class JxlsUtils {
    protected static final Logger logger = LogManager.getLogger(JxlsUtils.class);

    public void processTemplate(Map<String,Object> parameterMap, String templateFilePath,String outputFilePath) throws FileNotFoundException {
        InputStream templateFileInputStream  = new FileInputStream(templateFilePath);
        OutputStream outputFileOutputStream = new FileOutputStream(outputFilePath);
        processTemplate(parameterMap,templateFileInputStream,outputFileOutputStream);
    }

    public void processTemplate(Map<String,Object> parameterMap, InputStream inputStream,OutputStream outputStream){
        try {
            Context context = new Context();
            for(String key: parameterMap.keySet()){
                context.putVar(key,parameterMap.get(key));
            }
            JxlsHelper.getInstance().processTemplate(inputStream, outputStream, context);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
