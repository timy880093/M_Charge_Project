package com.gate.web.servlets.backend.commission;

import com.gate.utils.MapBeanConverterUtils;
import com.gate.web.displaybeans.DealerCompanyVO;
import com.gate.web.displaybeans.DealerVO;
import com.gate.web.facades.CommissionServiceImp;
import com.gate.web.formbeans.DealerCompanyBean;
import com.gate.web.servlets.backend.common.BackendPopTemplateServlet;

import javax.servlet.annotation.WebServlet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@WebServlet(urlPatterns = "/backendAdmin/dealerCompanyEditServlet")
public class DealerCompanyEditServlet extends BackendPopTemplateServlet {
    @Override
    public void doSomething(Map requestParameterMap, Map requestAttMap, Map sessionMap, Map otherMap) throws Exception {

        CommissionServiceImp serviceImp = new CommissionServiceImp();
        Object methodObj = requestParameterMap.get("method");
        String method = "";
        if (methodObj != null) method = ((String[]) requestParameterMap.get("method"))[0];
        List<Object> outList = new ArrayList<Object>();
        if (method.equals("create")) { //按下"新增經銷商方案"按鈕時，跳出的預設初始畫面
            otherMap.put(DISPATCH_PAGE, getDispatch_page());
        } else if (method.equals("edit") || method.equals("read")) { //檢視 經銷商方案
            String dealerCompanyId = ((String[]) requestParameterMap.get("dealerCompanyId"))[0];

            DealerCompanyVO vo = serviceImp.getDealerCompanyByDealerCompanyId(Integer.parseInt(dealerCompanyId));
            outList.add(vo);

            List<DealerVO> dealerList = serviceImp.getDealerByDealerCompanyId(Integer.parseInt(dealerCompanyId));
            outList.add(dealerList);

            if(method.equals("edit")){
                outList.add("edit");
            }
            otherMap.put(REQUEST_SEND_OBJECT, outList);
            otherMap.put(DISPATCH_PAGE, getDispatch_page());

        } else if (method.equals("insert")) { //新增或修改經銷商和經銷商業務員資訊
            DealerCompanyBean bean = new DealerCompanyBean();
            MapBeanConverterUtils.mapToBean(requestParameterMap, bean);

            String update = ((String[]) requestParameterMap.get("exist"))[0];
            serviceImp.insertDealerCompany(bean);
        }
    }

    public String getDispatch_page() {
        return "/backendAdmin/commission/edit_dealerCompany.jsp";
    }

}
