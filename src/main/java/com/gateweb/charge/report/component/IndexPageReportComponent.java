package com.gateweb.charge.report.component;

import com.gateweb.charge.enumeration.BillStatus;
import com.gateweb.orm.charge.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class IndexPageReportComponent {
    @Autowired
    BillRepository billRepository;

    public List<String> getRecentBillYmSet() {
        List<String> billYmList = new ArrayList<>(billRepository.findBillYmByTopLimit(12));
        Collections.reverse(billYmList);
        return billYmList;
    }

    public Optional<BigDecimal> getStatisticsByBillYmAndStatus(String billYm, BillStatus billStatus) {
        List<String> valueList = new ArrayList<>();
        return billRepository.findSumByBillYm(billYm, billStatus.name());
    }

    public List<Integer> getStatisticsByBillYmAndBillStatus(BillStatus billStatus) {
        List<String> recentBillYmSet = getRecentBillYmSet();
        List<Integer> arrList = new ArrayList();
        recentBillYmSet.stream().forEach(billYm -> {
            Optional<BigDecimal> sumValueOpt = getStatisticsByBillYmAndStatus(billYm, billStatus);
            if (sumValueOpt.isPresent()) {
                arrList.add(sumValueOpt.get().setScale(0, RoundingMode.HALF_UP).intValue());
            } else {
                arrList.add(BigDecimal.ZERO.intValue());
            }
        });
        return arrList;
    }

}
