package com.gate.web.servlets.backend.insertData;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gate.core.bean.BaseFormBean;
import com.gate.web.exceptions.FormValidationException;
import com.gate.web.exceptions.ReturnPathException;
import com.gate.web.servlets.MvcBaseServlet;
import com.gateweb.charge.model.PrepayDeductMasterEntity;
import com.gateweb.charge.model.UserEntity;
import com.gateweb.einv.exception.EinvSysException;
import com.google.gson.Gson;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;

import com.gate.config.SystemConfig;
import com.gate.web.beans.FirstCompanyPackageBean;
import com.gate.web.facades.FirstCompanyPackageService;
import com.gate.web.servlets.BaseServlet;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/backendAdmin/firstCompanyPackageAjaxServlet")
@Controller


public class firstCompanyPackageAjaxServlet extends MvcBaseServlet {

	@Autowired
    FirstCompanyPackageService firstCompanyPackageService;






    @RequestMapping(method = RequestMethod.GET, params = "sessionClean=Y", produces = "application/json;charset=utf-8")
    public String mainList(@RequestParam("sessionClean") String sessionClean, Model model, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String returnPage = TEMPLATE_PAGE;
        Gson gson = new Gson();
        String errorMessage = null;
        try{
            //寫入template的時間。
            UserEntity user = checkLogin(request, response);
            BaseFormBean formBeanObject = formBeanObject(request);
            Map otherMap = otherMap(request, response, formBeanObject);


            sendObjToViewer(request, otherMap);
        } catch (EinvSysException ese) {
            logger.error(ese.getMessage(), ese);
            errorMessage = gson.toJson("錯誤:" + ese.getMessage());
            returnPage = ERROR_PAGE;
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
            errorMessage = gson.toJson("IO動作失敗:" + ex.getMessage());
            request.setAttribute(ERROR_MESSAGE, errorMessage);
            returnPage = ERROR_PAGE;
        } catch (ServletException ex) {
            logger.error(ex.getMessage(), ex);
            errorMessage = gson.toJson("伺服器內部錯誤:" + ex.getMessage());
            returnPage = ERROR_PAGE;
        } catch (FormValidationException ex) {
            errorMessage = gson.toJson("表單驗證失敗:" + ex.getMessage());
            logger.error(ex.getMessage(), ex);
            returnPage = ERROR_PAGE;
        } catch (ReturnPathException ex) {
            errorMessage = gson.toJson("回傳路徑有問題:" + ex.getMessage());
            logger.error(ex.getMessage(), ex);
            returnPage = ERROR_PAGE;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            errorMessage = gson.toJson("錯誤:" + ex.getMessage());
            return ERROR_PAGE;
        }
        if (errorMessage != null) {
            request.setAttribute(ERROR_MESSAGE, errorMessage);
        }
        return returnPage;
    }

    @RequestMapping(method = RequestMethod.POST, params = "method=import1", produces = "application/json;charset=utf-8")
    public String import1(@RequestParam("method") String method, Model model
            , @RequestParam(value="fileName", required=true) String fileName
            , @RequestParam(value="oriFilename", required=true)  String oriFilename
            , HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("editPrepay model:   " + model);
        System.out.println("editPrepay method:   " + method);
        System.out.println("editPrepay masterId:   " + fileName);
        System.out.println("editPrepay masterId:   " + oriFilename);
        UserEntity user = checkLogin(request, response);
        BaseFormBean formBeanObject = formBeanObject(request);
        Map requestParameterMap = request.getParameterMap();
        Map requestAttMap = requestAttMap(request);
        Map sessionAttMap = sessionAttMap(request);
        Map otherMap =  otherMap(request, response, formBeanObject);

        String data="ok";
        String oriFilenames[] = oriFilename.split(",");
        String fileNames[] = fileName.split(",");
        List<List<String>> importLists = new ArrayList<>(); //記錄入帳過程，show在網頁上

        for(int i = 0;i<fileNames.length;i++){
            String file = fileNames[i];
            String filePath = SystemConfig.getInstance().getParameter("uploadTempPath") + File.separator +file;

            try {
                List<String> fileTmpList = new ArrayList<>();
                fileTmpList.add("------------------------------------------------------------------------------------------------------------------<br>");
                fileTmpList.add(oriFilenames[i]+"<br>");
                importLists.add(fileTmpList);

                List<String> result = parserInExcelToList(filePath);
                importLists.add(result);
            } catch (Exception e) {
                List<String> error = new ArrayList();
                error.add("檔名"+oriFilenames[i]+"有誤<br>");
                importLists.add(error);
            }
        }
        Map outMap = new HashMap();
        outMap.put("importList",importLists);

        Gson gson = new Gson();
        return gson.toJson(data);

    }


