package com.gateweb.charge.report.component;

import com.gateweb.orm.charge.entity.ChargePackage;
import com.gateweb.orm.charge.entity.Contract;
import com.gateweb.orm.charge.entity.Company;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Component
public class CurrentlyNotExpireContractReportDataGenerator {

    public List<String> headerListGen() {
        List<String> headerList = new ArrayList<>();
        headerList.add("companyName");
        headerList.add("companyBusinessNo");
        headerList.add("packageName");
        headerList.add("expirationDate");
        return headerList;
    }

    public List<HashMap<String, String>> reportDataGen(
            HashMap<Long, Company> companyMap
            , HashMap<Long, ChargePackage> packageMap
            , Collection<Contract> contractCollection) {
        List<HashMap<String, String>> resultList = new ArrayList<>();
        contractCollection.stream().forEach(contract -> {
            HashMap<String, String> resultHashMap = new HashMap<>();
            if (companyMap.containsKey(contract.getCompanyId())) {
                resultHashMap.put("companyName", companyMap.get(contract.getCompanyId()).getName());
                resultHashMap.put("companyBusinessNo", companyMap.get(contract.getCompanyId()).getBusinessNo());
            }
            if (packageMap.containsKey(contract.getPackageId())) {
                resultHashMap.put("packageName", packageMap.get(contract.getPackageId()).getName());
            }
            if (contract.getExpirationDate() != null) {
                resultHashMap.put("expirationDate", contract.getExpirationDate().format(
                        DateTimeFormatter.ofPattern("yyyy/MM/dd")
                ));
            }
            resultList.add(resultHashMap);
        });
        return resultList;
    }
}
