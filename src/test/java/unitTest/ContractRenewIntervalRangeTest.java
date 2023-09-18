package unitTest;

import com.gateweb.charge.infrastructure.nonAnnotated.CustomInterval;
import com.gateweb.charge.config.SpringWebMvcConfig;
import com.gateweb.charge.contract.component.ContractRenewComponent;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringWebMvcConfig.class})
@WebAppConfiguration
@Category(ContractPeriodicFeeCalculatorTest.class)
@TestPropertySource(properties = {"spring.profiles.active=test"})
public class ContractRenewIntervalRangeTest {
    @Autowired
    ContractRenewComponent contractRenewComponent;

    /**
     * sql中的between因為其區間為最後一天的剛開始，因此不會包含最後一天本身
     *
     */
    @Test
    public void renewIntervalCheckNormal() {
        Optional<CustomInterval> customIntervalOpt = contractRenewComponent.getRenewIntervalByYmStr("202111");
        if (customIntervalOpt.isPresent()) {
            Assert.assertEquals("2022-01-01T00:00", customIntervalOpt.get().getStartLocalDateTime().toString());
            Assert.assertEquals("2022-01-31T23:59:59", customIntervalOpt.get().getEndLocalDateTime().toString());
        }
    }

    @Test
    public void renewIntervalAcrossYear() {
        Optional<CustomInterval> customIntervalOpt = contractRenewComponent.getRenewIntervalByYmStr("202112");
        if (customIntervalOpt.isPresent()) {
            Assert.assertEquals("2022-02-01T00:00", customIntervalOpt.get().getStartLocalDateTime().toString());
            Assert.assertEquals("2022-02-28T23:59:59", customIntervalOpt.get().getEndLocalDateTime().toString());
        }
    }

    @Test
    public void renewIntervalAcrossYear1() {
        Optional<CustomInterval> customIntervalOpt = contractRenewComponent.getRenewIntervalByYmStr("202201");
        if (customIntervalOpt.isPresent()) {
            Assert.assertEquals("2022-03-01T00:00", customIntervalOpt.get().getStartLocalDateTime().toString());
            Assert.assertEquals("2022-03-31T23:59:59", customIntervalOpt.get().getEndLocalDateTime().toString());
        }
    }

    @Test
    public void renewIntervalTest3(){
        Optional<CustomInterval> customIntervalOpt = contractRenewComponent.getRenewIntervalByYmStr("202206");
        if (customIntervalOpt.isPresent()) {
            Assert.assertEquals("2022-08-01T00:00", customIntervalOpt.get().getStartLocalDateTime().toString());
            Assert.assertEquals("2022-08-31T23:59:59", customIntervalOpt.get().getEndLocalDateTime().toString());
        }
    }
}
