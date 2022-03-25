package unitTest;

import com.gateweb.charge.component.nonAnnotated.CustomInterval;
import com.gateweb.charge.contract.utils.ContractRenewIntervalGenerator;
import com.gateweb.orm.charge.entity.Contract;
import com.gateweb.utils.LocalDateTimeUtils;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Optional;

public class ContractRenewIntervalGenerateTest {

    @Test
    public void contractRenewIntervalGenTest() {
        Optional<LocalDateTime> fromLocalDateTime = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2020-11-02T00:00:00.000"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        );
        Optional<LocalDateTime> toLocalDateTime = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2021-11-01T00:00:00.000"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        );

        Contract contract = new Contract();
        contract.setPeriodMonth(12);
        contract.setEffectiveDate(fromLocalDateTime.get());
        contract.setExpirationDate(toLocalDateTime.get());

        CustomInterval customInterval = ContractRenewIntervalGenerator.genGeneralRenewInterval(contract);

        Optional<LocalDateTime> resultFrom = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2021-11-02T00:00:00.000"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        );
        Optional<LocalDateTime> resultTo = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2022-11-01T23:59:59.000"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        );

        Assert.assertEquals(customInterval.getStartLocalDateTime().toLocalDate().toString(), resultFrom.get().toLocalDate().toString());
        Assert.assertEquals(customInterval.getEndDateTime().toLocalDate().toString(), resultTo.get().toLocalDate().toString());
    }
}
