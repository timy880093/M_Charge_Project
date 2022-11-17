package com.gateweb.charge.report.component;

import com.gateweb.charge.report.p2bIasrReport.bean.P2bIasrReportGstaticBean;
import com.gateweb.charge.report.p2bIasrReport.bean.P2bIasrReportGstaticReq;
import com.gateweb.orm.charge.entity.view.ChargeIasrP2bTotalAmountView;
import com.gateweb.orm.charge.repository.ChargeIasrP2bTotalAmountViewRepository;
import com.gateweb.utils.ConcurrentUtils;
import com.gateweb.utils.LocalDateTimeUtils;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.gateweb.utils.ConcurrentUtils.pool;

@Component
public class P2bIasrReportGstaticBeanGenerator {
    Logger logger = LoggerFactory.getLogger(P2bIasrReportGstaticBeanGenerator.class);
    @Autowired
    ChargeIasrP2bTotalAmountViewRepository chargeIasrP2bTotalAmountViewRepository;

    public List<P2bIasrReportGstaticBean> gen(P2bIasrReportGstaticReq req) {
        Optional<String> startDateStr = LocalDateTimeUtils.parseLocalDateTimeToString(req.getStartLocalDateTime(), "yyyyMMdd");
        Optional<String> endDateStr = LocalDateTimeUtils.parseLocalDateTimeToString(req.getEndLocalDateTime(), "yyyyMMdd");
        List<String> yearMonthList = Collections.synchronizedList(req.getMonthStrList());
        List<ChargeIasrP2bTotalAmountView> chargeIasrP2bTotalAmountViewList =
                Collections.synchronizedList(
                        chargeIasrP2bTotalAmountViewRepository.findInvoiceAmountForP2b(
                                req.getSellerIdentifier(),
                                startDateStr.get(),
                                endDateStr.get()
                        )
                );
        List<CompletableFuture<Void>> completableFutureList = Collections.synchronizedList(new ArrayList<>());
        List<P2bIasrReportGstaticBean> resultList = Collections.synchronizedList(new ArrayList<>());
        Double median = getDescriptiveStatistics(chargeIasrP2bTotalAmountViewList).getPercentile(50);
        yearMonthList.parallelStream().forEach(yearMonth -> {
            completableFutureList.add(CompletableFuture.runAsync(() -> {
                P2bIasrReportGstaticBean p2bIasrReportGstaticBean = new P2bIasrReportGstaticBean();
                BigDecimal threeMonthAvg = gen3MonthAverageByYearMonth(yearMonth, chargeIasrP2bTotalAmountViewList);
                BigDecimal twelveMonthAvg = gen12MonthAverageByYearMonth(yearMonth, chargeIasrP2bTotalAmountViewList);
                BigDecimal m2m = genMonthToMonthByYearMonth(yearMonth, chargeIasrP2bTotalAmountViewList);
                BigDecimal q2q = genQuarterToQuarterByYearMonth(yearMonth, chargeIasrP2bTotalAmountViewList);
                BigDecimal avg3MinusAvg12 = gen3MonthAverageMinus12MonthAverage(yearMonth, chargeIasrP2bTotalAmountViewList);
                BigDecimal total = genTotalByYearMonth(yearMonth, chargeIasrP2bTotalAmountViewList);

                if (median.isNaN()) {
                    p2bIasrReportGstaticBean.setMedian(0d);
                } else if (median.isInfinite()) {
                    p2bIasrReportGstaticBean.setMedian(-1d);
                } else {
                    p2bIasrReportGstaticBean.setMedian(median);
                }
                p2bIasrReportGstaticBean.setMonth(yearMonth);
                p2bIasrReportGstaticBean.setTotal(total.doubleValue());
                p2bIasrReportGstaticBean.setAvgPre3Month(threeMonthAvg.doubleValue());
                p2bIasrReportGstaticBean.setAvgPre12Month(twelveMonthAvg.doubleValue());
                p2bIasrReportGstaticBean.setAvg3MinusAvg12(avg3MinusAvg12.doubleValue());
                p2bIasrReportGstaticBean.setM2m(m2m.doubleValue());
                p2bIasrReportGstaticBean.setQ2q(q2q.doubleValue());
                resultList.add(p2bIasrReportGstaticBean);
            }, pool));
        });
        ConcurrentUtils.completableGet(completableFutureList);
        return resultList.stream()
                .sorted(Comparator.comparing(P2bIasrReportGstaticBean::getMonth))
                .collect(Collectors.toList());
    }

