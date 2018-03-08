package com.gate.web.servlets.backend.cash;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;

import com.gate.utils.ExcelPoiWrapper;
import com.gate.web.beans.CashDetailBean;
import com.gate.web.beans.CashMasterBean;
import com.gate.web.beans.InvoiceExcelBean;
import com.gate.web.beans.QuerySettingVO;
import com.gate.web.facades.CalCycleService;
import com.gate.web.facades.CashService;
import com.gate.web.facades.CashServiceImp;
import com.gate.web.servlets.SearchServlet;

@WebServlet(urlPatterns = "/backendAdmin/cashSearchServlet")
public class CashSearchServlet extends SearchServlet {
    private static final String DOWNLOAD_FILE_NAME_OUT ="out_data";
    private static final String DOWNLOAD_FILE_NAME_INVOICE ="invoice_data";
    //private static final String TEMPLATE_EXCEL_DOWNLOAD_OUT = SystemConfig.getInstance().getParameter("uploadTempPath") + "/tempFile"+"/out_temp.xls";
    //private static final String TEMPLATE_EXCEL_DOWNLOAD_INVOICE = SystemConfig.getInstance().getParameter("uploadTempPath") + "/tempFile"+"/invoice_temp.xls";
    
    @Autowired
    CashService cashService;
    
    @Autowired
    CalCycleService calCycleService;

