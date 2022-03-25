package com.gateweb.charge.feeCalculation.billingItemGenerator;

import com.gateweb.orm.charge.entity.Bill;
import com.gateweb.orm.charge.entity.BillingItem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class BillingItemFeeCalculator {

    /**
     * 兩種算法不一樣
     * 台灣稅率為內含稅率
     * 因此其結果非商品各自計算稅額，而是加總後計算稅額再以比例分配給這些商品
     * 外國稅率為外加稅計算，因此商品可以各自擁有不同的稅率
     *
     * @param billingItemCollection
     * @return
     */
    public void calculateBillingItemCollection(Bill bill, Collection<BillingItem> billingItemCollection) {
        //預設內含稅率
        if (bill.getTwTax() == null) {
            bill.setTwTax(true);
        }
        //使用map總計稅額
        HashMap<BigDecimal, BigDecimal> taxRateExcludedAmountMap = new HashMap<>();
        billingItemCollection.stream().forEach(billingItem -> {
            if (!bill.getTwTax()
                    && billingItem.getTaxExcludedAmount() != null) {
                billingItem.setTaxIncludedAmount(
                        billingItem.getTaxExcludedAmount().multiply(
                                BigDecimal.ONE.add(billingItem.getTaxRate())
                        )
                );
                billingItem.setTaxAmount(
                        billingItem.getTaxIncludedAmount().subtract(
                                billingItem.getTaxExcludedAmount()
                        )
                );
            } else {
                if (taxRateExcludedAmountMap.containsKey(billingItem.getTaxRate())) {
                    BigDecimal excludedAmountSum = taxRateExcludedAmountMap.get(billingItem.getTaxRate()).add(billingItem.getTaxExcludedAmount());
                    taxRateExcludedAmountMap.put(billingItem.getTaxRate(), excludedAmountSum);
                } else {
                    taxRateExcludedAmountMap.put(billingItem.getTaxRate(), billingItem.getTaxExcludedAmount());
                }
            }
        });
        if (bill.getTwTax() != null && bill.getTwTax()) {
            HashMap<BigDecimal, BigDecimal> taxRateTaxAmountMap = new HashMap<>();
            //取得稅額總計
            BigDecimal rateTaxAmountSum = BigDecimal.ZERO;
            BigDecimal amountSum = BigDecimal.ZERO;
            for (Map.Entry<BigDecimal, BigDecimal> entry : taxRateExcludedAmountMap.entrySet()) {
                BigDecimal taxRate = entry.getKey();
                BigDecimal rateTaxAmount = taxRateExcludedAmountMap.get(taxRate).multiply(taxRate);
                if (taxRateTaxAmountMap.containsKey(taxRate)) {
                    rateTaxAmountSum = taxRateTaxAmountMap.get(taxRate).add(rateTaxAmount);
                }
                taxRateTaxAmountMap.put(
                        taxRate
                        , rateTaxAmount
                );
                rateTaxAmountSum = rateTaxAmountSum.add(rateTaxAmount);
                amountSum = amountSum.add(taxRateExcludedAmountMap.get(taxRate));
            }
            BigDecimal itemTaxSum = BigDecimal.ZERO;
            HashMap<Integer, BillingItem> decimalValueMap = new HashMap<>();
            for (BillingItem billingItem : billingItemCollection) {
                //乘回比例
                billingItem.setTaxAmount(
                        billingItem.getTaxExcludedAmount().multiply(billingItem.getTaxRate())
                );
                billingItem.setTaxIncludedAmount(
                        billingItem.getTaxExcludedAmount().add(billingItem.getTaxAmount())
                );
                itemTaxSum = itemTaxSum.add(billingItem.getTaxAmount());
                String decimalString = billingItem.getTaxAmount()
                        .stripTrailingZeros().setScale(4, RoundingMode.HALF_UP)
                        .toPlainString();
                int decimalIndex = decimalString.indexOf(".") + 1;
                if (decimalIndex > 0) {
                    decimalString = decimalString.substring(decimalIndex);
                } else {
                    decimalString = "0";
                }
                Integer decimalValue = Integer.parseInt(decimalString);
                decimalValueMap.put(decimalValue, billingItem);
            }
            //檢查是否有稅差
            if (rateTaxAmountSum.compareTo(itemTaxSum) != 0) {
                BigDecimal diff = rateTaxAmountSum.subtract(itemTaxSum);
                //找到小數點以後值最大的該項
                BillingItem targetBillingItem =
                        decimalValueMap.get(
                                decimalValueMap.keySet().stream().sorted().collect(Collectors.toList()).get(0)
                        );
                //加回稅差
                targetBillingItem.setTaxIncludedAmount(
                        targetBillingItem.getTaxIncludedAmount().add(diff)
                );
            }
            bill.setTaxAmount(rateTaxAmountSum);
            bill.setTaxExcludedAmount(amountSum);
            bill.setTaxIncludedAmount(rateTaxAmountSum.add(amountSum));
        }
    }
}
