package unitTest;

import com.gateweb.charge.chargePolicy.cycle.builder.*;
import com.gateweb.charge.component.nonAnnotated.CustomInterval;
import com.gateweb.charge.contract.utils.ContractRenewIntervalGenerator;
import com.gateweb.utils.LocalDateTimeUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Category(CustomIntervalUnitTest.class)
public class CustomIntervalUnitTest {

    @Test
    public void cronPatternIntervalPartitionTest1() {

        Optional<LocalDateTime> fromLocalDateTime = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2019-07-01T00:00:00.000"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        );

        Optional<LocalDateTime> toLocalDateTime = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2020-06-30T00:00:00.000"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        );

        CustomInterval customInterval = new CustomInterval(
                fromLocalDateTime.get()
                , toLocalDateTime.get()
        );

        CronCycle cronCycle = new CronCycle();
        cronCycle.buildCycle(" 0 0 0 1 * ?");

        List<CustomInterval> partitionIntervalList = cronCycle.doPartitioning(customInterval);
        Assert.assertEquals(12, partitionIntervalList.size());
    }

    @Test
    public void cronPatternIntervalPartitionTest1_1() {

        Optional<LocalDateTime> fromLocalDateTime = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2019-07-01T00:00:00.000"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        );
        Optional<LocalDateTime> toLocalDateTime = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2020-06-30T00:00:00.000"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        );

        CustomInterval customInterval = new CustomInterval(
                fromLocalDateTime.get()
                , toLocalDateTime.get()
        );

        CronCycle cronCycle = new CronCycle();
        /**
         * 千萬不要讓這種算秒的cron被用在發票計數，2019-07不管幾秒都是201907
         * 如果出現在兩個區間中會算到兩次
         */
        cronCycle.buildCycle(" 1 0 0 1 * ?");

        List<CustomInterval> partitionIntervalList = cronCycle.doPartitioning(customInterval);
        Assert.assertEquals(12, partitionIntervalList.size());
    }

    @Test
    public void cronPatternIntervalPartitionTest2() {
        Optional<LocalDateTime> fromLocalDateTime = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2019-07-01T00:00:00.000"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        );
        Optional<LocalDateTime> toLocalDateTime = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2020-06-30T00:00:00.000"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        );
        CustomInterval customInterval = new CustomInterval(
                fromLocalDateTime.get()
                , toLocalDateTime.get()
        );

        ChargeCycle monthCycle = new MonthCycle(1);
        List<CustomInterval> partitionIntervalList = monthCycle.doPartitioning(customInterval);
        Assert.assertEquals(12, partitionIntervalList.size());
    }

    @Test
    public void cronPatternIntervalPartitionTest3() {
        Optional<LocalDateTime> fromLocalDateTime = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2019-07-01T00:00:00.000"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        );
        Optional<LocalDateTime> toLocalDateTime = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2020-07-02T00:00:00.000"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        );
        CustomInterval customInterval = new CustomInterval(
                fromLocalDateTime.get()
                , toLocalDateTime.get()
        );

        ChargeCycle monthCycle = new GwRentalCalculateCycle(1, 12);
        List<CustomInterval> partitionIntervalList = monthCycle.doPartitioning(customInterval);
        Assert.assertEquals(12, partitionIntervalList.size());
    }

    @Test
    public void cronPatternIntervalPartitionTest4() {
        Optional<LocalDateTime> fromLocalDateTime = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2019-07-01T00:00:00.000"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        );
        Optional<LocalDateTime> toLocalDateTime = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2020-07-02T00:00:00.000"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        );
        CustomInterval customInterval = new CustomInterval(
                fromLocalDateTime.get()
                , toLocalDateTime.get()
        );

        ChargeCycle monthCycle = new GwOverageBillingCycle();
        List<CustomInterval> partitionIntervalList = monthCycle.doPartitioning(customInterval);
        Assert.assertEquals(6, partitionIntervalList.size());
    }

