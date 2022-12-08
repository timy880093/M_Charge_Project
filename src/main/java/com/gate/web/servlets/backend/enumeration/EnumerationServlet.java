package com.gate.web.servlets.backend.enumeration;

import com.gate.web.facades.CompanyService;
import com.gateweb.charge.enumeration.*;
import com.gateweb.charge.frontEndIntegration.bean.FrontOption;
import com.gateweb.orm.charge.entity.Company;
import com.gateweb.orm.charge.entity.ProductCategory;
import com.gateweb.orm.charge.repository.CompanyRepository;
import com.gateweb.orm.charge.repository.ProductCategoryRepository;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RequestMapping("/backendAdmin/enumerationServlet")
@Controller
/**
 * 用api的方式提供前端enumeration的查詢。
 */
public class EnumerationServlet {
    protected Logger logger = LoggerFactory.getLogger(EnumerationServlet.class);
    Gson gson = new Gson();

    @Autowired
    ProductCategoryRepository productCategoryRepository;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    CompanyService companyService;

    @PostMapping
    public String defaultPost(@RequestParam MultiValueMap<String, String> paramMap,
                              Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("defaultPost model:   " + model);
        logger.debug("defaultPost paramMap:   " + paramMap);
        return null;
    }

    @GetMapping
    public String defaultGet(@RequestParam MultiValueMap<String, String> paramMap
            , Model model, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        logger.debug("defaultGet model: {} ", model);
        logger.debug("defaultGet paramMap: {}", paramMap);
        return null;
    }