    public DescriptiveStatistics getDescriptiveStatistics(List<ChargeIasrP2bTotalAmountView> viewList) {
        DescriptiveStatistics descriptiveStatistics = new DescriptiveStatistics();
        viewList.stream().forEach(chargeIasrP2bTotalAmountView -> {
            descriptiveStatistics.addValue(chargeIasrP2bTotalAmountView.getTotal().doubleValue());
        });
        return descriptiveStatistics;
    }

    public BigDecimal genTotalByYearMonth(final String yearMonth, final List<ChargeIasrP2bTotalAmountView> viewList) {
        BigDecimal result = BigDecimal.ZERO;
        for (ChargeIasrP2bTotalAmountView view : viewList) {
            if (view.getMonth().equals(genCurrentYmdStr(yearMonth))) {
                result = result.add(view.getTotal());
            }
        }
        return result;
    }

    public BigDecimal gen3MonthAverageByYearMonth(final String yearMonth, final List<ChargeIasrP2bTotalAmountView> viewList) {
        BigDecimal result = BigDecimal.ZERO;
        Optional<YearMonth> ymOpt = LocalDateTimeUtils.parseYearMonthFromString(yearMonth, "yyyyMM");
        if (ymOpt.isPresent()) {
            for (int i = 0; i < 3; i++) {
                Optional<String> previousYmdStrOpt = genPreviousYmdStr(yearMonth, i);
                if (previousYmdStrOpt.isPresent()) {
                    for (ChargeIasrP2bTotalAmountView view : viewList) {
                        if (view.getMonth().equals(previousYmdStrOpt.get())) {
                            result = result.add(view.getTotal());
                        }
                    }
                }
            }
        }
        if (result.compareTo(BigDecimal.ZERO) != 0) {
            return result.divide(new BigDecimal(3), BigDecimal.ROUND_HALF_UP).setScale(4);
        } else {
            return result;
        }
    }

    public BigDecimal gen12MonthAverageByYearMonth(final String yearMonth, final List<ChargeIasrP2bTotalAmountView> viewList) {
        BigDecimal result = BigDecimal.ZERO;
        Optional<YearMonth> ymOpt = LocalDateTimeUtils.parseYearMonthFromString(yearMonth, "yyyyMM");
        if (ymOpt.isPresent()) {
            for (int i = 0; i < 12; i++) {
                Optional<String> previousYmdStrOpt = genPreviousYmdStr(yearMonth, i);
                if (previousYmdStrOpt.isPresent()) {
                    for (ChargeIasrP2bTotalAmountView view : viewList) {
                        if (view.getMonth().equals(previousYmdStrOpt.get())) {
                            result = result.add(view.getTotal());
                        }
                    }
                }
            }
        }
        if (result.compareTo(BigDecimal.ZERO) != 0) {
            return result.divide(new BigDecimal(12), BigDecimal.ROUND_HALF_UP).setScale(4);
        } else {
            return result;
        }
    }

    public Optional<String> genPreviousYmStr(String yearMonth, int minus) {
        Optional<YearMonth> ymOpt = LocalDateTimeUtils.parseYearMonthFromString(yearMonth, "yyyyMM");
        if (ymOpt.isPresent()) {
            YearMonth previousYm = ymOpt.get().minusMonths(minus);
            return Optional.of(LocalDateTimeUtils.yearMonthToString(previousYm, "yyyyMM"));
        }
        return Optional.empty();
    }

    public Optional<String> genPreviousYmdStr(String yearMonth, int minus) {
        Optional<String> previousYmStrOpt = genPreviousYmStr(yearMonth, minus);
        if (previousYmStrOpt.isPresent()) {
            return Optional.of(previousYmStrOpt.get() + "01");
        } else {
            return Optional.empty();
        }
    }

