package unitTest;

import com.gateweb.charge.config.SpringWebMvcConfig;
import com.gateweb.charge.contract.remainingCount.component.ContractExpireRenewDataCollectorDispatcher;
import com.gateweb.charge.contract.remainingCount.component.NegativeRemainingRenewDataCollectorDispatcher;
import com.gateweb.charge.contract.remainingCount.component.RemainingContractDispatchDataGenerator;
import com.gateweb.charge.contract.remainingCount.component.RemainingContractRenewDataCollectorDispatcher;
import com.gateweb.orm.charge.entity.InvoiceRemaining;
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
public class RemainingContractDispatchDataGeneratorTest {
    @Autowired
    RemainingContractDispatchDataGenerator remainingContractDispatchDataGenerator;

    public Optional<InvoiceRemaining> genNegativeRecordOpt(String invoiceDate) {
        InvoiceRemaining invoiceRemaining = new InvoiceRemaining();
        invoiceRemaining.setRemaining(-1);
        invoiceRemaining.setInvoiceRemainingId(1L);
        invoiceRemaining.setContractId(1L);
        invoiceRemaining.setInvoiceDate(invoiceDate);
        Optional<LocalDateTime> uploadDateTimeOpt = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2008-08-01T00:00:00.000"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        );
        invoiceRemaining.setUploadDate(uploadDateTimeOpt.get());
        invoiceRemaining.setCreateDate(LocalDateTime.now());
        return Optional.of(invoiceRemaining);
    }

    public Optional<InvoiceRemaining> genExpireRecordOpt(String invoiceDate) {
        InvoiceRemaining invoiceRemaining = new InvoiceRemaining();
        invoiceRemaining.setRemaining(100);
        invoiceRemaining.setInvoiceRemainingId(2L);
        invoiceRemaining.setContractId(1L);
        invoiceRemaining.setInvoiceDate(invoiceDate);
        invoiceRemaining.setCreateDate(LocalDateTime.now());
        return Optional.of(invoiceRemaining);
    }

    @Test
    public void negativeRecordTest1() {
        Optional<RemainingContractRenewDataCollectorDispatcher> collectorOpt =
                remainingContractDispatchDataGenerator.getCollectorDispatcher(
                        genNegativeRecordOpt("20080831"), genExpireRecordOpt("20090831")
                );
        Assert.assertTrue(collectorOpt.isPresent());
        Assert.assertTrue(collectorOpt.get() instanceof NegativeRemainingRenewDataCollectorDispatcher);
    }

    @Test
    public void negativeRecordTest2() {
        Optional<RemainingContractRenewDataCollectorDispatcher> collectorOpt =
                remainingContractDispatchDataGenerator.getCollectorDispatcher(
                        genNegativeRecordOpt("20080831"), Optional.empty());
        Assert.assertTrue(collectorOpt.isPresent());
        Assert.assertTrue(collectorOpt.get() instanceof NegativeRemainingRenewDataCollectorDispatcher);
    }

    @Test
    public void expireRecordTest1() {
        Optional<RemainingContractRenewDataCollectorDispatcher> collectorOpt =
                remainingContractDispatchDataGenerator.getCollectorDispatcher(
                        Optional.empty(), genExpireRecordOpt("20090831")
                );
        Assert.assertTrue(collectorOpt.isPresent());
        Assert.assertTrue(collectorOpt.get() instanceof ContractExpireRenewDataCollectorDispatcher);
    }

    @Test
    public void expireRecordTest2() {
        Optional<RemainingContractRenewDataCollectorDispatcher> collectorOpt =
                remainingContractDispatchDataGenerator.getCollectorDispatcher(
                        genNegativeRecordOpt("20090831"), genExpireRecordOpt("20080831")
                );
        Assert.assertTrue(collectorOpt.isPresent());
        Assert.assertTrue(collectorOpt.get() instanceof ContractExpireRenewDataCollectorDispatcher);
    }

    @Test
    public void expireRecordTest3() {
        Optional<RemainingContractRenewDataCollectorDispatcher> collectorOpt =
                remainingContractDispatchDataGenerator.getCollectorDispatcher(
                        genNegativeRecordOpt("20080931"), genExpireRecordOpt("20080931")
                );
        Assert.assertTrue(collectorOpt.isPresent());
        Assert.assertTrue(collectorOpt.get() instanceof ContractExpireRenewDataCollectorDispatcher);
    }

    @Test
    public void emptyTest() {
        Optional<RemainingContractRenewDataCollectorDispatcher> collectorOpt =
                remainingContractDispatchDataGenerator.getCollectorDispatcher(
                        Optional.empty(), Optional.empty()
                );
        Assert.assertFalse(collectorOpt.isPresent());
    }
}
