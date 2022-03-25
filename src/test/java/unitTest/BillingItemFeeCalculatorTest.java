package unitTest;

import com.gateweb.charge.feeCalculation.billingItemGenerator.BillingItemFeeCalculator;
import com.gateweb.orm.charge.entity.Bill;
import com.gateweb.orm.charge.entity.BillingItem;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class BillingItemFeeCalculatorTest {
    final BillingItemFeeCalculator billingItemFeeCalculator = new BillingItemFeeCalculator();

    public BillingItem testBillingItem1() {
        BillingItem billingItem = new BillingItem();
        billingItem.setTaxRate(new BigDecimal(0.05));
        billingItem.setTaxExcludedAmount(new BigDecimal(8.4));
        return billingItem;
    }

    public BillingItem testBillingItem2() {
        BillingItem billingItem = new BillingItem();
        billingItem.setTaxRate(new BigDecimal(0.05));
        billingItem.setTaxExcludedAmount(new BigDecimal(24.15));
        return billingItem;
    }

    public BillingItem testBillingItem3() {
        BillingItem billingItem = new BillingItem();
        billingItem.setTaxRate(new BigDecimal(0.05));
        billingItem.setTaxExcludedAmount(new BigDecimal(38.85));
        return billingItem;
    }

    public BillingItem testBillingItem4() {
        BillingItem billingItem = new BillingItem();
        billingItem.setTaxRate(new BigDecimal(0.05));
        billingItem.setTaxExcludedAmount(new BigDecimal(39.375));
        return billingItem;
    }

    public BillingItem testBillingItem5() {
        BillingItem billingItem = new BillingItem();
        billingItem.setTaxRate(new BigDecimal(0.05));
        billingItem.setTaxExcludedAmount(new BigDecimal(59.325));
        return billingItem;
    }

    public Set<BillingItem> getTestDataSet() {
        Set<BillingItem> billingItemSet = new HashSet<>();
        billingItemSet.add(testBillingItem1());
        billingItemSet.add(testBillingItem2());
        billingItemSet.add(testBillingItem3());
        billingItemSet.add(testBillingItem4());
        billingItemSet.add(testBillingItem5());
        return billingItemSet;
    }

    public Bill twTaxBill() {
        Bill bill = new Bill();
        bill.setTwTax(true);
        bill.setTaxRate(new BigDecimal(0.05));
        return bill;
    }

    @Test
    public void billTest1() {
        Bill bill = twTaxBill();
        billingItemFeeCalculator.calculateBillingItemCollection(bill, getTestDataSet());
        BigDecimal result = bill.getTaxIncludedAmount().setScale(0, BigDecimal.ROUND_HALF_UP);
        Assert.assertEquals(179, result.intValue());
    }
}
