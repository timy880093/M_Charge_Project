package unitTest;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.quartz.CronExpression;
import org.springframework.scheduling.support.CronSequenceGenerator;

import java.text.ParseException;
import java.util.Date;

@Category(CronExpressionUnitTest.class)
public class CronExpressionUnitTest {

    @Test
    public void chargeFeeByUnitPriceUnitTest1() throws ParseException {
        String cronExpression1 = "0 0 1 * * FRI";
        String cronExpression2 = "1 1 1 15 * ?";
        String cronExpression3 = "1 1 1 16 FEB,APR,JUN,AUG,OCT,DEC ? ";
        boolean validExpression1 = CronSequenceGenerator.isValidExpression(cronExpression1);
        boolean validExpression2 = CronSequenceGenerator.isValidExpression(cronExpression2);
        boolean validExpression3 = CronSequenceGenerator.isValidExpression(cronExpression3);
        CronExpression exp = new CronExpression(cronExpression2);
        Date nextExecutionTime = exp.getNextValidTimeAfter(new Date());
        System.out.println(nextExecutionTime.toString());
        Assert.assertEquals(true, validExpression1);
        Assert.assertEquals(true, validExpression2);
        Assert.assertEquals(true, validExpression3);
    }
}
