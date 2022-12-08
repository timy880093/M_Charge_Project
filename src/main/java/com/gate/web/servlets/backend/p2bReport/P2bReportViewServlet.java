package com.gate.web.servlets.backend.p2bReport;

import com.gate.web.servlets.abstraction.DefaultDisplayPageModelViewController;
import com.gateweb.charge.report.component.P2bIasrReportGstaticBeanGenerator;
import com.gateweb.charge.report.p2bIasrReport.bean.P2bIasrReportGstaticBean;
import com.gateweb.charge.report.p2bIasrReport.bean.P2bIasrReportGstaticReq;
import com.gateweb.charge.report.p2bIasrReport.component.P2bReportReqConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@RequestMapping("/backendAdmin/p2bReportViewServlet")
@Controller
public class P2bReportViewServlet extends DefaultDisplayPageModelViewController {
    @Autowired
    P2bIasrReportGstaticBeanGenerator p2bIasrReportGstaticBeanGenerator;

    protected final Logger logger = LogManager.getLogger(getClass());
    private static final String DEFAULT_DISPATCH_PAGE = "/pages/p2bIasrReportListView.html";

    @Override
    public String getDefaultPage() {
        return DEFAULT_DISPATCH_PAGE;
    }

    /**
     * @param seller
     * @param startDate yyyyMMdd
     * @param endDate   yyyyMMdd
     * @return
     */
    @GetMapping(value = "/gstatic/seller/{seller}/startDate/{startDate}/endDate/{endDate}")
    public ResponseEntity findGStaticDataList(
            @PathVariable("seller") String seller
            , @PathVariable("startDate") String startDate
            , @PathVariable("endDate") String endDate) {
        Optional<P2bIasrReportGstaticReq> p2bIasrReportGstaticReqOptional =
                P2bReportReqConverter.gen(seller, startDate, endDate);
        if (p2bIasrReportGstaticReqOptional.isPresent()) {
            List<P2bIasrReportGstaticBean> resultList = p2bIasrReportGstaticBeanGenerator.gen(
                    p2bIasrReportGstaticReqOptional.get()
            );
            return ResponseEntity.ok(resultList);
        }
        return ResponseEntity.badRequest().build();
    }

}
