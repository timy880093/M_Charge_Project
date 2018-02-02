package com.gate.web.servlets.backend.insertData;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.annotation.WebServlet;

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


@WebServlet(urlPatterns = "/backendAdmin/firstCompanyPackageAjaxServlet")
public class firstCompanyPackageAjaxServlet extends BaseServlet {

	@Autowired
    FirstCompanyPackageService firstCompanyPackageService;

    @Override
    public String[] serviceBU(Map requestParameterMap, Map requestAttMap, Map sessionMap, Map otherMap) throws Exception {
        Object methodObj = requestParameterMap.get("method");
        String method = "";
        if(methodObj!=null) method = ((String[])requestParameterMap.get("method"))[0];
        if(method.equals("import") ){ //匯入入帳資料-入帳批次
            String fileName = ((String[])requestParameterMap.get("fileName"))[0];
            String oriFilename = ((String[])requestParameterMap.get("oriFilename"))[0];
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

            otherMap.put(AJAX_JSON_OBJECT,outMap);
            return null;
        }
        return new String[0];
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
            String result = firstCompanyPackageService.insertFirstCmpPkg(sourceList);
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