    public String genCurrentYmdStr(String yearMonth) {
        return yearMonth + "01";
    }

    public BigDecimal genMonthToMonthByYearMonth(final String yearMonth, final List<ChargeIasrP2bTotalAmountView> viewList) {
        BigDecimal result = BigDecimal.ZERO;
        Optional<String> previousYmStrOpt = genPreviousYmStr(yearMonth, 12);
        if (!previousYmStrOpt.isPresent()) {
            return result;
        }
        BigDecimal currentYmTotal = genTotalByYearMonth(yearMonth, viewList);
        BigDecimal previousYmTotal = genTotalByYearMonth(previousYmStrOpt.get(), viewList);
        //現在減過去除過去
        boolean isCurrentTotalZero = currentYmTotal.compareTo(BigDecimal.ZERO) == 0;
        boolean isPreviousTotalZero = previousYmTotal.compareTo(BigDecimal.ZERO) == 0;
        if (!isCurrentTotalZero && !isPreviousTotalZero) {
            result = (currentYmTotal.subtract(previousYmTotal))
                    .divide(previousYmTotal, RoundingMode.HALF_UP).setScale(4);
        } else if (!isCurrentTotalZero && isPreviousTotalZero) {
            result = new BigDecimal(1.0);
        } else if (isCurrentTotalZero && !isPreviousTotalZero) {
            result = new BigDecimal(-1.0);
        }
        return result.multiply(new BigDecimal(100));
    }

    public BigDecimal genQuarterToQuarterByYearMonth(final String yearMonth, final List<ChargeIasrP2bTotalAmountView> viewList) {
        BigDecimal result = BigDecimal.ZERO;
        Optional<String> previousYmStrOpt = genPreviousYmStr(yearMonth, 12);
        if (!previousYmStrOpt.isPresent()) {
            return result;
        }
        BigDecimal current3mAvg = gen3MonthAverageByYearMonth(yearMonth, viewList);
        BigDecimal previous3mAvg = gen3MonthAverageByYearMonth(previousYmStrOpt.get(), viewList);
        boolean isCurrent3mAvgZero = current3mAvg.compareTo(BigDecimal.ZERO) == 0;
        boolean isPrevious3mAvgZero = previous3mAvg.compareTo(BigDecimal.ZERO) == 0;

        if (!isCurrent3mAvgZero && !isPrevious3mAvgZero) {
            result = (current3mAvg.subtract(previous3mAvg)).divide(previous3mAvg, RoundingMode.HALF_UP).setScale(4);
        } else if (!isCurrent3mAvgZero && isPrevious3mAvgZero) {
            result = new BigDecimal(1.0);
        } else if (isCurrent3mAvgZero && !isPrevious3mAvgZero) {
            result = new BigDecimal(-1.0);
        }
        return result.multiply(new BigDecimal(100));
    }

    public BigDecimal gen3MonthAverageMinus12MonthAverage(final String yearMonth, final List<ChargeIasrP2bTotalAmountView> viewList) {
        BigDecimal result = BigDecimal.ZERO;
        Optional<ChargeIasrP2bTotalAmountView> currentViewOpt =
                viewList.stream().filter(view -> {
                    return view.getMonth().equals(genCurrentYmdStr(yearMonth));
                }).findFirst();
        if (currentViewOpt.isPresent()) {
            BigDecimal current3mAvg = gen3MonthAverageByYearMonth(yearMonth, viewList);
            BigDecimal current12mAvg = gen12MonthAverageByYearMonth(yearMonth, viewList);
            boolean is12mAvgZero = current12mAvg.compareTo(BigDecimal.ZERO) == 0;
            boolean is3mAvgZero = current3mAvg.compareTo(BigDecimal.ZERO) == 0;
            if (!is3mAvgZero && !is12mAvgZero) {
                result = current3mAvg.subtract(current12mAvg).setScale(0, BigDecimal.ROUND_HALF_UP);
            } else if (!is3mAvgZero && is12mAvgZero) {
                result = current3mAvg;
            } else if (is3mAvgZero && !is12mAvgZero) {
                result = current12mAvg.negate();
            }
        }
        return result;
    }

}
