package unitTest;

import com.gateweb.charge.config.SpringWebMvcConfig;
import com.gateweb.charge.contract.component.ContractRenewComponent;
import com.gateweb.charge.enumeration.ContractStatus;
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

import java.time.LocalDateTime;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringWebMvcConfig.class})
@WebAppConfiguration
@TestPropertySource(properties = {"spring.profiles.active=uat"})
public class RemainingCountContractRenewTest {
    @Autowired
    ContractRenewComponent contractRenewComponent;

    public Contract genContract() {
        Contract contract = new Contract();
        contract.setContractId(13210L);
        contract.setCreateDate(LocalDateTime.now());
        contract.setName("關網");
        contract.setCompanyId(8367L);
        contract.setPackageId(28L);
        contract.setAutoRenew(true);
        Optional<LocalDateTime> effectiveDateOpt = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2022-02-12T00:00:00.000"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        );
        contract.setEffectiveDate(effectiveDateOpt.get());
        Optional<LocalDateTime> expirationDate = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2023-02-11T23:59:59.000"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        );
        contract.setExpirationDate(expirationDate.get());
        contract.setPeriodMonth(12);
        contract.setCreateDate(LocalDateTime.now());
        contract.setStatus(ContractStatus.E);
        return contract;
    }

    @Test
    public void successfulRenewContractCase1() {
        Optional<Contract> renewContractOpt = contractRenewComponent.genRenewRemainingContract(genContract(), "20230211");
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

    @Test
    public void successfulRenewContractCase2() {
        LocalDateTime effectiveDateTime = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2023-02-12T00:00:00.000"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        ).get();
        LocalDateTime expectedEffectiveDateTime = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2023-02-12T00:00:00.000"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        ).get();
        LocalDateTime expectedExpirationDateTime = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2024-02-11T23:59:59.000"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        ).get();
        Optional<Contract> renewContractOpt = contractRenewComponent.genRenewRemainingContract(
                genContract(), effectiveDateTime, expectedExpirationDateTime.plusSeconds(1)
        );
        Assert.assertTrue(renewContractOpt.isPresent());
        Assert.assertTrue(renewContractOpt.get().getAutoRenew());
        Assert.assertEquals(
                expectedEffectiveDateTime
                , renewContractOpt.get().getEffectiveDate());
        Assert.assertEquals(
                expectedExpirationDateTime
                , renewContractOpt.get().getExpirationDate());
    }

    @Test
    public void invalidExecutionDateCase1() {
        LocalDateTime effectiveDateTime = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2023-02-12T00:00:00.000"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        ).get();
        LocalDateTime expectedExpirationDateTime = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2024-02-11T23:59:59.000"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        ).get();
        Optional<Contract> renewContractOpt = contractRenewComponent.genRenewRemainingContract(
                genContract(), effectiveDateTime, expectedExpirationDateTime
        );
        Assert.assertFalse(renewContractOpt.isPresent());
    }
}