    @GetMapping(value = "/api/productType", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getProductTypePossibleValues() {
        return gson.toJson(ProductType.values());
    }

    @GetMapping(value = "/api/chargeBaseType", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getChargeBaseTypePossibleValues() {
        Set<FrontOption> optionSet = new HashSet<>();
        Arrays.stream(ChargeBaseType.values()).forEach(chargeBaseType -> {
            FrontOption frontOption = new FrontOption();
            frontOption.setId(chargeBaseType.name());
            frontOption.setName(chargeBaseType.description);
            optionSet.add(frontOption);
        });
        return gson.toJson(optionSet);
    }

    @GetMapping(value = "/api/chargePlan", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getChargePlanPossibleValues() {
        return gson.toJson(ChargePlan.values());
    }

    @GetMapping(value = "/api/prepayChargePlan", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getPrepayChargePlanPossibleValues() {
        List possibleList = new ArrayList();
        possibleList.add(ChargePlan.INITIATION);
        possibleList.add(ChargePlan.PERIODIC);
        return gson.toJson(possibleList);
    }

    @GetMapping(value = "/api/contractStatus", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getContractStatusPossibleValues() {
        Set<FrontOption> optionSet = new HashSet<>();
        Arrays.stream(ContractStatus.values()).filter(contractStatus -> {
            if (contractStatus.name().equals("C")) {
                return true;
            }
            if (contractStatus.name().equals("D")) {
                return true;
            }
            if (contractStatus.name().equals("E")) {
                return true;
            }
            return false;
        }).forEach(contractStatus -> {
            FrontOption frontOption = new FrontOption();
            frontOption.setId(contractStatus.name());
            frontOption.setName(contractStatus.description);
            optionSet.add(frontOption);
        });
        return gson.toJson(optionSet);
    }

    @GetMapping(value = "/api/billStatus", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getBillStatusPossibleValues() {
        Set<FrontOption> optionSet = new HashSet<>();
        Arrays.stream(BillStatus.values()).forEach(billStatus -> {
            FrontOption frontOption = new FrontOption();
            frontOption.setId(billStatus.name());
            frontOption.setName(billStatus.description);
            optionSet.add(frontOption);
        });
        return gson.toJson(optionSet);
    }

    @GetMapping(value = "/api/deductStatus", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getDeductStatusPossibleValues() {
        return gson.toJson(DeductStatus.values());
    }

    @GetMapping(value = "/api/deductType", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getDeductTypePossibleValues() {
        return gson.toJson(DeductType.values());
    }

    @GetMapping(value = "/api/paidPlan", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getPaidPlanPossibleValues() {
        Set<FrontOption> optionSet = new HashSet<>();
        Arrays.stream(PaidPlan.values()).forEach(paidPlan -> {
            FrontOption frontOption = new FrontOption();
            frontOption.setId(String.valueOf(paidPlan.name()));
            frontOption.setName(paidPlan.description);
            optionSet.add(frontOption);
        });
        return gson.toJson(optionSet);
    }

    @GetMapping(value = "/api/productStatus", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getProductStatusPossibleValues() {
        return gson.toJson(ProductStatus.values());
    }

    @GetMapping(value = "/api/terminationClauseType", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getTerminationClauseTypeValues() {
        return gson.toJson(TerminationClauseType.values());
    }

    @GetMapping(value = "/api/chargePackageStatus", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getChargePackageStatusValues() {
        return gson.toJson(ChargePackageStatus.values());
    }

    @GetMapping(value = "/api/chargeRuleStatus", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getChargeRuleStatusValues() {
        return gson.toJson(ChargeRuleStatus.values());
    }

    @GetMapping(value = "/api/productCategory", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String productCategoryMenuValues() {
        List<ProductCategory> productCategoryList = productCategoryRepository.findAll();
        Set<FrontOption> optionSet = new HashSet<>();
        productCategoryList.stream().forEach(productCategory -> {
            if (productCategory.getProductCategoryId().compareTo(4L) == 0) {
                return;
            }
            if (productCategory.getProductCategoryId().compareTo(1L) == 0) {
                return;
            }
            FrontOption frontOption = new FrontOption();
            frontOption.setId(String.valueOf(productCategory.getProductCategoryId()));
            frontOption.setName(productCategory.getCategoryName());
            optionSet.add(frontOption);
        });
        return gson.toJson(optionSet);
    }

    @GetMapping(value = "/api/chargeBase", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String chargeBaseMenuList() {
        return gson.toJson(ChargeBaseType.values());
    }

    @GetMapping(value = "/api/companyMenuItem", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String companyMenuItemList() {
        List<Company> companySet = companyRepository.findAll();
        return gson.toJson(companyToFrontOption(companySet));
    }

    @GetMapping(value = "/api/bankCode", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String bankCodeList() {
        Set<FrontOption> optionSet = new HashSet<>();
        Arrays.stream(BankCode.values()).forEach(bankCode -> {
            FrontOption frontOption = new FrontOption();
            frontOption.setId(bankCode.getCode());
            frontOption.setName(bankCode.name());
            optionSet.add(frontOption);
        });
        return gson.toJson(optionSet);
    }

    @GetMapping(value = "/api/paymentMethod", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String paymentMethodList() {
        Set<FrontOption> optionSet = new HashSet<>();
        Arrays.stream(PaymentMethod.values()).forEach(bankCode -> {
            FrontOption frontOption = new FrontOption();
            frontOption.setId(bankCode.name());
            frontOption.setName(bankCode.name());
            optionSet.add(frontOption);
        });
        return gson.toJson(optionSet);
    }

    @GetMapping( value = "/api/cycleType", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String cycleTypeMenuList() {
        Set<FrontOption> optionSet = new HashSet<>();
        //加入空白選項
        optionSet.add(genNonSelectOption());
        Arrays.stream(CycleType.values()).forEach(cycleType -> {
            FrontOption frontOption = new FrontOption();
            frontOption.setId(cycleType.name());
            frontOption.setName(cycleType.description);
            optionSet.add(frontOption);
        });
        return gson.toJson(optionSet);
    }

    @GetMapping(value = "/api/chargeCycleType", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String chargeCycleTypeMenuList() {
        Set<FrontOption> optionSet = new HashSet<>();
        //加入空白選項
        optionSet.add(genNonSelectOption());
        Arrays.stream(CycleType.values()).filter(cycleType -> {
            return cycleType.equals(CycleType.ANY)
                    || cycleType.equals(CycleType.GW_OVERAGE_BIL)
                    || cycleType.equals(CycleType.SEASON);
        }).forEach(cycleType -> {
            FrontOption frontOption = new FrontOption();
            frontOption.setId(cycleType.name());
            frontOption.setName(cycleType.description);
            optionSet.add(frontOption);
        });
        return gson.toJson(optionSet);
    }

    /**
     * 只開放兩種作為計算規則
     *
     * @return
     */
    @GetMapping(value = "/api/calculateCycleType", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String calculateCycleTypeMenuList() {
        Set<FrontOption> optionSet = new HashSet<>();
        //加入空白選項
        optionSet.add(genNonSelectOption());
        Arrays.stream(CycleType.values()).filter(cycleType -> {
            return cycleType.equals(CycleType.GW_OVERAGE_BIL_CAL) || cycleType.equals(CycleType.GW_RENTAL_CAL);
        }).forEach(cycleType -> {
            FrontOption frontOption = new FrontOption();
            frontOption.setId(cycleType.name());
            frontOption.setName(cycleType.description);
            optionSet.add(frontOption);
        });
        return gson.toJson(optionSet);
    }

    public Set<FrontOption> companyToFrontOption(Collection<Company> companySet) {
        Set<FrontOption> optionSet = new HashSet<>();
        companySet.stream().forEach(company -> {
            FrontOption frontOption = new FrontOption();
            frontOption.setId(company.getCompanyId().toString());
            StringBuffer companyDescBuffer = new StringBuffer();
            companyDescBuffer.append(company.getName());
            companyDescBuffer.append("(");
            companyDescBuffer.append(company.getBusinessNo());
            companyDescBuffer.append(")");
            frontOption.setName(companyDescBuffer.toString());
            optionSet.add(frontOption);
        });
        return optionSet;
    }

    public FrontOption genNonSelectOption() {
        FrontOption frontOption = new FrontOption();
        frontOption.setName("未選擇");
        frontOption.setId("");
        return frontOption;
    }
}

