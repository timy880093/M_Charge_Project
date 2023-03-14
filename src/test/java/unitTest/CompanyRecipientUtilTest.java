package unitTest;

import com.gateweb.charge.notice.utils.CompanyRecipientUtils;
import com.gateweb.orm.charge.entity.Company;
import org.junit.Assert;
import org.junit.Test;

public class CompanyRecipientUtilTest {
    public Company provideEmail1Company() {
        Company company = new Company();
        company.setEmail1("se01@gateweb.com.tw");
        return company;
    }

    public Company provideBothCompany() {
        Company company = new Company();
        company.setEmail1("se01@gateweb.com.tw");
        company.setEmail2("service01@gateweb.com.tw");
        return company;
    }

    public Company provideDuplicateEmailCompany() {
        Company company = new Company();
        company.setEmail1("se01@gateweb.com.tw");
        company.setEmail2("se01@gateweb.com.tw");
        return company;
    }

    @Test
    public void onlyEmail1SuccessTest() {
        Company company = provideEmail1Company();
        String recipient = CompanyRecipientUtils.noticeCompanyRecipientEncode(company);
        Assert.assertEquals("se01@gateweb.com.tw", recipient);
    }

    @Test
    public void bothEmail1AndEmail2SuccessTest() {
        Company company = provideBothCompany();
        String recipient = CompanyRecipientUtils.noticeCompanyRecipientEncode(company);
        Assert.assertEquals("se01@gateweb.com.tw,service01@gateweb.com.tw", recipient);
    }

    @Test
    public void duplicateEmailTest() {
        Company company = provideDuplicateEmailCompany();
        String recipient = CompanyRecipientUtils.noticeCompanyRecipientEncode(company);
        Assert.assertEquals("se01@gateweb.com.tw", recipient);
    }

    @Test
    public void emptyEmailTest() {
        Company company = new Company();
        String recipient = CompanyRecipientUtils.noticeCompanyRecipientEncode(company);
        Assert.assertEquals("", recipient);
    }
}