    @Test
    /**
     * 年繳13個月
     */
    public void monthCycleIntervalPartitionTest13() {
        Optional<LocalDateTime> fromLocalDateTime = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2019-07-31T00:00:00.000"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        );
        Optional<LocalDateTime> toLocalDateTime = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2020-07-30T00:00:00.000"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        );
        CustomInterval customInterval = new CustomInterval(
                fromLocalDateTime.get()
                , toLocalDateTime.get()
        );

        ChargeCycle monthCycle = new MonthCycle(1);
        List<CustomInterval> partitionIntervalList = monthCycle.doPartitioning(customInterval);
        Assert.assertEquals(13, partitionIntervalList.size());
    }

    @Test
    public void monthCycleIntervalPartitionTest20() {
        Optional<LocalDateTime> fromLocalDateTime = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2021-05-01T00:00:00.000"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        );
        Optional<LocalDateTime> toLocalDateTime = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2022-12-31T00:00:00.000"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        );
        CustomInterval customInterval = new CustomInterval(
                fromLocalDateTime.get()
                , toLocalDateTime.get()
        );
        ChargeCycle monthCycle = new MonthCycle(1);
        List<CustomInterval> partitionIntervalList = monthCycle.doPartitioning(customInterval);
        Assert.assertEquals(20, partitionIntervalList.size());
    }

    @Test
    public void month12Test() {
        Optional<LocalDateTime> fromLocalDateTime = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2019-07-01T00:00:00.000"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        );
        Optional<LocalDateTime> toLocalDateTime = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2020-06-30T00:00:00.000"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        );

        LocalDateTime resultLocalDateTime = fromLocalDateTime.get().plusMonths(12).minusDays(1);
        Assert.assertEquals(toLocalDateTime.get(), resultLocalDateTime);
    }

    @Test
    public void expirationDateGenTest1() {
        Optional<LocalDateTime> fromLocalDateTime = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2021-03-11T00:00:00.000"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        );
        Optional<LocalDateTime> endLocalDateTime = ContractRenewIntervalGenerator.getContractExpirationDate(
                fromLocalDateTime.get()
                , 12
        );
        System.out.println(endLocalDateTime.toString());
    }

    @Test
    public void seasonPartitionTest1() {
        Optional<LocalDateTime> fromLocalDateTime = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2020-08-09T00:00:00.000"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        );
        Optional<LocalDateTime> toLocalDateTime = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2021-08-08T00:00:00.000"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        );
        CustomInterval targetInterval = new CustomInterval(fromLocalDateTime.get(), toLocalDateTime.get());
        ChargeCycle seasonCycle = new SeasonCycle(1);
        List<CustomInterval> customIntervalList = seasonCycle.doPartitioning(targetInterval);
        Assert.assertEquals(customIntervalList.size(), 4);
    }

    @Test
    public void gwOverageBillingCycleTest() {
        Optional<LocalDateTime> fromLocalDateTime = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2020-08-09T00:00:00.000"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        );
        Optional<LocalDateTime> toLocalDateTime = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2021-08-08T00:00:00.000"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        );
        CustomInterval targetInterval = new CustomInterval(fromLocalDateTime.get(), toLocalDateTime.get());
        ChargeCycle gwOverageBillingCycle = new GwOverageBillingCycle();
        List<CustomInterval> customIntervalList = gwOverageBillingCycle.doPartitioning(targetInterval);
        Assert.assertEquals(6, customIntervalList.size());
    }

    @Test
    public void gwOverageBillingCycle16Test() {
        Optional<LocalDateTime> fromLocalDateTime = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2020-09-16T00:00:00.000"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        );
        Optional<LocalDateTime> toLocalDateTime = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2021-09-15T00:00:00.000"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        );
        CustomInterval targetInterval = new CustomInterval(fromLocalDateTime.get(), toLocalDateTime.get());
        ChargeCycle gwOverageBillingCycle = new GwOverageBillingCycle();
        List<CustomInterval> customIntervalList = gwOverageBillingCycle.doPartitioning(targetInterval);
        Assert.assertEquals(customIntervalList.size(), 6);
    }

