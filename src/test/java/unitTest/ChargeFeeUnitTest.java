package unitTest;

import com.gateweb.charge.chargePolicy.calculateRule.type.ACMixTypeCalculateRule;
import com.gateweb.charge.chargePolicy.calculateRule.type.ATypeCalculateRule;
import com.gateweb.charge.chargePolicy.calculateRule.type.CalculateRule;
import com.gateweb.charge.chargePolicy.calculateRule.type.GeneralCalculateRule;
import com.gateweb.orm.charge.entity.NewGrade;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.math.BigDecimal;
import java.util.LinkedList;

@Category(ChargeFeeUnitTest.class)
public class ChargeFeeUnitTest {
    LinkedList<NewGrade> gradeLinkedList = new LinkedList<>();

    @Test
    public void chargeFeeByUnitPriceGradeTable() {
        NewGrade gradeTable = new NewGrade();
        gradeTable.setUnitPrice(new BigDecimal(0.5));
        gradeTable.setCntStart(251);
        gradeTable.setCntEnd(999999999);
        gradeLinkedList.add(gradeTable);

        CalculateRule calculateRule = new GeneralCalculateRule();
        BigDecimal calculateResult = calculateRule.calculateFee(1426, gradeLinkedList);
        Integer chargeableCount = calculateRule.chargeableCount(1426, gradeLinkedList);
        Assert.assertEquals(588, calculateResult.intValue());
        Assert.assertEquals(1176, chargeableCount.intValue());
    }

    @Test
    public void fixPriceAccumulationTest() {
        NewGrade gradeTable = new NewGrade();
        gradeTable.setFixPrice(new BigDecimal(760));
        gradeTable.setCntStart(18001);
        gradeTable.setCntEnd(36000);
        gradeLinkedList.add(gradeTable);

        NewGrade gradeTable2 = new NewGrade();
        gradeTable2.setFixPrice(new BigDecimal(760));
        gradeTable2.setCntStart(36001);
        gradeTable2.setCntEnd(54000);
        gradeLinkedList.add(gradeTable2);

        CalculateRule calculateRule = new ATypeCalculateRule();
        BigDecimal result = calculateRule.calculateFee(36067, gradeLinkedList);
        int chargeableCount = calculateRule.chargeableCount(36067, gradeLinkedList);
        Assert.assertEquals(18067, chargeableCount);
        Assert.assertEquals(1520, result.intValue());
    }

    @Test
    public void chargeAccumulationUnitTest() {
        NewGrade gradeTable = new NewGrade();
        gradeTable.setFixPrice(new BigDecimal(800));
        gradeTable.setCntStart(0);
        gradeTable.setCntEnd(1000);
        gradeLinkedList.add(gradeTable);

        CalculateRule calculateRule = new ATypeCalculateRule();
        BigDecimal result = calculateRule.calculateFee(1426, gradeLinkedList);
        int chargeableCount = calculateRule.chargeableCount(1426, gradeLinkedList);
        Assert.assertEquals(1000, chargeableCount);
        Assert.assertEquals(800, result.intValue());
    }

    @Test
    public void chargeAccumulationUnitTestMiddle() {
        NewGrade gradeTable = new NewGrade();
        gradeTable.setFixPrice(new BigDecimal(800));
        gradeTable.setCntStart(251);
        gradeTable.setCntEnd(600);
        gradeLinkedList.add(gradeTable);

        CalculateRule calculateRule = new ATypeCalculateRule();
        BigDecimal result = calculateRule.calculateFee(1426, gradeLinkedList);
        int chargeableCount = calculateRule.chargeableCount(1426, gradeLinkedList);
        Assert.assertEquals(350, chargeableCount);
        Assert.assertEquals(800, result.intValue());
    }

    @Test
    public void chargeAccumulationUnitTestNotYet() {
        NewGrade gradeTable = new NewGrade();
        gradeTable.setFixPrice(new BigDecimal(800));
        gradeTable.setCntStart(251);
        gradeTable.setCntEnd(600);
        gradeLinkedList.add(gradeTable);

        CalculateRule calculateRule = new ATypeCalculateRule();
        BigDecimal result = calculateRule.calculateFee(180, gradeLinkedList);
        int chargeableCount = calculateRule.chargeableCount(180, gradeLinkedList);
        Assert.assertEquals(0, chargeableCount);
        Assert.assertEquals(0, result.intValue());
    }

    @Test
    public void chargeCirculationTest() {
        NewGrade gradeTable = new NewGrade();
        gradeTable.setFixPrice(new BigDecimal(800));
        gradeTable.setCntStart(0);
        gradeTable.setCntEnd(1000);
        gradeLinkedList.add(gradeTable);

        CalculateRule calculateRule = new ACMixTypeCalculateRule();
        BigDecimal result = calculateRule.calculateFee(1426, gradeLinkedList);
        int chargeableCount = calculateRule.chargeableCount(1426, gradeLinkedList);
        Assert.assertEquals(1000, chargeableCount);
        Assert.assertEquals(1600, result.intValue());
    }

