package com.gate.web.servlets.backend.calCycle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.annotation.WebServlet;

import org.springframework.beans.factory.annotation.Autowired;

import com.gate.web.displaybeans.GiftVO;
import com.gate.web.facades.CalCycleService;
import com.gate.web.servlets.backend.common.BackendPopTemplateServlet;


@WebServlet(urlPatterns = "/backendAdmin/calCycleEditServlet")
public class CalCycleEditServlet extends BackendPopTemplateServlet {

    @Autowired
    CalCycleService calCycleService;
    @Override
    public void doSomething(Map requestParameterMap, Map requestAttMap, Map sessionMap, Map otherMap) throws Exception {

        Object methodObj = requestParameterMap.get("method");
        String method = "";
        if (methodObj != null) method = ((String[]) requestParameterMap.get("method"))[0];
        List<Object> outList = new ArrayList<Object>();
        if (method.equals("editGift")) { //贈送張數畫面
            Integer billId = null;
            if (requestParameterMap.get("billId") != null) {
                billId = Integer.parseInt(((String[]) requestParameterMap.get("billId"))[0]);
            }
            String dispatch_page = "";

            if (billId != null) {
                GiftVO giftVO = calCycleService.findGiftByBillId(billId);
                outList.add(giftVO);
            }
            dispatch_page = getEditDispatch_giftPage();
            otherMap.put(REQUEST_SEND_OBJECT, outList);
            otherMap.put(DISPATCH_PAGE, dispatch_page);
        } else if (method.equals("updateGift")) { //更新贈送張數
            Integer billId = null;
            Integer cntGift = null;
            if (requestParameterMap.get("billId") != null) {
                billId = Integer.parseInt(((String[]) requestParameterMap.get("billId"))[0]);
            }
            if (requestParameterMap.get("cntGift") != null) {
                cntGift = Integer.parseInt(((String[]) requestParameterMap.get("cntGift"))[0]);
            }

            if (billId != null) {
                calCycleService.updateCntGiftByBillId(billId, cntGift);
            }
            otherMap.put(AJAX_JSON_OBJECT, "ok");
        }
    }

    public String getEditDispatch_giftPage() {
        return "/backendAdmin/calCycle/calCycle_editGift.jsp";
    }

}
