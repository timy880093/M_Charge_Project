package com.gate.web.servlets.backend.cash;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gate.config.SystemConfig;
import com.gate.core.bean.BaseFormBean;
import com.gate.utils.FieldUtils;
import com.gate.web.facades.CashService;
import com.gate.web.servlets.MvcBaseServlet;
import com.gateweb.charge.model.UserEntity;

/**
 * Created by emily on 2016/1/19.
 */
@RequestMapping("/backendAdmin/cashImportServlet")
@Controller
public class CashImportServlet extends MvcBaseServlet {

    @Autowired
    CashService cashService;

	@Autowired
	FieldUtils fieldUtils;

	private static final String DEFAULT_IMPORT_DISPATCH_PAGE = "/backendAdmin/cash/cash_in_import.jsp";

	@RequestMapping(method = RequestMethod.POST)
	public String defaultPost(Model model, @RequestParam MultiValueMap<String, String> paramMap,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.debug("defaultPost paramMap:   " + paramMap);
		logger.debug("defaultPost model:   " + model);
		UserEntity user = checkLogin(request, response);
		BaseFormBean formBeanObject = formBeanObject(request);
		Map requestParameterMap = request.getParameterMap();
		Map requestAttMap = requestAttMap(request);
		Map sessionAttMap = sessionAttMap(request);
		Map otherMap = otherMap(request, response, formBeanObject);
		otherMap.put(DISPATCH_PAGE, DEFAULT_IMPORT_DISPATCH_PAGE);
		sendObjToViewer(request, otherMap);
		return DEFAULT_IMPORT_DISPATCH_PAGE;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String defaultGet(Model model, @RequestParam MultiValueMap<String, String> paramMap,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.debug("defaultGet paramMap:   " + paramMap);
		logger.debug("defaultGet model:   " + model);
		UserEntity user = checkLogin(request, response);
		BaseFormBean formBeanObject = formBeanObject(request);
		Map requestParameterMap = request.getParameterMap();
		Map requestAttMap = requestAttMap(request);
		Map sessionAttMap = sessionAttMap(request);
		Map otherMap = otherMap(request, response, formBeanObject);
		otherMap.put(DISPATCH_PAGE, DEFAULT_IMPORT_DISPATCH_PAGE);
		sendObjToViewer(request, otherMap);
		return DEFAULT_IMPORT_DISPATCH_PAGE;
	}
	
	@RequestMapping(method=RequestMethod.GET, params = "sessionClean=Y", produces = "application/json;charset=utf-8")
    public String defaultGetSessionClean(@RequestParam("sessionClean") String sessionClean, Model model, 
    		 @RequestParam MultiValueMap<String, String> paramMap, HttpServletRequest request, HttpServletResponse response)
    	            throws Exception {
    	logger.debug("defaultGetSessionClean model:   "+model);
    	logger.debug("defaultGetSessionClean paramMap:   "+paramMap);
        logger.debug("defaultGetSessionClean sessionClean:   "+sessionClean);
    	UserEntity user = checkLogin(request, response);
    	BaseFormBean formBeanObject = formBeanObject(request);
    	Map requestParameterMap = request.getParameterMap();
        Map requestAttMap = requestAttMap(request);
        Map sessionAttMap = sessionAttMap(request);
        Map otherMap =  otherMap(request, response, formBeanObject);
        otherMap.put(DISPATCH_PAGE, DEFAULT_IMPORT_DISPATCH_PAGE);
        sendObjToViewer(request, otherMap);
    	return DEFAULT_IMPORT_DISPATCH_PAGE;
    }
	
	@RequestMapping(method = RequestMethod.GET, params = "method=submit", produces = "application/json;charset=utf-8")
	public String fileImportSubmit(@RequestParam("method") String method
			, @RequestParam MultiValueMap<String, String> paramMap , Model model, HttpServletRequest request,
					HttpServletResponse response) throws Exception {
		
		logger.debug("fileImportSubmit method:   " + method);
		logger.debug("fileImportSubmit paramMap:   " + paramMap);
		logger.debug("fileImportSubmit model:   "+model);
		
    	UserEntity user = checkLogin(request, response);
    	BaseFormBean formBeanObject = formBeanObject(request);
    	Map requestParameterMap = request.getParameterMap();
        Map requestAttMap = requestAttMap(request);
        Map sessionAttMap = sessionAttMap(request);
        Map otherMap =  otherMap(request, response, formBeanObject);
        sendObjToViewer(request, otherMap);
    	return DEFAULT_IMPORT_DISPATCH_PAGE;

    }
	
	@RequestMapping(method=RequestMethod.GET, params = "method=entry", produces = "application/json;charset=utf-8")
    public String fileImportEntry(Model model
    				, @RequestParam("companyId") String companyId, HttpServletRequest request, HttpServletResponse response)
    	            throws Exception {
    		logger.debug("fileImportEntry model:   "+model);
        logger.debug("fileImportEntry companyId:   "+companyId);

	    	UserEntity user = checkLogin(request, response);
	    	BaseFormBean formBeanObject = formBeanObject(request);
	    	Map requestParameterMap = request.getParameterMap();
	        Map requestAttMap = requestAttMap(request);
	        Map sessionAttMap = sessionAttMap(request);
	        Map otherMap =  otherMap(request, response, formBeanObject);
	        otherMap.put(REQUEST_SEND_OBJECT, ((String[]) requestParameterMap.get("companyId"))[0]);
	        otherMap.put(DISPATCH_PAGE, DEFAULT_IMPORT_DISPATCH_PAGE);
	        sendObjToViewer(request, otherMap);
	    	return BOOTSTRAP_POP_TEMPLATE_PAGE;
    }
	
	@RequestMapping(method = RequestMethod.POST, params = "method=import", produces = "application/json;charset=utf-8")
	public @ResponseBody String fileImport(@RequestParam("method") String method
					, @RequestParam("companyId") String companyId, @RequestParam("fileName") String fileName
					, @RequestParam("oriFilename") String oriFilename
					, @RequestParam MultiValueMap<String, String> paramMap, Model model, HttpServletRequest request,
					HttpServletResponse response) throws Exception {

		logger.debug("fileImport companyId:   " + companyId);
		logger.debug("fileImport fileName:   " + fileName);
		logger.debug("fileImport method:   " + method);
		logger.debug("fileImport model:   "+model);
		logger.debug("fileImport paramMap:   "+paramMap);

    	UserEntity user = checkLogin(request, response);
    	BaseFormBean formBeanObject = formBeanObject(request);
    	Map requestParameterMap = request.getParameterMap();
        Map requestAttMap = requestAttMap(request);
        Map sessionAttMap = sessionAttMap(request);
        Map otherMap =  otherMap(request, response, formBeanObject);
        sendObjToViewer(request, otherMap);
		//
		String _fileName = ((String[]) requestParameterMap.get("fileName"))[0];
        String filePath = SystemConfig.getInstance().getParameter("uploadTempPath");
        filePath = filePath+"/"+_fileName;
        //filePath = this.getClass().getResource("/").getPath()+ filePath+"/"+ _fileName;

        String _companyId = null;
        String[] values = (String[]) requestParameterMap.get("companyId");
        _companyId = values[0];
        List<List<String>> importLists = new ArrayList<>(); //記錄入帳過程，show在網頁上
        Map outMap = new HashMap();
        try {
            List<String> fileTmpList = new ArrayList<>();
            fileTmpList.add("------------------------------------------------------------------------------------------------------------------<br>");
            fileTmpList.add(oriFilename+"<br>");
            importLists.add(fileTmpList);

            List<String> result = parser$InExcelToList(filePath);
            importLists.add(result);
        } catch (Exception e) {
            List<String> error = new ArrayList();
            error.add("檔名 "+oriFilename+" 有誤<br>");
            importLists.add(error);
        }


        //otherMap.put(AJAX_JSON_OBJECT, errorMap);
        outMap.put("importList", importLists);
        otherMap.put(AJAX_JSON_OBJECT,outMap);
		//
        String jsonString = convertAjaxToJson(outMap);
	    return jsonString;
    }

	public List<String> parser$InExcelToList(String filePath) throws Exception {
		List<String> list = new ArrayList();
		try {

			FileInputStream file = new FileInputStream(new File(filePath));
			// Get the workbook instance for XLS file
			HSSFWorkbook workbook = new HSSFWorkbook(file);
			// Get first sheet from the workbook
			HSSFSheet sheet = workbook.getSheetAt(0);

			// Iterate through each rows from first sheet
			// 設定index初始值單筆匯入跳過一筆
			// 匯入資料直到最後一筆資料
			for (int rowIndex = 6; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
				HashMap<String, Object> map = new HashMap();
				Row row = sheet.getRow(rowIndex);

				Integer cellIndex = 0;
				boolean isAdd = true;

				// 若列不存在
				if (row == null) {
					continue;
				}

				String businesscode = null; // 統編
				String inDate = null; // 繳費日期
				String bankYM = null;// 帳單月份
				// BigDecimal inMoney = new BigDecimal(0);//實繳金額
				Double inMoney = null;

				// 取得項目資料
				// 統編
				Cell cell_1 = row.getCell(1);
				if (fieldUtils.isNotEmptyCell(cell_1)) {
					businesscode = "" + (String) fieldUtils.getCellValue(cell_1);

					// 繳費日期 例:2015-06-25
					Cell cell_9 = row.getCell(9);
					if (fieldUtils.isNotEmptyCell(cell_9)) {
						inDate = (String) fieldUtils.getCellValue(cell_9);
						if (inDate.indexOf("-") != -1) {
							inDate = inDate.replace("-", "/");
						}
					}

					// 帳單月份 例:201505
					Cell cell_10 = row.getCell(10);
					if (fieldUtils.isNotEmptyCell(cell_10)) {
						bankYM = (String) fieldUtils.getCellValue(cell_10);
					}

					// 實繳金額
					Cell cell_13 = row.getCell(13);
					if (fieldUtils.isNotEmptyCell(cell_13)) {
						inMoney = (Double) fieldUtils.getCellValue(cell_13);
					}

				} else {
					continue;
				}

				// 執行入帳
//FIXME
				//String result = cashService.excelSumIn(businesscode, inDate, bankYM, inMoney);
				String result = "test";
				logger.debug(result);
				list.add(result);

			}

			file.close();
		} catch (Exception e) {
			e.printStackTrace();
			list.add("資料轉型過程發生例外狀況<br>");
		}
		return list;
	}

}
