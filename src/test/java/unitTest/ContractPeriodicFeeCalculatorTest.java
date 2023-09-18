package unitTest;

import com.gateweb.charge.infrastructure.nonAnnotated.CustomInterval;
import com.gateweb.charge.config.SpringWebMvcConfig;
import com.gateweb.charge.contract.component.ContractPeriodicFeeCalculator;
import com.gateweb.utils.LocalDateTimeUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringWebMvcConfig.class})
@WebAppConfiguration
@TestPropertySource(properties = {"spring.profiles.active=test"})
public class ContractPeriodicFeeCalculatorTest {


    @Autowired
    ContractPeriodicFeeCalculator contractPeriodicFeeCalculator;

    @Test
    public void overageIntervalSliceCountTest() {
        Set<CustomInterval> customIntervalSet = contractPeriodicFeeCalculator.getOverageCalculateIntervalByYmStr("202003");
        Assert.assertEquals(customIntervalSet.size(), 2);
    }

    @Test
    public void overageIntervalSliceCountTest2() {
        Set<CustomInterval> customIntervalSet = contractPeriodicFeeCalculator.getOverageCalculateIntervalByYmStr("202002");
        Assert.assertEquals(customIntervalSet.size(), 0);
    }

    @Test
    public void overageIntervalSliceRangeTest() {
        LocalDateTime currentLocalDateTime = LocalDateTime.now();
        for (int i = 1; i <= 15; i += 2) {
            overageIntervalSliceRangeTestByLocalDateTime(currentLocalDateTime.plusMonths(i));
        }
    }

    public void overageIntervalSliceRangeTestByLocalDateTime(LocalDateTime localDateTime) {
        Optional<String> ymStrOpt = LocalDateTimeUtils.parseLocalDateTimeToString(localDateTime, "yyyyMM");
        if (ymStrOpt.isPresent()) {
            overageIntervalSliceRangeTestByStr(ymStrOpt.get());
        }
    }

    public void overageIntervalSliceRangeTestByStr(String ymStr) {
        Set<CustomInterval> customIntervalSet = contractPeriodicFeeCalculator.getOverageCalculateIntervalByYmStr(ymStr);
        int ymInteger = Integer.parseInt(ymStr);
        if (ymInteger % 2 != 0) {
            Assert.assertEquals(customIntervalSet.size(), 2);
            customIntervalSet.stream().forEach(sliceInterval -> {
                int currentStartMonth = sliceInterval.getStartDateTime().getMonthOfYear();
                int prevStartMonth = sliceInterval.getStartDateTime().minusSeconds(1).getMonthOfYear();
                int currentEndMonth = sliceInterval.getEndDateTime().getMonthOfYear();
                int nextEndMonth = sliceInterval.getEndDateTime().plusSeconds(1).getMonthOfYear();
                Assert.assertNotSame(currentStartMonth, prevStartMonth);
                Assert.assertNotSame(currentEndMonth, nextEndMonth);
            });
        }
    }
}
