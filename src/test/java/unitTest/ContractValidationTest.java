package unitTest;

import com.gateweb.charge.config.SpringWebMvcConfig;
import com.gateweb.charge.contract.component.ContractValidationComponent;
import com.gateweb.charge.enumeration.ContractStatus;
import com.gateweb.orm.charge.entity.Contract;
import org.eclipse.core.runtime.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDateTime;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringWebMvcConfig.class})
@WebAppConfiguration
@Category(ContractPeriodicFeeCalculatorTest.class)
@TestPropertySource(properties = {"spring.profiles.active=test"})
public class ContractValidationTest {
    @Autowired
    ContractValidationComponent contractValidationComponent;

    public Contract genInstallationDateType1() {
        Contract contract = new Contract();
        contract.setStatus(ContractStatus.C);
        contract.setFirstInvoiceDateAsEffectiveDate(false);
        contract.setInstallationDate(LocalDateTime.now());
        return contract;
    }

    public Contract genInstallationDateType2() {
        Contract contract = new Contract();
        contract.setStatus(ContractStatus.E);
        contract.setFirstInvoiceDateAsEffectiveDate(false);
        contract.setInstallationDate(LocalDateTime.now());
        return contract;
    }

    public Contract genFirstInvoiceDateAsEffectiveDateType1() {
        Contract contract = new Contract();
        contract.setStatus(ContractStatus.E);
        contract.setFirstInvoiceDateAsEffectiveDate(true);
        return contract;
    }

    public Contract genFirstInvoiceDateAsEffectiveDateType2() {
        Contract contract = new Contract();
        contract.setStatus(ContractStatus.C);
        contract.setFirstInvoiceDateAsEffectiveDate(true);
        return contract;
    }

    public Contract genMixType1() {
        Contract contract = new Contract();
        contract.setStatus(ContractStatus.E);
        contract.setFirstInvoiceDateAsEffectiveDate(true);
        contract.setInstallationDate(LocalDateTime.now());
        return contract;
    }

    public Contract genMixType2() {
        Contract contract = new Contract();
        contract.setStatus(ContractStatus.C);
        contract.setFirstInvoiceDateAsEffectiveDate(true);
        contract.setInstallationDate(LocalDateTime.now());
        return contract;
    }

    public Contract genUpdateType1() {
        Contract contract = new Contract();
        contract.setEffectiveDate(LocalDateTime.now().minusDays(1));
        contract.setExpirationDate(LocalDateTime.now());
        contract.setStatus(ContractStatus.E);
        contract.setFirstInvoiceDateAsEffectiveDate(true);
        contract.setInstallationDate(LocalDateTime.now());
        return contract;
    }

    public Contract genUpdateType2() {
        Contract contract = new Contract();
        contract.setEffectiveDate(LocalDateTime.now().minusDays(1));
        contract.setExpirationDate(LocalDateTime.now());
        contract.setStatus(ContractStatus.C);
        contract.setFirstInvoiceDateAsEffectiveDate(true);
        contract.setInstallationDate(LocalDateTime.now());
        return contract;
    }

    @Test
    public void installationDateTypeTest() {
        Contract type1 = genInstallationDateType1();
        Contract type2 = genInstallationDateType2();
        Assert.isTrue(contractValidationComponent.contractTypeValidation(type1));
        Assert.isTrue(!contractValidationComponent.contractTypeValidation(type2));
    }

    @Test
    public void mixTypeTest() {
        Contract mixType1 = genMixType1();
        Contract mixType2 = genMixType2();
        Assert.isTrue(!contractValidationComponent.contractTypeValidation(mixType1));
        Assert.isTrue(contractValidationComponent.contractTypeValidation(mixType2));
    }

    @Test
    public void firstInvoiceAsEffectiveDateTest() {
        Contract type1 = genFirstInvoiceDateAsEffectiveDateType1();
        Contract type2 = genFirstInvoiceDateAsEffectiveDateType2();
        Assert.isTrue(!contractValidationComponent.contractTypeValidation(type1));
        Assert.isTrue(contractValidationComponent.contractTypeValidation(type2));
    }

    @Test
    public void updateType1Test() {
        Contract type1 = genUpdateType1();
        Assert.isTrue(contractValidationComponent.contractTypeValidation(type1));
    }

    @Test
    public void updateType2Test() {
        Contract type2 = genUpdateType2();
        Assert.isTrue(!contractValidationComponent.contractTypeValidation(type2));
    }

}
