package unitTest;

import com.gateweb.base.exception.NullPropertyException;
import com.gateweb.charge.notice.bean.OBank;
import com.gateweb.charge.notice.bean.PaymentRequestMailFreemarkerData;
import com.gateweb.utils.ObjectUtil;
import com.mysema.commons.lang.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;
import java.util.ArrayList;

public class ObjectNullCheckerTest {
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    public PaymentRequestMailFreemarkerData prepareNonNullData() {
        PaymentRequestMailFreemarkerData paymentRequestMailFreemarkerData = new PaymentRequestMailFreemarkerData();
        paymentRequestMailFreemarkerData.setExtraNoticeMessage("");
        paymentRequestMailFreemarkerData.setExtraNotice(false);
        paymentRequestMailFreemarkerData.setCompanyName("");
        paymentRequestMailFreemarkerData.setCorrection(false);
        paymentRequestMailFreemarkerData.setCtbcVirtualAccount("");
        paymentRequestMailFreemarkerData.setHaveOverage(false);
        paymentRequestMailFreemarkerData.setPaymentExpirationDate("");
        paymentRequestMailFreemarkerData.setPaymentRequestMailOverageItemList(new ArrayList<>());
        paymentRequestMailFreemarkerData.setPaymentRequestOverageTotalAmount(BigDecimal.ZERO);
        paymentRequestMailFreemarkerData.setPaymentRequestTotalAmount(BigDecimal.ZERO);
        paymentRequestMailFreemarkerData.setYmString("");
        paymentRequestMailFreemarkerData.setoBank(new OBank(false, ""));
        return paymentRequestMailFreemarkerData;
    }

    @Test(expected = NullPropertyException.class)
    public void failedCase() throws NullPropertyException {
        ObjectUtil.objectPropertyNullCheck(new PaymentRequestMailFreemarkerData());
    }

    @Test
    public void successfulCase() throws NullPropertyException {
        ObjectUtil.objectPropertyNullCheck(prepareNonNullData());
        Assert.isTrue(true, "true");
    }
}
