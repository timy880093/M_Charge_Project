package com.gate.web.servlets.backend.companyCharge;

import com.gate.utils.TimeUtils;
import com.gate.web.facades.ChargeServiceImp;
import com.gate.web.facades.CommissionServiceImp;
import com.gate.web.facades.CompanyChargeServiceImp;
import com.gate.web.facades.CompanyServiceImp;
import com.gate.web.formbeans.CompanyChargeCycleBean;
import com.gate.web.formbeans.CompanyChargeSingleBean;
import com.gate.web.servlets.backend.common.BackendPopTemplateServlet;
import dao.DealerCompanyEntity;

import javax.servlet.annotation.WebServlet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.gate.utils.MapBeanConverterUtils.mapToBean;

@WebServlet(urlPatterns = "/backendAdmin/companyChargeEditServlet")
public class CompanyChargeEditServlet extends BackendPopTemplateServlet {
    @Override
    public void doSomething(Map requestParameterMap, Map requestAttMap, Map sessionMap, Map otherMap) throws Exception {

        CompanyChargeServiceImp serviceImp = new CompanyChargeServiceImp();
        Object methodObj = requestParameterMap.get("method");
        String method = "";
        if (methodObj != null) method = ((String[]) requestParameterMap.get("method"))[0];
        List<Object> outList = new ArrayList<Object>();
        String charge_type = ((String[]) requestParameterMap.get("type"))[0];
        //String charge_type = "1";
        if(method.equals("read")){  //檢視用戶綁合約的方案內容
            String dispatch_page = "";
            String package_id = ((String[]) requestParameterMap.get("packageId"))[0];
            if ("1".equals(charge_type)) {
                Map packageMap = serviceImp.getCyclePackageInfoByPackageId(Integer.parseInt(package_id));
                outList.add(packageMap);
                dispatch_page = "/backendAdmin/companyCharge/read_company_charge_detail_month.jsp";
            } else if("2".equals(charge_type)){
                Map packageMap = serviceImp.getGradePackageInfoByPackageId(Integer.parseInt(package_id));
                outList.add(packageMap);
                dispatch_page = "/backendAdmin/companyCharge/read_company_charge_detail_grade.jsp";
            }
            otherMap.put(REQUEST_SEND_OBJECT, outList);
            otherMap.put(DISPATCH_PAGE, dispatch_page);
        } else if (method.equals("edit")) { //在用戶清單中，選擇要新增某用戶的合約時，會跳出一個該用戶設定相關合約的內容

            String companyId = ((String[]) requestParameterMap.get("companyId"))[0];
            CompanyServiceImp companyServiceImp = new CompanyServiceImp();
            Map companyMap = companyServiceImp.getCompanyInfoByCompanyId(Integer.parseInt(companyId));
            outList.add(companyMap);    //公司基本資料 0
            String dispatch_page = "";
            if ("1".equals(charge_type) || "2".equals(charge_type)) {
                Timestamp evlS = TimeUtils.getCurrentTimestamp();

                outList.add(serviceImp.getChargeMonthList());  //1
                outList.add(serviceImp.getChargeGradeList()); //2
                outList.add(serviceImp.getChargeCycleHisByCompany(Integer.parseInt(companyId)));  //3 //2

                //經銷公司列表
                CommissionServiceImp dealerCompanyserviceImp = new CommissionServiceImp();
                List<DealerCompanyEntity> dealerCompanyEntities = dealerCompanyserviceImp.getDealerCompanyListForDropBox();
                outList.add(dealerCompanyEntities); //4 //3

                Timestamp evlE = TimeUtils.getCurrentTimestamp();
                long difference= evlE.getTime() - evlS.getTime();
                logger.info("CompanyChargeEditServlet 撈「設定公司收費方式 月租型」頁面資料  difference="+difference+"ms");

                dispatch_page = getEditDispatch_page1();
            }

            otherMap.put(REQUEST_SEND_OBJECT, outList);
            otherMap.put(DISPATCH_PAGE, dispatch_page);
        } else if (method.equals("insert")) { //用戶綁合約: 填要的合約內容後，按下確認新增，作用戶合約的動作

            if ("1".equals(charge_type) || "2".equals(charge_type)) {
                //月租型(packageType=1) //級距型(packageType=2)
                CompanyChargeCycleBean chargeCycleBean = new CompanyChargeCycleBean();
                mapToBean(requestParameterMap,chargeCycleBean);
                serviceImp.insertCompanyChargeCycle(chargeCycleBean);  //新增月租合約(用戶綁合約)
            }
        } else if(method.equals("settleView")){ //settle(結清畫面)
            if ("1".equals(charge_type) || "2".equals(charge_type)) { //1.月租 2.級距
                String companyId = ((String[]) requestParameterMap.get("companyId"))[0];
                String packageId = ((String[]) requestParameterMap.get("packageId"))[0];
                Map settleInfo = serviceImp.getSettleInfo(Integer.parseInt(companyId), Integer.parseInt(packageId));
                outList.add(settleInfo);
            }
            otherMap.put(REQUEST_SEND_OBJECT, outList);
            otherMap.put(DISPATCH_PAGE, getEditDispatch_page3());
        }
    }


    public String getEditDispatch_page1() {
        return "/backendAdmin/companyCharge/edit_company_charge_month.jsp";
    }

    public String getEditDispatch_page3(){
        return "/backendAdmin/companyCharge/edit_settle.jsp";
    }
}
