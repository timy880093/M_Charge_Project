package com.gate.web.servlets.backend.charge;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.annotation.WebServlet;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.gate.utils.MapBeanConverterUtils;
import com.gate.web.displaybeans.ChargeModeCycleVO;
import com.gate.web.displaybeans.ChargeModeGradeVO;
import com.gate.web.facades.ChargeService;
import com.gate.web.facades.ChargeServiceImp;
import com.gate.web.formbeans.ChargeModeCycleBean;
import com.gate.web.formbeans.ChargeModeGradeBean;
import com.gate.web.servlets.backend.common.BackendPopTemplateServlet;


@WebServlet(urlPatterns = "/backendAdmin/chargeEditServlet")
public class ChargeEditServlet extends BackendPopTemplateServlet {

    @Autowired
    ChargeService chargeService;

    @Override
    public void doSomething(Map requestParameterMap, Map requestAttMap, Map sessionMap, Map otherMap) throws Exception {

        Object methodObj = requestParameterMap.get("method");
        String method = "";
        if (methodObj != null) method = ((String[]) requestParameterMap.get("method"))[0];
        List<Object> outList = new ArrayList<Object>();
        if(method.equals("add")){
            String dispatch_page = getCreateDispatch_page();
            otherMap.put(REQUEST_SEND_OBJECT, outList);
            otherMap.put(DISPATCH_PAGE, dispatch_page);

        } else if (method.equals("edit") || method.equals("read")) { //新增or查詢 方案
            if(method.equals("edit")){
                outList.add("edit");
            }else{
                outList.add("read");
            }
            String charge_type = ((String[]) requestParameterMap.get("type"))[0];
            Integer chargeId = null;
            if(requestParameterMap.get("chargeId")!=null){
                chargeId = Integer.parseInt(((String[]) requestParameterMap.get("chargeId"))[0]);
            }
            String dispatch_page = "";
            if("1".equals(charge_type)){ //月租制
                if(chargeId!=null){
                    ChargeModeCycleVO chargeVO = chargeService.findChargeModeCycleByChargeId(chargeId);
                    outList.add(chargeVO);
                }

                dispatch_page = getEditDispatch_page1();
            } else if("2".equals(charge_type)){ //級距制
                if(chargeId!=null){
                    //級距型方案
                    ChargeModeGradeVO chargeVO = chargeService.findChargeModeGradeByChargeId(chargeId);
                    outList.add(chargeVO);
                    //級距型方案的級距表
                    List gradeList = chargeService.getGradeList(chargeId);
                    outList.add(gradeList);
                }

                dispatch_page = getEditDispatch_page2();
            }

            otherMap.put(REQUEST_SEND_OBJECT, outList);
            otherMap.put(DISPATCH_PAGE, dispatch_page);
        } else if (method.equals("insert")) { //新增方案
            String charge_type = ((String[]) requestParameterMap.get("type"))[0];
            if("1".equals(charge_type)){ //月租型
                ChargeModeCycleBean bean = new ChargeModeCycleBean();
                MapBeanConverterUtils.mapToBean(requestParameterMap,bean);
                if(StringUtils.isNotEmpty(bean.getChargeId())){
                    chargeService.updateChargeModeCycle(bean);
                } else{
                    bean.setStatus(String.valueOf(1));
                    chargeService.insertChargeModeCycle(bean);
                }

            }else if("2".equals(charge_type)){ //級距型
                ChargeModeGradeBean bean = new ChargeModeGradeBean();
                MapBeanConverterUtils.mapToBean(requestParameterMap,bean);

                chargeService.transactionInsertChargeModeGrade(bean);
//                Integer chargeModeGradeId = bean.getChargeId();
//                if(null != chargeModeGradeId ){
//                    chargeService.transactionUpdateChargeModeGrade(bean);
//                } else{
//                    bean.setStatus(1);
//                    chargeService.transactionInsertChargeModeGrade(bean);
//                }

            }
        }
    }

    public String getCreateDispatch_page() {
        return "/backendAdmin/charge/edit_charge_package.jsp";
    }
    public String getEditDispatch_page1() {
        return "/backendAdmin/charge/edit_charge_package_month.jsp";
    }
    public String getEditDispatch_page2() {
        return "/backendAdmin/charge/edit_charge_package_grade.jsp";
    }

}
