package com.gate.web.servlets.backend.common;

import com.gate.web.servlets.abstraction.DefaultDisplayPageModelViewController;
import com.gateweb.charge.component.propertyProvider.EnvironmentPropertyProvider;
import com.gateweb.charge.enumeration.BillStatus;
import com.gateweb.charge.report.component.IndexPageReportComponent;
import com.gateweb.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;

@RequestMapping("/backendAdmin/indexServlet")
@Controller
public class IndexServlet extends DefaultDisplayPageModelViewController {
    @Autowired
    IndexPageReportComponent indexPageReportComponent;
    @Autowired
    EnvironmentPropertyProvider environmentPropertyProvider;

    @Override
    public String getDefaultPage() {
        return "/pages/index.html";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/recentBillYm", produces = "application/text;charset=utf-8")
    @ResponseBody
    public String getRecentBillYm() {
        List<String> recentBillYm = indexPageReportComponent.getRecentBillYmSet();
        return JsonUtils.gsonToJson(recentBillYm);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/billYmStatistics/{billStatus}", produces = "application/text;charset=utf-8")
    @ResponseBody
    public String getBillYmStatisticsByBillStatus(@PathVariable("billStatus") String billStatus) {
        List<Integer> statisticsData = indexPageReportComponent.getStatisticsByBillYmAndBillStatus(BillStatus.valueOf(billStatus));
        return JsonUtils.gsonToJson(statisticsData);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/env", produces = "application/text;charset=utf-8")
    @ResponseBody
    public String getEnv() {
        Optional<String> envOpt = environmentPropertyProvider.getEnvProperty();
        if (!envOpt.isPresent()) {
            envOpt = Optional.of("");
        }
        return JsonUtils.gsonToJson(envOpt.get());
    }
}