    @Test
    public void chargeCirculationTest2() {
        NewGrade gradeTable = new NewGrade();
        gradeTable.setFixPrice(new BigDecimal(800));
        gradeTable.setCntStart(0);
        gradeTable.setCntEnd(1000);
        gradeLinkedList.add(gradeTable);

        CalculateRule calculateRule = new ACMixTypeCalculateRule();
        BigDecimal result = calculateRule.calculateFee(0, gradeLinkedList);
        int chargeableCount = calculateRule.chargeableCount(0, gradeLinkedList);
        Assert.assertEquals(0, chargeableCount);
        Assert.assertEquals(800, result.intValue());
    }

    @Test
    public void fixPriceRentalTest() {
        NewGrade gradeTable = new NewGrade();
        gradeTable.setUnitPrice(new BigDecimal(200));
        gradeTable.setCntStart(0);
        gradeTable.setCntEnd(250);
        gradeLinkedList.add(gradeTable);

        CalculateRule calculateRule = new ACMixTypeCalculateRule();
        BigDecimal result = calculateRule.calculateFee(0, gradeLinkedList);
        int chargeableCount = calculateRule.chargeableCount(0, gradeLinkedList);
        Assert.assertEquals(0, chargeableCount);
        Assert.assertEquals(200, result.intValue());
    }

    @Test
    public void acMixTypeCalculateRuleTest1() {
        NewGrade root = new NewGrade();
        root.setUnitPrice(new BigDecimal(0.5));
        root.setCntStart(251);
        root.setCntEnd(1450);
        gradeLinkedList.add(root);

        NewGrade level1 = new NewGrade();
        level1.setFixPrice(new BigDecimal(0));
        level1.setCntStart(1451);
        level1.setCntEnd(15000);
        gradeLinkedList.add(level1);

        NewGrade level2 = new NewGrade();
        level2.setFixPrice(new BigDecimal(760));
        level2.setCntStart(15001);
        level2.setCntEnd(30000);
        gradeLinkedList.add(level2);

        NewGrade level3 = new NewGrade();
        level3.setFixPrice(new BigDecimal(760));
        level3.setCntStart(30001);
        level3.setCntEnd(45000);
        gradeLinkedList.add(level3);

        CalculateRule calculateRule = new ATypeCalculateRule();
        BigDecimal result0 = calculateRule.calculateFee(180, gradeLinkedList);
        int chargeableCount = calculateRule.chargeableCount(180, gradeLinkedList);
        Assert.assertEquals(0, chargeableCount);
        Assert.assertEquals(0, result0.intValue());

        BigDecimal result1 = calculateRule.calculateFee(301, gradeLinkedList);
        int chargeableCount1 = calculateRule.chargeableCount(301, gradeLinkedList);
        Assert.assertEquals(51, chargeableCount1);
        Assert.assertEquals(new BigDecimal(25.5), result1);

        BigDecimal result1_1 = calculateRule.calculateFee(3456, gradeLinkedList);
        int chargeableCount1_1 = calculateRule.chargeableCount(3456, gradeLinkedList);
        Assert.assertEquals(3206, chargeableCount1_1);
        Assert.assertEquals(600, result1_1.intValue());

        BigDecimal result2 = calculateRule.calculateFee(12345, gradeLinkedList);
        int chargeableCount2 = calculateRule.chargeableCount(12345, gradeLinkedList);
        Assert.assertEquals(12095, chargeableCount2);
        Assert.assertEquals(600, result2.intValue());

        BigDecimal result3 = calculateRule.calculateFee(23456, gradeLinkedList);
        int chargeableCount3 = calculateRule.chargeableCount(23456, gradeLinkedList);
        Assert.assertEquals(23206, chargeableCount3);
        Assert.assertEquals(1360, result3.intValue());
    }

    @Test
    public void package200FeeUnitTest() {
        NewGrade gradeTable = new NewGrade();
        gradeTable.setUnitPrice(new BigDecimal(0.5).setScale(2));
        gradeTable.setCntStart(251);
        gradeTable.setCntEnd(1450);
        gradeLinkedList.add(gradeTable);

        CalculateRule calculateRule = new ATypeCalculateRule();
        BigDecimal result = calculateRule.calculateFee(354, gradeLinkedList);
        int chargeableCount = calculateRule.chargeableCount(354, gradeLinkedList);
        Assert.assertEquals(104, chargeableCount);
        Assert.assertEquals(52, result.intValue());
    }

    /**
     * 馬上開年繳測試
     */
    @Test
    public void packageImTest() {
        LinkedList<NewGrade> gradeLinkedList = new LinkedList<>();
        NewGrade grade1 = new NewGrade();
        grade1.setUnitPrice(new BigDecimal(0.5).setScale(2));
        grade1.setCntStart(251);
        grade1.setCntEnd(1450);

        NewGrade grade2 = new NewGrade();
        grade2.setFixPrice(BigDecimal.ZERO);
        grade2.setCntStart(1451);
        grade2.setCntEnd(15000);

        NewGrade grade3 = new NewGrade();
        grade3.setFixPrice(new BigDecimal(15001));
        grade3.setCntStart(15001);
        grade3.setCntEnd(30000);

        gradeLinkedList.add(grade1);
        gradeLinkedList.add(grade2);
        gradeLinkedList.add(grade3);
        CalculateRule calculateRule = new ATypeCalculateRule();
        BigDecimal result = calculateRule.calculateFee(5412, gradeLinkedList);
        int chargeableCount = calculateRule.chargeableCount(354, gradeLinkedList);
        System.out.println(result);
    }

}
