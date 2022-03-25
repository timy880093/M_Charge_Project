package unitTest;

import com.gateweb.charge.report.ctbc.CtbcVirtualAccountGenerator;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Arrays;
import java.util.List;

@Category(ContractPeriodicFeeCalculatorTest.class)
public class CtbcVirtualAccountGeneratorTest {
    CtbcVirtualAccountGenerator ctbcVirtualAccountGenerator = new CtbcVirtualAccountGenerator();

    @Test
    public void virtualAccountTest1() {
        Integer result = ctbcVirtualAccountGenerator.getCheckSum("81472", "89140303").get();
        Assert.assertEquals(new Integer(4), result);
    }

    @Test
    public void virtualAccountTest2() {
        Integer result = ctbcVirtualAccountGenerator.getCheckSum("84285", "07932921").get();
        Assert.assertEquals(new Integer(6), result);
    }

    @Test
    public void virtualAccountTest3() {
        Integer result = ctbcVirtualAccountGenerator.getCheckSum("84285", "25172710").get();
        Assert.assertEquals(new Integer(0), result);
    }

    @Test
    public void virtualAccountTest4() {
        Integer result = ctbcVirtualAccountGenerator.getCheckSum("84285", "79172809").get();
        Assert.assertEquals(new Integer(0), result);
    }

    @Test
    public void ctbcVirtualAccountTest10() {
        List<String> businessNoList = Arrays.asList(new String[]{
                "07932921"
                , "16423883"
                , "22703567"
                , "23836805"
                , "24665717"
                , "28557915"
                , "53332936"
                , "54339063"
                , "54672010"
                , "84682633"
        });
        businessNoList.stream().forEach(businessNo -> {
            genAndPrintCtbcVirtualAccount("84285", businessNo);
        });
    }

    public void genAndPrintCtbcVirtualAccount(String businessId, String businessNo) {
        Integer result = ctbcVirtualAccountGenerator.getCheckSum(businessId, businessNo).get();
        System.out.println(businessId + "," + businessNo + "," + result);
    }
}
