package com.gate.web.servlets.backend.cash;

import com.gate.config.SystemConfig;
import com.gate.web.facades.CashService;
import com.gate.web.facades.CashServiceImp;
import com.gate.web.servlets.BaseServlet;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import javax.servlet.annotation.WebServlet;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.gate.utils.DecimalUtils.getStrippingValue;

/**
 * Created by emily on 2016/1/19.
 */
@WebServlet(urlPatterns = "/backendAdmin/cashImportServlet")
public class CashImportServlet extends BaseServlet {

    CashServiceImp cashServiceImp = new CashServiceImp();

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

                    List<String> result = parser$InExcelToList(filePath);
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

        otherMap.put(DISPATCH_PAGE,getDispatch_page());
        return new String[]{FORWARD_TYPE_F,POP_TEMPLATE_PAGE};
    }

    public String getDispatch_page() {
        return "/backendAdmin/cash/cash_in_import.jsp";
    }

    public List<String> parser$InExcelToList( String filePath) throws Exception {
        List<String> list = new ArrayList();
        try {

            FileInputStream file = new FileInputStream(new File(filePath));
            //Get the workbook instance for XLS file
            HSSFWorkbook workbook = new HSSFWorkbook(file);
            //Get first sheet from the workbook
            HSSFSheet sheet = workbook.getSheetAt(0);

            //Iterate through each rows from first sheet
            //設定index初始值單筆匯入跳過一筆
            //匯入資料直到最後一筆資料
            for(int rowIndex= 6;rowIndex<=sheet.getLastRowNum();rowIndex++)
            {
                HashMap<String, Object> map = new HashMap();
                Row row =sheet.getRow(rowIndex);


                Integer cellIndex = 0;
                boolean isAdd = true;

                //若列不存在
                if(row == null){
                    continue;
                }

                String businesscode = null ; //統編
                String inDate = null; //繳費日期
                String bankYM = null;//帳單月份
//                BigDecimal inMoney = new BigDecimal(0);//實繳金額
                Double inMoney = null;

                //取得項目資料
                //統編
                Cell cell_1 = row.getCell(1);
                if(isNotEmptyCell(cell_1)){
                    businesscode = "" + (String)getCellValue(cell_1);

                    //繳費日期 例:2015-06-25
                    Cell cell_9 = row.getCell(9);
                    if(isNotEmptyCell(cell_9)){
                        inDate = (String)getCellValue(cell_9);
                        if(inDate.indexOf("-")!=-1){
                            inDate = inDate.replace("-", "/");
                        }
                    }

                    //帳單月份 例:201505
                    Cell cell_10 = row.getCell(10);
                    if(isNotEmptyCell(cell_10)){
                        bankYM = (String)getCellValue(cell_10);
                    }

                    //實繳金額
                    Cell cell_13 = row.getCell(13);
                    if(isNotEmptyCell(cell_13)){
                        inMoney = (Double)getCellValue(cell_13);
                    }

                } else {
                    continue;
                }

                //執行入帳
                String result = cashServiceImp.excelSumIn(businesscode, inDate, bankYM, inMoney);
                System.out.println(result);
                list.add(result);

            }

            file.close();
        } catch (Exception e) {
            e.printStackTrace();
            list.add("資料轉型過程發生例外狀況<br>");
        }
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