    @Test
    public void gwOverageBillingCalCycle16Test() {
        Optional<LocalDateTime> fromLocalDateTime = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2020-09-16T00:00:00.000"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        );
        Optional<LocalDateTime> toLocalDateTime = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2021-09-15T00:00:00.000"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        );
        CustomInterval targetInterval = new CustomInterval(fromLocalDateTime.get(), toLocalDateTime.get());
        ChargeCycle gwOverageBillingCalCycle = new GwOverageBillingCalCycle();
        List<CustomInterval> customIntervalList = gwOverageBillingCalCycle.doPartitioning(targetInterval);
        Assert.assertEquals(customIntervalList.size(), 12);
    }

    @Test
    public void gwOverageBillingCycleCommonTest() {
        Optional<LocalDateTime> fromLocalDateTime = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2021-01-18T00:00:00.000"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        );
        Optional<LocalDateTime> toLocalDateTime = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2022-01-17T00:00:00.000"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        );
        CustomInterval targetInterval = new CustomInterval(fromLocalDateTime.get(), toLocalDateTime.get());
        ChargeCycle gwOverageBillingCycle = new GwOverageBillingCycle();
        List<CustomInterval> resultList = gwOverageBillingCycle.doPartitioning(targetInterval);
        Assert.assertEquals(resultList.size(), 6);
    }

    @Test
    public void gwOverageBillingCalCycleCommonTest() {
        Optional<LocalDateTime> fromLocalDateTime = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2021-01-18T00:00:00.000"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        );
        Optional<LocalDateTime> toLocalDateTime = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2022-01-17T00:00:00.000"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        );
        CustomInterval targetInterval = new CustomInterval(fromLocalDateTime.get(), toLocalDateTime.get());
        ChargeCycle gwOverageBillingCalCycle = new GwOverageBillingCalCycle();
        List<CustomInterval> resultList = gwOverageBillingCalCycle.doPartitioning(targetInterval);
        Assert.assertEquals(resultList.size(), 12);
    }

    @Test
    public void gwOverageBillingCalCycle26748306Test() {
        Optional<LocalDateTime> fromLocalDateTime = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2021-02-11T00:00:00.000"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        );
        Optional<LocalDateTime> toLocalDateTime = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2022-02-10T00:00:00.000"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        );
        CustomInterval targetInterval = new CustomInterval(fromLocalDateTime.get(), toLocalDateTime.get());
        ChargeCycle gwOverageBillingCalCycle = new GwOverageBillingCalCycle();
        List<CustomInterval> resultList = gwOverageBillingCalCycle.doPartitioning(targetInterval);
        Assert.assertEquals(resultList.size(), 12);
    }

    @Test
    public void gwOverageBillingCycle26748306Test() {
        Optional<LocalDateTime> fromLocalDateTime = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2021-02-11T00:00:00.000"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        );
        Optional<LocalDateTime> toLocalDateTime = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2022-02-10T00:00:00.000"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        );
        CustomInterval targetInterval = new CustomInterval(fromLocalDateTime.get(), toLocalDateTime.get());
        ChargeCycle gwOverageBillingCycle = new GwOverageBillingCycle();
        List<CustomInterval> resultList = gwOverageBillingCycle.doPartitioning(targetInterval);
        Assert.assertEquals(resultList.size(), 6);
    }

    @Test
    public void gwOverageBillingCalCycleTest2() {
        Optional<LocalDateTime> fromLocalDateTime = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2021-04-10T00:00:00.000"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        );
        Optional<LocalDateTime> toLocalDateTime = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2022-04-09T00:00:00.000"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        );
        CustomInterval targetInterval = new CustomInterval(fromLocalDateTime.get(), toLocalDateTime.get());
        ChargeCycle gwOverageBillingCalCycle = new GwOverageBillingCalCycle();
        List<CustomInterval> resultList = gwOverageBillingCalCycle.doPartitioning(targetInterval);
        Assert.assertEquals(resultList.size(), 12);
    }

}
