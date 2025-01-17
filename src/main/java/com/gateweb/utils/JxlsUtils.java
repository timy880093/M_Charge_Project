package com.gateweb.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jxls.area.Area;
import org.jxls.builder.AreaBuilder;
import org.jxls.builder.xml.XmlAreaBuilder;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.transform.Transformer;
import org.jxls.transform.poi.PoiTransformer;
import org.jxls.util.JxlsHelper;
import org.jxls.util.TransformerFactory;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * Created by Eason on 3/19/2018.
 */
public class JxlsUtils {
    private static final Logger logger = LogManager.getLogger(JxlsUtils.class);

    @Deprecated
    public void processTemplate(Map<String, Object> parameterMap, String templateFilePath, String outputFilePath) throws FileNotFoundException {
        try (InputStream templateFileInputStream = new FileInputStream(templateFilePath)) {
            OutputStream outputFileOutputStream = new FileOutputStream(outputFilePath);
            processTemplate(parameterMap, templateFileInputStream, outputFileOutputStream);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public static void processTemplate(Map<String, Object> parameterMap, InputStream inputStream, OutputStream outputStream) {
        try {
            Context context = new Context();
            for (String key : parameterMap.keySet()) {
                context.putVar(key, parameterMap.get(key));
            }
            JxlsHelper.getInstance().processTemplate(inputStream, outputStream, context);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public static void processTemplate(
            Map<String, Object> parameterMap
            , InputStream templateInputStream
            , OutputStream resultOutputStream
            , InputStream configXmlInputStream
            , CellRef cellRef) {
        try {
            Transformer transformer = TransformerFactory.createTransformer(templateInputStream, resultOutputStream);
            AreaBuilder areaBuilder = new XmlAreaBuilder(configXmlInputStream, transformer);
            List<Area> xlsAreaList = areaBuilder.build();
            Area xlsArea = xlsAreaList.get(0); //create new method, if you want customize this variable.
            Context context = PoiTransformer.createInitialContext();
            for (String key : parameterMap.keySet()) {
                context.putVar(key, parameterMap.get(key));
            }
            xlsArea.applyAt(cellRef, context);
            transformer.write();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