    public List<String> parserInExcelToList( String filePath) throws Exception {
        FileInputStream file =  null;
        List<String> list = new ArrayList();
        List<FirstCompanyPackageBean> sourceList = new ArrayList<FirstCompanyPackageBean>();
        try {

            file = new FileInputStream(new File(filePath));
            //Get the workbook instance for XLS file
            HSSFWorkbook workbook = new HSSFWorkbook(file);
            //Get first sheet from the workbook
            HSSFSheet sheet = workbook.getSheetAt(0);

            //Iterate through each rows from first sheet
            //設定index初始值單筆匯入跳過一筆
            //匯入資料直到最後一筆資料
            for(int rowIndex= 1;rowIndex<=sheet.getLastRowNum();rowIndex++)
            {
                HashMap<String, Object> map = new HashMap();
                FirstCompanyPackageBean bean = new FirstCompanyPackageBean();

                Row row =sheet.getRow(rowIndex);
                //若列不存在
                if(row == null){
                    continue;
                }

                String businesscode = null ; //統編
                String dealerCmpName = null; //經銷商
                String dealerName = null; //經銷商業務
                String realStartDate = null; //起帳日
                String chargeName = null; //計費型態
                Integer freeMonth = 0; //客製化:加贈免費月份
                Double giftPrice = 0d; //客製化:贈送金額
                String broker2CpName = null; //介紹公司
                String broker2Name = null; //介紹公司的介紹人
                String broker3CpName = null; //裝機公司
                String broker3Name = null; //裝機公司的裝機人
                String warrantyNo = null; //發票機序號
                String startDate = null; //保固起日
                String extend = null;//是否延長保固

                //取得項目資料
                //列表頭：1.客戶編號、2.公司名稱、3.統編、4.經銷商、5.經銷商業務、6.發票日、7.熱感設定費、8.收款、
                // 9.起帳日、10.計費型態、11.預繳金額、12.熱感機序號、13.第一張發票日、14.業種、15.備註、16.、
                // 17.加贈免費月份、18.贈送金額、19.介紹公司、20.介紹公司的介紹人、21.裝機公司、22.裝機公司的裝機人
                // 23.發票機序號 24.保固起日 25.是否延長保固

                //統編
                Cell cell_2 = row.getCell(2);
                if(isNotEmptyCell(cell_2)){
                    businesscode = "" + (String)getCellValue(cell_2);
                    bean.setBusinesscode(businesscode);

                    //經銷商
                    Cell cell_3 = row.getCell(3);
                    if(isNotEmptyCell(cell_3)){
                        dealerCmpName = (String)getCellValue(cell_3);
                    }

                    //經銷商業務
                    Cell cell_4 = row.getCell(4);
                    if(isNotEmptyCell(cell_4)){
                        dealerName = (String)getCellValue(cell_4);
                    }

                    //起帳日
                    Cell cell_8 = row.getCell(8);
                    if(isNotEmptyCell(cell_8)){
                        realStartDate = (String)getCellValue(cell_8);
                    }

                    //計費型態
                    Cell cell_9 = row.getCell(9);
                    if(isNotEmptyCell(cell_9)){
                        chargeName = (String)getCellValue(cell_9);
                    }

                    //加贈免費月份
                    Cell cell_16 = row.getCell(16);
                    if(isNotEmptyCell(cell_16)){
                        freeMonth = ((Double)getCellValue(cell_16)).intValue();
                    }

                    //贈送金額
                    Cell cell_17 = row.getCell(17);
                    if(isNotEmptyCell(cell_17)){
                        giftPrice = (Double)getCellValue(cell_17);
                    }

                    //介紹公司
                    Cell cell_18 = row.getCell(18);
                    if(isNotEmptyCell(cell_18)){
                        broker2CpName = (String)getCellValue(cell_18);
                    }

                    //介紹公司的介紹人
                    Cell cell_19 = row.getCell(19);
                    if(isNotEmptyCell(cell_19)){
                        broker2Name = (String)getCellValue(cell_19);
                    }

                    //裝機公司
                    Cell cell_20 = row.getCell(20);
                    if(isNotEmptyCell(cell_20)){
                        broker3CpName = (String)getCellValue(cell_20);
                    }

                    //裝機公司的裝機人
                    Cell cell_21 = row.getCell(21);
                    if(isNotEmptyCell(cell_21)){
                        broker3Name = (String)getCellValue(cell_21);
                    }

                    //發票機序號
                    Cell cell_22 = row.getCell(22);
                    if(isNotEmptyCell(cell_22)){
                        warrantyNo = (String)getCellValue(cell_22);
                    }

                    //保固起日
                    Cell cell_23 = row.getCell(23);
                    if(isNotEmptyCell(cell_23)){
                        startDate = (String)getCellValue(cell_23);
                    }

                    //是否延長保固
                    Cell cell_24 = row.getCell(24);
                    if(isNotEmptyCell(cell_24)){
                        extend = (String)getCellValue(cell_24);
                    }

                } else {
                    continue;
                }

                System.out.println("businesscode:"+businesscode+",  dealerCmpName:"+dealerCmpName+",  dealerName="+dealerName
                +", realStartDate="+realStartDate+", chargeName="+chargeName+", freeMonth="+freeMonth+", giftPrice="+giftPrice
                +", broker2CpName="+broker2CpName+", broker2Name="+broker2Name+", broker3CpName="+broker3CpName+", broker3Name="+broker3Name
                +", warrantyNo="+warrantyNo+", startDate="+startDate+", extend="+extend);

                bean.setBusinesscode(businesscode); //統編
                bean.setDealerCmpName(dealerCmpName); //經銷商
                bean.setDealerName(dealerName); //經銷商業務
                bean.setRealStartDate(realStartDate); //起帳日
                bean.setChargeName(chargeName); //計費型態
                bean.setFreeMonth(freeMonth); //客製化:加贈免費月份
                bean.setGiftPrice(giftPrice); //客製化:贈送金額
                bean.setBroker2CpName(broker2CpName); //介紹公司
                bean.setBroker2Name(broker2Name); //介紹公司的介紹人
                bean.setBroker3CpName(broker3CpName); //裝機公司
                bean.setBroker3Name(broker3Name); //裝機公司的裝機人
                bean.setWarrantyNo(warrantyNo); //發票機序號
                bean.setStartDate(startDate); //保固起日
                bean.setExtend(extend); //是否延長保固

                sourceList.add(bean);
            }

            //執行批次建立第一次的用戶綁合約的資料
            String result = firstCompanyPackageService.insertFirstCmpPkg(sourceList,10);
            System.out.println(result);
            list.add(result);

        } catch (Exception e) {
            e.printStackTrace();
            list.add("資料轉型過程發生例外狀況<br>");
        } finally {
            file.close();
        }
        System.out.println("匯入資料完成_emily test");
        return list;
    }

    /**
     * 過瀘不允許的欄位值。
     * @param cell
     * @return
     */
    private boolean isNotEmptyCell(Cell cell){
        boolean result = true;
        //parameters
        if(cell!=null){
            if(getCellValue(cell)!=null){
                Object cellValue = getCellValue(cell);
                if(cellValue==null){
                    result = false;
                }
                if(String.valueOf(getCellValue(cell)).trim().length()==0){
                    result = false;
                }
                if(cellValue.equals("null")){
                    result = false;
                }
            }else{
                result = false;
            }
        }else{
            result = false;
        }
        return result;
    }

    private Object getCellValue(Cell cell) {
        Object value = new Object();
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_BOOLEAN:
                value = cell.getBooleanCellValue();
                break;
            case Cell.CELL_TYPE_NUMERIC:
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    Date date = cell.getDateCellValue();
                    if (date != null) {
                        value = new SimpleDateFormat("yyyy-MM-dd").format(date);
                    } else {
                        value = "";
                    }
                } else {
                    value = cell.getNumericCellValue();
                }
                break;
            case Cell.CELL_TYPE_STRING:
                value = cell.getStringCellValue();
                break;
            default:
                value = null;
        }
        return value;
    }
}
