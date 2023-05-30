package unitTest.remaining_contract_renew;

import com.gateweb.charge.config.SpringWebMvcConfig;
import com.gateweb.charge.contract.component.ContractRenewComponent;
import com.gateweb.orm.charge.entity.Contract;
import com.gateweb.utils.LocalDateTimeUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import static unitTest.remaining_contract_renew.TestContractProvider.genValidContract;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringWebMvcConfig.class})
@WebAppConfiguration
@TestPropertySource(properties = {"spring.profiles.active=uat"})
public class NegativeCountContractRenewTest {
    @Autowired
    ContractRenewComponent contractRenewComponent;

    @Test
    public void negativeRemainingRenewContract_WithNullPeriodMonth() {
        LocalDateTime effectiveDateTime = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2023-02-12T00:00:00.000"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        ).get();
        Optional<Contract> renewContractOpt = contractRenewComponent.genNegativeRemainingRenewContract(
                TestContractProvider.genNullPeriodMonthContract(), effectiveDateTime
        );
        Assert.assertFalse(renewContractOpt.isPresent());
    }

    @Test
    public void negativeRemainingRenewContract_WithValidEffectiveDateContract() {
        LocalDateTime effectiveDateTime = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2023-02-12T00:00:00.000"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        ).get();
        Optional<Contract> renewContractOpt = contractRenewComponent.genNegativeRemainingRenewContract(
                genValidContract(), effectiveDateTime
        );
        Assert.assertTrue(renewContractOpt.isPresent());
        Assert.assertTrue(renewContractOpt.get().getAutoRenew());
        Assert.assertEquals(
                LocalDateTimeUtils.parseLocalDateTimeFromString(
                        "2023-02-12T00:00:00.000"
                        , "yyyy-MM-dd'T'HH:mm:ss.SSS"
                ).get()
                , renewContractOpt.get().getEffectiveDate());
        Assert.assertEquals(
                LocalDateTimeUtils.parseLocalDateTimeFromString(
                        "2024-02-11T23:59:59.000"
                        , "yyyy-MM-dd'T'HH:mm:ss.SSS"
                ).get()
                , renewContractOpt.get().getExpirationDate());
    }
}
