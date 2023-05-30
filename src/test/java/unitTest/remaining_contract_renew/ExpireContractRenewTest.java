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
public class ExpireContractRenewTest {
    @Autowired
    ContractRenewComponent contractRenewComponent;

    @Test
    public void expireRemainingRenewContract_ExpirationDateIsAfterCurrentDateTime() {
        Clock clock = Clock.fixed(Instant.parse("2023-02-11T23:59:58.000Z"), ZoneId.of("UTC"));
        LocalDateTime effectiveDateTime = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2023-02-12T00:00:00.000"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        ).get();
        Optional<Contract> renewContractOpt = contractRenewComponent.genExpireRemainingRenewContract(
                TestContractProvider.genValidContract(), effectiveDateTime, clock
        );
        Assert.assertFalse(renewContractOpt.isPresent());
    }

    @Test
    public void expireRemainingRenewContract_WithNullPeriodMonth() {
        Clock clock = Clock.fixed(Instant.parse("2023-02-13T23:59:58.000Z"), ZoneId.of("UTC"));
        LocalDateTime effectiveDateTime = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2023-02-12T00:00:00.000"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        ).get();
        Optional<Contract> renewContractOpt = contractRenewComponent.genExpireRemainingRenewContract(
                TestContractProvider.genNullPeriodMonthContract(), effectiveDateTime, clock
        );
        Assert.assertFalse(renewContractOpt.isPresent());
    }

    @Test
    public void expireRemainingRenewContract_WithValidEffectiveDateContract() {
        LocalDateTime effectiveDateTime = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2023-02-12T00:00:00.000"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        ).get();
        Optional<Contract> renewContractOpt = contractRenewComponent.genExpireRemainingRenewContract(
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
