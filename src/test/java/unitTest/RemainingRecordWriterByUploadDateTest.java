package unitTest;

import com.gateweb.utils.LocalDateTimeUtils;
import com.gateweb.charge.contract.remainingCount.component.RemainingRecordWriterByUploadDate;
import com.gateweb.charge.config.SpringWebMvcConfig;
import org.eclipse.core.runtime.Assert;
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
public class RemainingRecordWriterByUploadDateTest {
    @Autowired
    RemainingRecordWriterByUploadDate remainingCountRecordWriter;

    @Test
    public void needToBeRecordedTest1() {
        Optional<LocalDateTime> currentTimestamp = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2020-07-01T00:00:00.000"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        );

        //上次的結束日期
        Optional<LocalDateTime> searchStartDate = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2019-07-01T00:00:00.000"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        );

        Optional<LocalDateTime> contractExpirationDate = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2019-08-31T00:00:00.000"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        );


        Assert.isTrue(remainingCountRecordWriter.needToBeRecorded(
                currentTimestamp.get()
                , searchStartDate.get()
                , contractExpirationDate.get()
                , -1
                , 48
                ));
    }

    @Test
    public void needToBeRecordedTest2() {
        Optional<LocalDateTime> currentTimestamp = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2020-07-01T00:00:00.000"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        );

        //上次的結束日期
        Optional<LocalDateTime> searchStartDate = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2019-07-01T00:00:00.000"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        );

        Optional<LocalDateTime> contractExpirationDate = LocalDateTimeUtils.parseLocalDateTimeFromString(
                "2019-08-31T00:00:00.000"
                , "yyyy-MM-dd'T'HH:mm:ss.SSS"
        );

        Assert.isTrue(remainingCountRecordWriter.needToBeRecorded(
                currentTimestamp.get()
                , searchStartDate.get()
                , contractExpirationDate.get()
                , 0
                , 0
        ));
    }
}
