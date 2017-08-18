package com.gate.web.servlets.backend.common;

import com.gate.config.SystemConfig;
import com.gate.utils.FileUtils;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;


public class UploadServlet extends HttpServlet {

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String tempFolder = SystemConfig.getInstance().getParameter("uploadTempPath");
        try {
//            Part _oPart = req.getPart("FILE");
            String oriFilenames= "";
            String fileNames="";
            // _sFilename = new String(_sFilename.getBytes("ISO8859_1"),"UTF-8");  // 在不同的code page啟動AP時有時需要轉碼
            for(Part part : req.getParts()) {
                if("FILE".equals(part.getName())) {
                    String oriFilename = getFilename(part);  // 檔名由Header取出
                    String[] outMessage =  writeFile(resp, tempFolder, part, oriFilename);
                    oriFilenames = oriFilenames.concat(outMessage[0]+",");
                    fileNames = fileNames.concat(outMessage[1]+",");
                }
            }
            oriFilenames = oriFilenames.substring(0,oriFilenames.length()-1);
            fileNames = fileNames.substring(0,fileNames.length()-1);
            Gson gson = new Gson();
            HashMap map = new HashMap();
            map.put("oriFilename",oriFilenames);
            map.put("fileName",fileNames);

            resp.getWriter().print(gson.toJson(map));

        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().print(e.getMessage());
        } finally {
        }
    }

    private String[] writeFile(HttpServletResponse resp, String tempFolder, Part _oPart, String oriFilename) throws IOException {
        String fileName = UUID.randomUUID().toString()+ FileUtils.getSuffixName(oriFilename);
        String filepath = tempFolder + File.separator + fileName;
        File _oFile = new File(filepath);
//            if (_oFile.exists()) _oFile.delete();  // 檔案已存在時先刪除
        _oPart.write(fileName);  // saving the uploaded file.
        System.out.println("Uploaded filename=" + oriFilename + "-filepath="+filepath);  // debug
        String[] outPath = new String[2];
        outPath[0]=oriFilename;
        outPath[1]= fileName;
        return outPath;

    }

    private static String getFilename(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                String filename = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                return filename.substring(filename.lastIndexOf('/') + 1).substring(filename.lastIndexOf('\\') + 1); // MSIE fix.
            }
        }
        return null;
    }
}
