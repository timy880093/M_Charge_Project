package com.gate.web.servlets.backend.company;

import com.gate.config.SystemConfig;
import com.gate.core.db.Dom4jUtils;
import com.gate.web.beans.CityBean;
import com.gate.web.displaybeans.CompanyVO;
import com.gate.web.facades.CompanyServiceImp;
import com.gate.web.formbeans.CompanyBean;
import com.gate.web.servlets.backend.common.BackendPopTemplateServlet;
import org.apache.commons.lang.StringUtils;

import javax.servlet.annotation.WebServlet;
import java.io.File;
import java.util.*;

import static com.gate.utils.MapBeanConverterUtils.mapToBean;


@WebServlet(urlPatterns = "/backendAdmin/companyEditServlet")
public class CompanyEditServlet extends BackendPopTemplateServlet {

    @Override
    public void doSomething(Map requestParameterMap, Map requestAttMap, Map sessionMap, Map otherMap) throws Exception {
        Object methodObj = requestParameterMap.get("method");
        String method = "";
        if (methodObj != null) method = ((String[]) requestParameterMap.get("method"))[0];
        List<Object> outList = new ArrayList<Object>();
        /*取出縣市*/
        Dom4jUtils dom4jUtils = new Dom4jUtils();
        List<Map<String, Object>> maps = dom4jUtils.getResourceBean("//resource/taiwan/country");
        List<CityBean> cityBeans = new ArrayList<CityBean>();
        for (Map map : maps) {
            CityBean cityBean = new CityBean();
            mapToBean(map, cityBean);
            cityBeans.add(cityBean);
        }
        Set<CityBean> citys = new LinkedHashSet<CityBean>(cityBeans);
        outList.add(citys);
        if (method.equals("edit") ||  method.equals("read")) {
            CompanyServiceImp serviceImp = new CompanyServiceImp();
            String[] values = (String[]) requestParameterMap.get("companyId");
            CompanyVO companyVO = serviceImp.findCompanyByCompanyId(Integer.valueOf(values[0]));
            outList.add(companyVO);
            if(method.equals("read")){
                outList.add("read");
            }
            otherMap.put(REQUEST_SEND_OBJECT, outList);
            otherMap.put(DISPATCH_PAGE, getDispatch_page());
        } else if (method.equals("insert")) {
            CompanyBean companyBean = new CompanyBean();
            mapToBean(requestParameterMap, companyBean);
            CompanyServiceImp serviceImp = new CompanyServiceImp();
            if (StringUtils.isNotEmpty(companyBean.getCompanyId())) {
                if(companyBean.getTransferType().equals("3")
                        && !(serviceImp.checkIfCompanyKeyExist(Integer.parseInt(companyBean.getCompanyId())))){         //改為介接廠商重新產生companyKey
                    companyBean.setCompanyKey(UUID.randomUUID().toString());
                    //產生MigTransaction/src/companyKey的資料夾
                    //產生MigTransaction/bak/companyKey的資料夾
                    //產生MigTransaction/complete/companyKey的資料夾
                    File migSrcPath = new File(SystemConfig.getInstance().getParameter("MIG_ExchangeRootFolder") + "\\src\\" + companyBean.getCompanyKey());
                    File migBakPath = new File(SystemConfig.getInstance().getParameter("MIG_ExchangeRootFolder") + "\\bak\\" + companyBean.getCompanyKey());
                    File migCompletePath = new File(SystemConfig.getInstance().getParameter("MIG_ExchangeRootFolder") + "\\complete\\" + companyBean.getCompanyKey());
                    if (!migSrcPath.exists()) {
                        migSrcPath.mkdirs();
                    }
                    if(!migBakPath.exists()){
                        migBakPath.mkdirs();
                    }
                    if(!migCompletePath.exists()){
                        migCompletePath.mkdirs();
                    }
                }
                adminUpdate(companyBean);
            } else {
                if(companyBean.getTransferType().equals("3")){         //介接廠商
                    companyBean.setCompanyKey(UUID.randomUUID().toString());
                }
                serviceImp.insertCompany(companyBean);
            }

            otherMap.put(AJAX_JSON_OBJECT, "success");
        }
    }

    public void adminUpdate(CompanyBean companyBean) throws Exception {
        CompanyServiceImp serviceImp = new CompanyServiceImp();
        serviceImp.updateCompany(companyBean);
    }

    public String getDispatch_page() {
        return "/backendAdmin/company/company_edit.jsp";
    }

}