    @Override
    public String[] serviceBU(Map requestParameterMap, Map requestAttMap, Map sessionMap, Map otherMap) throws Exception {


        Object methodObj = requestParameterMap.get("method");
        String method = "";
        if (methodObj != null) method = ((String[]) requestParameterMap.get("method"))[0];
        List<Object> outList = new ArrayList<Object>();
        if (method.equals("search")) {
            QuerySettingVO querySettingVO = new QuerySettingVO();
            Map pageMap = getData(requestParameterMap, querySettingVO, otherMap, "cashSearchVO");
            otherMap.put(AJAX_JSON_OBJECT, pageMap);
            return null;
        } else if(method.equals("out")) { //出帳-多筆
            String data = "success!!";
            Integer exeCnt = 0;
            try{
                String destJson = ((String[]) requestParameterMap.get("destJson"))[0]; //帳單年月
                exeCnt = cashService.out(destJson);
                data += "  total counts: "+exeCnt+"";
            }catch(Exception ex){
                System.out.println(ex);
                data = " fail!!";
            }

            otherMap.put(AJAX_JSON_OBJECT, data);
            return null;
        } else if(method.equals("outYM")) { //出帳-批次
            String data = "success!!";
            Integer exeCnt = 0;
            try{
                String outYM = ((String[]) requestParameterMap.get("outYM"))[0]; //帳單年月
                Object userCompanyIdObj = ((String[]) requestParameterMap.get("userCompanyId"))[0];
                Integer userCompanyId = 0 ;
                if(null != userCompanyIdObj){
                    if( !userCompanyIdObj.equals("")){
                        userCompanyId = Integer.parseInt(((String[]) requestParameterMap.get("userCompanyId"))[0]); //用戶id
                    }
                }
                exeCnt = cashService.outYM(outYM, userCompanyId);
                data += "  total counts: "+exeCnt;
            }catch(Exception ex){
                System.out.println(ex);
                data = " fail!!";
            }

            otherMap.put(AJAX_JSON_OBJECT, data);
            return null;
        } else if(method.equals("searchDetail")){ //檢視帳單明細
            QuerySettingVO querySettingVO = new QuerySettingVO();
            Map pageMap = getData(requestParameterMap, querySettingVO, otherMap, "cashFlowDetailSearchVO");
            otherMap.put(AJAX_JSON_OBJECT, pageMap);
            return null;
        } else if(method.equals("outExcelym")){ //匯出Excel帳單-批次(請選擇出帳單年月)
            String outYM = ((String[]) requestParameterMap.get("outYM"))[0]; //帳單年月
            List cashMasterList =  cashService.getCashMasterDetail(outYM);
            String filePath = this.getClass().getResource("/").getPath()+"/tempFile"+"/out_temp.xls";
            ExcelPoiWrapper excel= genCashDataToExcel(cashMasterList, filePath);
            HttpServletResponse response = (HttpServletResponse) otherMap.get(RESPONSE);
            responseExcelFileToClient(excel, response, DOWNLOAD_FILE_NAME_OUT+"_"+outYM);

            return null;
        }else if(method.equals("outExcel")){ //匯出Excel帳單-多筆(請勾選欲執行的資料)
            String outYM = ((String[]) requestParameterMap.get("outYM"))[0]; //帳單年月
            String destJson = ((String[]) requestParameterMap.get("destJson"))[0]; //多筆的選擇
            List cashMasterList =  cashService.getCashMasterDetail(outYM, destJson);
            String filePath = this.getClass().getResource("/").getPath()+"/tempFile"+"/out_temp.xls";
            ExcelPoiWrapper excel= genCashDataToExcel(cashMasterList, filePath);
            HttpServletResponse response = (HttpServletResponse) otherMap.get(RESPONSE);
            responseExcelFileToClient(excel, response, DOWNLOAD_FILE_NAME_OUT+"_"+outYM);

            return null;
        } else if(method.equals("cancelOutYM")){ //批次取消出帳
            String data = "success!!";
            try{
                String outYM = ((String[]) requestParameterMap.get("outYM"))[0]; //帳單年月
                int exeCnt = cashService.cancelOutYM(outYM);
                data += " total counts: " + exeCnt;
            }catch(Exception ex){
                System.out.println(ex);
                data = " fail!!";
            }
            otherMap.put(AJAX_JSON_OBJECT, data);
            return null;
        } else if(method.equals("cancelOut")){ //多筆取消出帳
            String data = "success!!";
            try{
                String destJson = ((String[]) requestParameterMap.get("destJson"))[0]; //帳單年月
                int exeCnt = cashService.cancelOut(destJson);
                data += " total counts: " + exeCnt;
            }catch(Exception ex){
                System.out.println(ex);
                data = " fail!!";
            }

            otherMap.put(AJAX_JSON_OBJECT, data);
            return null;
        } else if (method.equals("emailYM")){  //批次-寄email
            String data = "success!!";
            try{
                String calYM  = ((String[]) requestParameterMap.get("outYM"))[0];
                int exeCnt = cashService.sendBillMailYM(calYM);
                data += " total counts: " + exeCnt;
            }catch(Exception ex){
                System.out.println(ex);
                data = " fail!!";
            }
            otherMap.put(AJAX_JSON_OBJECT, data);
            return null;

        } else if(method.equals("email")){  //多筆-寄email
            String data = "success!!";
            try{
                String destJson = ((String[]) requestParameterMap.get("destJson"))[0]; //帳單年月
                int exeCnt = cashService.sendBillMail(destJson);
                data += " total counts: " + exeCnt;
            }catch(Exception ex){
                System.out.println(ex);
                data = " fail!!";
            }
            otherMap.put(AJAX_JSON_OBJECT, data);
            return null;

        } else if(method.equals("email1")){  //多筆-未繳費客戶通知
            String data = "success!!";
            try{
                String destJson = ((String[]) requestParameterMap.get("destJson"))[0]; //帳單年月
                int exeCnt = cashService.transactionSendBillMail1(destJson);
                data += " total counts: " + exeCnt;
            }catch(Exception ex){
                System.out.println(ex);
                data = " fail!!";
            }
            otherMap.put(AJAX_JSON_OBJECT, data);
            return null;

        } else if (method.equals("emailUnrecorded")){  //批次-寄email
            String data = "success!!";
            try{
                String calYM  = ((String[]) requestParameterMap.get("outYM"))[0];
                int exeCnt = cashService.sendBillMailYM(calYM);
                data += " total counts: " + exeCnt;
            }catch(Exception ex){
                System.out.println(ex);
                data = " fail!!";
            }
            otherMap.put(AJAX_JSON_OBJECT, data);
            return null;
        } else if(method.equals("invoiceExcelYM")){ //匯出發票資料-by 年月
            String outYM = ((String[]) requestParameterMap.get("outYM"))[0]; //帳單年月
            List invoiceItemList =  cashService.getInvoiceItem(outYM);
            String filePath = this.getClass().getResource("/").getPath()+"/tempFile"+"/invoice_temp.xls";
            ExcelPoiWrapper excel= genInvoiceItemToExcel(invoiceItemList, filePath);
            HttpServletResponse response = (HttpServletResponse) otherMap.get(RESPONSE);
            responseExcelFileToClient(excel, response, DOWNLOAD_FILE_NAME_INVOICE+"_"+outYM);

            return null;
        } else if(method.equals("invoiceExcel")){ //匯出發票資料-by 多筆
            String destJson = ((String[]) requestParameterMap.get("destJson"))[0]; //多筆的選擇
            List cashMasterList =  cashService.getInvoiceItem(null, destJson);
            String filePath = this.getClass().getResource("/").getPath()+"/tempFile"+"/invoice_temp.xls";
            ExcelPoiWrapper excel= genInvoiceItemToExcel(cashMasterList, filePath);
            HttpServletResponse response = (HttpServletResponse) otherMap.get(RESPONSE);
            responseExcelFileToClient(excel, response, DOWNLOAD_FILE_NAME_INVOICE);

            return null;
        } else {
            List ymList = cashService.getYM();
            outList.add(ymList); //0.
            List userCompanyList = calCycleService.getUserCompanyList();
            outList.add(userCompanyList); //1.

            //2.計算年月:預設每月15日後算這個月的帳單
            Calendar now = Calendar.getInstance();
            Integer day = now.get(Calendar.DAY_OF_MONTH);
            if(day<15){ //每月15日後算這個月的帳單
                now.add(Calendar.MONTH, -1);
            }
            String year = "" + now.get(Calendar.YEAR);
            String month = "";
            if ((now.get(Calendar.MONTH) + 1)<10){
                month = "0" + (now.get(Calendar.MONTH) + 1);
            }else{
                month = "" + (now.get(Calendar.MONTH) + 1);
            }
            outList.add(year + month);

            otherMap.put(REQUEST_SEND_OBJECT, outList);
            otherMap.put(DISPATCH_PAGE, getDispatch_page());
            String[] returnList = {FORWARD_TYPE_F, TEMPLATE_PAGE};
            return returnList;
        }
    }

