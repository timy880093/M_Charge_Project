package com.gate.web.servlets.backend.cash;

import com.gate.utils.MapBeanConverterUtils;
import com.gate.web.displaybeans.CashDetailVO;
import com.gate.web.displaybeans.CashMasterVO;
import com.gate.web.facades.CashServiceImp;
import com.gate.web.formbeans.GiftBean;
import com.gate.web.servlets.backend.common.BackendPopTemplateServlet;
import dao.BillCycleEntity;
import org.apache.commons.lang.StringUtils;

import javax.servlet.annotation.WebServlet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@WebServlet(urlPatterns = "/backendAdmin/cashEditServlet")
public class CashEditServlet extends BackendPopTemplateServlet {

    @Override
    public void doSomething(Map requestParameterMap, Map requestAttMap, Map sessionMap, Map otherMap) throws Exception {
        CashServiceImp serviceImp = new CashServiceImp();
        Object methodObj = requestParameterMap.get("method");
        String method = "";
        if (methodObj != null) method = ((String[]) requestParameterMap.get("method"))[0];
        List<Object> outList = new ArrayList<Object>();
        if(method.equals("viewCashDetail")){ //檢視帳單明細
            Integer cashMasterId = null;
            if (requestParameterMap.get("cashMasterId") != null) {
                cashMasterId = Integer.parseInt(((String[]) requestParameterMap.get("cashMasterId"))[0]);
            }
            List<CashDetailVO> cashDetailList=serviceImp.getCashDetailListByMasterId(cashMasterId);
            outList.add(cashDetailList);

            CashMasterVO cashMasterVO = serviceImp.getCashMasterByMasterId(cashMasterId);
            outList.add(cashMasterVO);

            String dispatch_page = getEditDispatch_page();
            otherMap.put(REQUEST_SEND_OBJECT, outList);
            otherMap.put(DISPATCH_PAGE, dispatch_page);
        } else if(method.equals("updateCashDetail")){ //更新帳單明細
            Integer cashDetailId = null;
            if (requestParameterMap.get("updateCashDetailId") != null) {
                if(!StringUtils.isEmpty(((String[]) requestParameterMap.get("updateCashDetailId"))[0])){
                    cashDetailId = Integer.parseInt(((String[]) requestParameterMap.get("updateCashDetailId"))[0]);
                }
            }
            Double diffPrice = 0d;
            if (requestParameterMap.get("updateDiffPrice") != null) {
                if(!StringUtils.isEmpty(((String[]) requestParameterMap.get("updateDiffPrice"))[0])){
                    diffPrice = Double.parseDouble(((String[]) requestParameterMap.get("updateDiffPrice"))[0]);
                }
            }
            String diffPriceNote = null;
            if (requestParameterMap.get("updateDiffPriceNote") != null) {
                diffPriceNote = ((String[]) requestParameterMap.get("updateDiffPriceNote"))[0];
            }
            Integer cashMasterId = null;
            if (requestParameterMap.get("cashMasterId") != null) {
                if(!StringUtils.isEmpty(((String[]) requestParameterMap.get("cashMasterId"))[0])){
                    cashMasterId = Integer.parseInt(((String[]) requestParameterMap.get("cashMasterId"))[0]);
                }
            }

            boolean isOK = serviceImp.updateCashDetail(cashDetailId, diffPrice, diffPriceNote);

            List<CashDetailVO> cashDetailList=serviceImp.getCashDetailListByMasterId(cashMasterId);
            outList.add(cashDetailList);

            CashMasterVO cashMasterVO = serviceImp.getCashMasterByMasterId(cashMasterId);
            outList.add(cashMasterVO);

            String dispatch_page = getEditDispatch_page();
            otherMap.put(REQUEST_SEND_OBJECT, outList);
            otherMap.put(DISPATCH_PAGE, dispatch_page);
        } else if(method.equals("viewOrverList")){ //檢視超額明細
            Integer cashDetailId = null;
            if (requestParameterMap.get("cashDetailId") != null) {
                cashDetailId = Integer.parseInt(((String[]) requestParameterMap.get("cashDetailId"))[0]);
            }
            Integer billType = null;
            if (requestParameterMap.get("billType") != null) {
                billType = Integer.parseInt(((String[]) requestParameterMap.get("billType"))[0]);
            }
            List<BillCycleEntity> billCycleList=serviceImp.getOverListByDetailId(cashDetailId);
            outList.add(billCycleList);

            String dispatch_page = getEditDispatch_OverListpage();
            if(2 == billType){
                dispatch_page = getEditDispatch_OverGradeListpage(); //級距型
            }
            otherMap.put(REQUEST_SEND_OBJECT, outList);
            otherMap.put(DISPATCH_PAGE, dispatch_page);
        } else if(method.equals("cancelOver")){ //取消超額計算
            Integer cashDetailId = null;
            if (requestParameterMap.get("cashDetailId") != null) {
                cashDetailId = Integer.parseInt(((String[]) requestParameterMap.get("cashDetailId"))[0]);
            }

            boolean isOK = serviceImp.cancelOver(cashDetailId);

            Integer cashMasterId = null;
            if (requestParameterMap.get("cashMasterId") != null) {
                cashMasterId = Integer.parseInt(((String[]) requestParameterMap.get("cashMasterId"))[0]);
            }
            List<CashDetailVO> cashDetailList=serviceImp.getCashDetailListByMasterId(cashMasterId);
            outList.add(cashDetailList);

            CashMasterVO cashMasterVO = serviceImp.getCashMasterByMasterId(cashMasterId);
            outList.add(cashMasterVO);

            String dispatch_page = getEditDispatch_page();
            otherMap.put(REQUEST_SEND_OBJECT, outList);
            otherMap.put(DISPATCH_PAGE, dispatch_page);
        } else if(method.equals("cancelPrepay")) { //取消預繳
            Integer cashDetailId = null;
            if (requestParameterMap.get("cashDetailId") != null) {
                cashDetailId = Integer.parseInt(((String[]) requestParameterMap.get("cashDetailId"))[0]);
            }

            boolean isOK = serviceImp.cancelPrepay(cashDetailId);

            Integer cashMasterId = null;
            if (requestParameterMap.get("cashMasterId") != null) {
                cashMasterId = Integer.parseInt(((String[]) requestParameterMap.get("cashMasterId"))[0]);
            }
            List<CashDetailVO> cashDetailList=serviceImp.getCashDetailListByMasterId(cashMasterId);
            outList.add(cashDetailList);

            CashMasterVO cashMasterVO = serviceImp.getCashMasterByMasterId(cashMasterId);
            outList.add(cashMasterVO);

            String dispatch_page = getEditDispatch_page();
            otherMap.put(REQUEST_SEND_OBJECT, outList);
            otherMap.put(DISPATCH_PAGE, dispatch_page);
        } else if(method.equals("delCashMaster")){ //刪除帳單(帳單裡沒有任何明細，則可刪除)
            Integer cashMasterId = null;
            if (requestParameterMap.get("cashMasterId") != null) {
                cashMasterId = Integer.parseInt(((String[]) requestParameterMap.get("cashMasterId"))[0]);
                serviceImp.delCashMaster(cashMasterId);
                otherMap.put(AJAX_JSON_OBJECT, "success");
            } else {
                throw new Exception();
            }
        }
    }

    public String getEditDispatch_page() {
        return "/backendAdmin/cash/cash_editCashDetail.jsp";
    }
    public String getEditDispatch_OverListpage() {
        return "/backendAdmin/cash/cash_overList.jsp";
    }
    public String getEditDispatch_OverGradeListpage() {
        return "/backendAdmin/cash/cash_overGradeList.jsp";
    }

}
