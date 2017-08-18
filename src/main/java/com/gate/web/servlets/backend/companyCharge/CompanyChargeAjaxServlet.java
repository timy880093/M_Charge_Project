package com.gate.web.servlets.backend.companyCharge;

import com.gate.web.displaybeans.ChargeModeCycleVO;
import com.gate.web.displaybeans.ChargeModeGradeVO;
import com.gate.web.facades.CompanyChargeServiceImp;
import com.gate.web.servlets.BaseServlet;

import javax.servlet.annotation.WebServlet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@WebServlet(urlPatterns = "/backendAdmin/companyChargeAjaxServlet")
public class CompanyChargeAjaxServlet extends BaseServlet {
    @Override
    public String[] serviceBU(Map requestParameterMap, Map requestAttMap, Map sessionMap, Map otherMap) throws Exception {
        List<Object> outList = new ArrayList<Object>();
        CompanyChargeServiceImp serviceImp = new CompanyChargeServiceImp();
        Object methodObj = requestParameterMap.get("method");
        String method = "";
        if(methodObj!=null) method = ((String[])requestParameterMap.get("method"))[0];
        if(method.equals("getChargeInfo")){  //選擇方案清單(select)後，會自動帶出該方案的相關訊息
            String strChargeId = ((String[])requestParameterMap.get("chargeId"))[0];
            String packageType = ((String[])requestParameterMap.get("packageType"))[0];
            Integer chargeId = Integer.parseInt(strChargeId);
            if(null != packageType){
                if("1".equals(packageType)){ //月租
                    ChargeModeCycleVO cycleVO = serviceImp.findChargeModeCycleByChargeId(chargeId);
                    otherMap.put(AJAX_JSON_OBJECT,cycleVO);
                } else if("2".equals(packageType)){ //級距
                    ChargeModeGradeVO cycleVO = serviceImp.findChargeModeGradeByChargeId(chargeId);
                    otherMap.put(AJAX_JSON_OBJECT,cycleVO);
                }
            }
        } else if(method.equals("getDealerList")){  //選擇經銷公司(select)後，會自動帶出該經銷公司的業務員清單
            String dealerCompanyId = ((String[])requestParameterMap.get("dealerCompanyId"))[0];
            List dealerList = serviceImp.getDealerList(Integer.parseInt(dealerCompanyId));
            otherMap.put(AJAX_JSON_OBJECT,dealerList);
        }else if(method.equals("getCycleTryCalSettle")){ //方案結清前試算
            String packageId = ((String[]) requestParameterMap.get("packageId"))[0];
            String endDate = ((String[]) requestParameterMap.get("endDate"))[0];
            Map tryCalSettleInfo = serviceImp.getCycleTryCalSettle(Integer.parseInt(packageId), endDate);
            otherMap.put(AJAX_JSON_OBJECT,tryCalSettleInfo);
        }else if(method.equals("doSettle")){ //方案結清
            //結清:更新real_end_date和end_date，並且把bill_cylce裡end_date後的bill全部作廢，cash_detail裡的end_date後的cash也要全部作廢
            String packageId = ((String[]) requestParameterMap.get("packageId"))[0];
            String endDate = ((String[]) requestParameterMap.get("endDate"))[0];
            String realEndDate = ((String[]) requestParameterMap.get("realEndDate"))[0];
            Map tryCalSettleInfo = serviceImp.doSettle(Integer.parseInt(packageId), endDate, realEndDate);
            otherMap.put(AJAX_JSON_OBJECT,tryCalSettleInfo);
        }
        return null;
    }
}
