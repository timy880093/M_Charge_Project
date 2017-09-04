package com.gate.web.servlets.backend.commissionLog;

import com.gate.utils.MapBeanConverterUtils;
import com.gate.web.facades.CommissionLogServiceImp;
import com.gate.web.servlets.backend.common.BackendPopTemplateServlet;

import javax.servlet.annotation.WebServlet;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by emily on 2016/1/11.
 */
@WebServlet(urlPatterns = "/backendAdmin/commissionLogEditServlet")
public class CommissionLogEditServlet extends BackendPopTemplateServlet {
    @Override
    public void doSomething(Map requestParameterMap, Map requestAttMap, Map sessionMap, Map otherMap) throws Exception {
        CommissionLogServiceImp serviceImp = new CommissionLogServiceImp();
        Object methodObj = requestParameterMap.get("method");
        String method = "";
        if (methodObj != null) method = ((String[]) requestParameterMap.get("method"))[0];
        List<Object> outList = new ArrayList<Object>();

        if (method.equals("edit")) { //檢視 該筆佣金的用戶繳款清單

            String commissionLogId = ((String[]) requestParameterMap.get("commission_log_id"))[0];

            List comLogDetailList = serviceImp.getCommissionLogDetailList(commissionLogId);
            outList.add(comLogDetailList);

            otherMap.put(REQUEST_SEND_OBJECT, outList);
            otherMap.put(DISPATCH_PAGE, getDispatch_page());
        } else if (method.equals("updateNote")) { //修改 佣金計算的備註
            String data = "delete success!! ";
            try {
                Integer commissionLogId = 0;
                String strCommissionLogId = ((String[]) requestParameterMap.get("commission_log_id"))[0];
                if (null != strCommissionLogId) {
                    commissionLogId = Integer.parseInt(strCommissionLogId);
                }
                String note = ((String[]) requestParameterMap.get("note"))[0];
                serviceImp.updateNote(commissionLogId, note);
            } catch (Exception e) {
                e.printStackTrace();
                data = "delete error";
            }
            otherMap.put(AJAX_JSON_OBJECT, data);

        } else if (method.equals("delete")) {// 刪除
           String data="sucess !!";
            try {
                Integer commissionLogId = 0;
                String strCommissionLogId = ((String[]) requestParameterMap.get("commission_log_id"))[0];
                if (null == strCommissionLogId) {
                    commissionLogId = Integer.parseInt(strCommissionLogId);
                }
                serviceImp.delete(commissionLogId);

            }catch (Exception e){
                e.printStackTrace();
                data = "delete error";

            }
        otherMap.put(AJAX_JSON_OBJECT,data);
        }




        }

    public String getDispatch_page() {
        return "/backendAdmin/commissionLog/commissionLog_edit.jsp";
    }
}
