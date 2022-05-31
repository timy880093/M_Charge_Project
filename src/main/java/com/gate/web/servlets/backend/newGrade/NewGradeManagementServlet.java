package com.gate.web.servlets.backend.newGrade;

import com.gate.web.facades.UserService;
import com.gate.web.servlets.abstraction.DefaultDisplayPageModelViewController;
import com.gateweb.charge.chargePolicy.grade.RootGradeViewBeanConverter;
import com.gateweb.charge.chargePolicy.grade.bean.RootGradeStorageBean;
import com.gateweb.charge.chargePolicy.grade.service.GradeService;
import com.gateweb.charge.chargePolicy.grade.service.RootGradeSearchService;
import com.gateweb.charge.exception.InvalidGradeLevelException;
import com.gateweb.charge.frontEndIntegration.bean.SweetAlertResponse;
import com.gateweb.charge.frontEndIntegration.enumeration.SweetAlertStatus;
import com.gateweb.charge.model.nonMapped.CallerInfo;
import com.gateweb.orm.charge.entity.RootGradeFetchView;
import com.gateweb.orm.charge.repository.RootGradeFetchViewRepository;
import com.gateweb.utils.bean.BeanConverterUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequestMapping("/backendAdmin/newGradeManagementServlet")
@Controller
public class NewGradeManagementServlet extends DefaultDisplayPageModelViewController {
    private final Logger logger = LogManager.getLogger(NewGradeManagementServlet.class);
    private static final String DEFAULT_DISPATCH_PAGE = "/pages/chargeGradeListView.html";
    final BeanConverterUtils beanConverterUtils = new BeanConverterUtils();

    @Autowired
    GradeService gradeService;
    @Autowired
    RootGradeFetchViewRepository rootGradeFetchViewRepository;
    @Autowired
    RootGradeSearchService rootGradeSearchService;
    @Autowired
    RootGradeViewBeanConverter rootGradeViewBeanConverter;
    @Autowired
    UserService userService;

    Gson gson = new Gson();

    @RequestMapping(method = RequestMethod.GET, params = "method=list", produces = "application/json;charset=utf-8")
    public @ResponseBody
    String searchList(@RequestParam MultiValueMap<String, String> paramMap, Model model) {
        List<RootGradeFetchView> rootGradeViewList = rootGradeFetchViewRepository.findAll();
        Map dataMap = new HashMap();
        dataMap.put("data", rootGradeViewList);
        return gson.toJson(dataMap);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/list", produces = "application/json;charset=utf-8")
    public @ResponseBody
    String searchFetchViewList() {
        List<RootGradeFetchView> rootGradeViewList = rootGradeSearchService.findAllRootGradeFetchView();
        Map dataMap = new HashMap();
        dataMap.put("data", rootGradeViewList);
        return gson.toJson(dataMap);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/api/saveOrUpdate", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String createNewGrade(HttpServletRequest request, HttpServletResponse response, @RequestBody String jsonBody) {
        SweetAlertResponse sweetAlertResponse = new SweetAlertResponse();
        try {
//            todo: 暫時拔掉權限
            Optional<CallerInfo> callerInfoOptional = userService.getCallerInfoByUserId(new Long(318));
            if (callerInfoOptional.isPresent()) {
                gson = new GsonBuilder().serializeNulls().create();
                HashMap<String, Object> map = beanConverterUtils.convertJsonToMap(jsonBody);
                RootGradeFetchView rootGradeFetchView = rootGradeViewBeanConverter.convert(map);
                RootGradeStorageBean rootGradeStorageBean = gradeService.mergeWithDataBase(
                        rootGradeFetchView, callerInfoOptional.get()
                );
                gradeService.saveRootGradeStorageBean(rootGradeStorageBean);
                System.out.println(gson.toJson(rootGradeFetchView));
                sweetAlertResponse.setSweetAlertStatus(SweetAlertStatus.SUCCESS);
                sweetAlertResponse.setTitle("修改成功");
            }
        } catch (InvalidGradeLevelException igle) {
            sweetAlertResponse.setSweetAlertStatus(SweetAlertStatus.ERROR);
            sweetAlertResponse.setTitle("修改失敗");
            sweetAlertResponse.setMessage("級距層級不可為空");
        } catch (Exception ex) {
            sweetAlertResponse.setSweetAlertStatus(SweetAlertStatus.ERROR);
            sweetAlertResponse.setTitle("非預期錯誤");
            sweetAlertResponse.setMessage(ex.getMessage());
        }
        return gson.toJson(sweetAlertResponse);
    }

    @RequestMapping(method = RequestMethod.GET, params = "method=delete", produces = "application/json;charset=utf8")
    public @ResponseBody
    String deleteNewGrade(@RequestParam MultiValueMap<String, String> paramMap, Model model) {
        HashMap<String, String> resultMap = new HashMap<>();
        try {
            Long rootId = Long.valueOf(paramMap.get("rootId").get(0));
            gradeService.transactionDeleteNewGradeIfExists(rootId);
            resultMap.put("status", "success");
            resultMap.put("message", "修改成功");
        } catch (Exception e) {
            resultMap.put("status", "error");
            resultMap.put("message", "修改失敗");
            logger.error(e.getMessage());
        }
        return gson.toJson(resultMap);
    }

    @Override
    public String getDefaultPage() {
        return DEFAULT_DISPATCH_PAGE;
    }
}