    public String getDispatch_page() {
        return "/backendAdmin/cash/cash_list.jsp";
    }


    /**
     * AJAX 資料來源
     *
     * @param querySettingVO
     * @return
     * @throws Exception
     */
    public Map doSearchData(QuerySettingVO querySettingVO, Map otherMap) throws Exception {
        Map cashList = cashService.getCashMaster(querySettingVO);
        return cashList;
    }

    @Override
    public Map doSearchDownloadData(QuerySettingVO querySettingVO, Map otherMap) throws Exception {
        Map map = new HashMap();
        List cashMasterList =  cashService.getCashMasterDetail("201601");
        map.put("cashMasterList",cashMasterList);
        return map;
    }


    private void responseExcelFileToClient(ExcelPoiWrapper excel, HttpServletResponse response,String fileName)
            throws Exception {
        response.setContentType("text/plain");
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName+".xls");
        excel.getWorkBook().write(response.getOutputStream());
        response.getOutputStream().close();

    }

    /**
     *  產生查詢下載的Excel報表
     * @param cashMasterList
     * @param tempPath
     * @return
     * @throws Exception
     */
    private ExcelPoiWrapper genCashDataToExcel(List<CashMasterBean> cashMasterList, String tempPath) throws Exception {
        ExcelPoiWrapper excel = new ExcelPoiWrapper(tempPath);

        HashMap packageMap = new HashMap();
        excel.setWorkSheet(1);
        int baseRow=2;
        int index = 0;
        int packageIndex = 8;
        for(CashMasterBean masterBean:cashMasterList){
            excel.copyRows(2, 21, 1, baseRow);

            excel.setValue(baseRow, index + 1, masterBean.getBusinessNo());
            excel.setValue(baseRow, index + 2, masterBean.getInAmount());
            excel.setValue(baseRow, index + 6, masterBean.getCompanyName());
            excel.setValue(baseRow, index + 7, masterBean.getBusinessNo());

            List<CashDetailBean> cashDetailList = masterBean.getCashDetailList();
            for(CashDetailBean detail: cashDetailList){
                Integer chargeId = detail.getChargeId();
                Integer cashType = detail.getCashType(); //1.月租 2.超額 3.代印代寄 4.加值型 5.儲值 6.預繳
                Integer billType = detail.getBillType(); //1.月租 2.級距

                //要繳的金額(月租預繳和超額分開)
                BigDecimal taxInclusivePrice_ = detail.getTaxInclusivePrice();
                Integer taxInclusivePrice = 0;
                if(taxInclusivePrice_.intValue() == 0){
                    taxInclusivePrice = 0; //不然excel顯示的數字會0E-9
                }else{
                    taxInclusivePrice = taxInclusivePrice_.setScale(0,BigDecimal.ROUND_HALF_UP).intValue();
                }


                if(null == packageMap.get(chargeId+","+cashType+","+billType)){
                    packageMap.put(chargeId+","+cashType+","+billType, packageIndex);
                    excel.setValue(baseRow, index + packageIndex, taxInclusivePrice);
                    if(cashType == 1){ //月租
                        excel.setValue(1, index + packageIndex, detail.getPackageName());
                    }else if(cashType == 2){ //超額
                        excel.setValue(1, index + packageIndex, detail.getPackageName()+"(超額)");
                    }else  if(cashType == 6){ //預繳
                        excel.setValue(1, index + packageIndex, "預繳");
                    }else if(cashType == 7){ //7.扣抵
                        excel.setValue(1, index + packageIndex, "扣抵");
                    }
                    packageIndex++;
                }else{
                    Integer packageIndexOld = (Integer)packageMap.get(chargeId + ","+cashType+","+billType);
                    excel.setValue(baseRow, index + packageIndexOld, taxInclusivePrice);
                }
            }
            baseRow++;
        }

        //把沒有packagename的cell填0
        HSSFSheet sheet = excel.getSheet();
        for(int i=2; i<baseRow; i++){
            Row row =sheet.getRow(i-1);
            for(int j=8; j<packageIndex; j++){
                Cell cell = row.getCell(j-1);
                if(!isNotEmptyCell(cell)){
                    excel.setValue(i, j, 0);
                }
            }
        }

        return excel;

    }

    //匯出發票資料Excel
    private ExcelPoiWrapper genInvoiceItemToExcel(List<InvoiceExcelBean> InvoiceExcelList, String tempPath) throws Exception {
        ExcelPoiWrapper excel = new ExcelPoiWrapper(tempPath);
        HashMap packageMap = new HashMap();
        excel.setWorkSheet(1);
        int baseRow=3;
        int index = 0;
        int packageIndex = 8;
        for(InvoiceExcelBean bean:InvoiceExcelList){
            excel.copyRows(3, 21, 1, baseRow);

            excel.setValue(baseRow, index + 1, bean.getInvoiceIndex()); //發票張數
            excel.setValue(baseRow, index + 2, bean.getInvoiceDate()); //發票日期
            excel.setValue(baseRow, index + 3, bean.getItemIndex()); //品名序號
            excel.setValue(baseRow, index + 4, bean.getItemName()); //發票品名
            excel.setValue(baseRow, index + 5, bean.getItemCnt()); //數量
            excel.setValue(baseRow, index + 6, bean.getUnitPrice()); //單價
            excel.setValue(baseRow, index + 7, bean.getTaxType()); //課稅別
            excel.setValue(baseRow, index + 8, bean.getTax()); //稅率
            excel.setValue(baseRow, index + 10, bean.getBusinessNo()); //買方統編
            excel.setValue(baseRow, index + 11, 2); //1.列印 2.列印+email

            baseRow++;
        }
        return excel;

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
