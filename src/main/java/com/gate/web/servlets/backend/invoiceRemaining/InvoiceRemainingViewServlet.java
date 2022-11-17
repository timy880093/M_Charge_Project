package com.gate.web.servlets.backend.invoiceRemaining;

import com.gate.web.facades.UserService;
import com.gate.web.servlets.abstraction.DefaultDisplayPageModelViewController;
import com.gateweb.charge.model.nonMapped.CallerInfo;
import com.gateweb.charge.report.bean.InvoiceRemainingHistoryView;
import com.gateweb.charge.security.ChargeUserPrinciple;
import com.gateweb.charge.service.dataGateway.InvoiceRemainingHistoryViewDataGateway;
import com.gateweb.utils.JsonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequestMapping("/backendAdmin/invoiceRemainingViewServlet")
@Controller
public class InvoiceRemainingViewServlet extends DefaultDisplayPageModelViewController {
    protected final Logger logger = LogManager.getLogger(getClass());
    private static final String DEFAULT_DISPATCH_PAGE = "/pages/einvRemainingListView.html";

    @Autowired
    InvoiceRemainingHistoryViewDataGateway invoiceRemainingHistoryViewDataGateway;
    @Autowired
    UserService userService;

    @RequestMapping(method = RequestMethod.GET, value = "/find/list/{companyId}", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String searchInvoiceRemainingViewList(
            Authentication authentication
            , @PathVariable("companyId") long companyId) {
        List<InvoiceRemainingHistoryView> invoiceRemainingDataList = new ArrayList<>();
        try {
            ChargeUserPrinciple chargeUserPrinciple = getChargeUserPrinciple(authentication);
            Optional<CallerInfo> callerInfoOptional = userService.getCallerInfoByChargeUser(chargeUserPrinciple.getUserInstance());
            if (callerInfoOptional.isPresent()) {
                invoiceRemainingDataList = invoiceRemainingHistoryViewDataGateway.findByCompanyId(companyId);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return JsonUtils.gsonToJson(invoiceRemainingDataList);
    }

    @Override
    public String getDefaultPage() {
        return DEFAULT_DISPATCH_PAGE;
    }
}
