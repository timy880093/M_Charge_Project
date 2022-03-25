package seleniumTest;

import com.gateweb.charge.config.SpringWebMvcConfig;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@RunWith(SpringJUnit4ClassRunner.class)
@ComponentScan(basePackages = {
        "com.gateweb.charge.config"
        , "seleniumTest"
})
@ContextConfiguration(classes = {SpringWebMvcConfig.class})
@WebAppConfiguration
public class IntegrationTest {
    private SeleniumTestUtils seleniumTestUtils = new SeleniumTestUtils();
    private ContractPageTestUtils contractPageTestUtils = new ContractPageTestUtils();
    private static ChromeDriver driver;

    @BeforeClass
    public static void openBrowser() {
        System.setProperty("webdriver.chrome.driver", "chromedriver_win32/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @Test
    public void testUatWebsiteLogin() {
        login();
        String actualUrl = driver.getCurrentUrl();
        String expectedUrl = "http://192.168.100.222:9070/backendAdmin/indexServlet";
        Assert.assertEquals(actualUrl, expectedUrl);
        closeBrowser();
    }

    @Test
    public void testContractCreate() {
        login();
        createContract();
    }

    @Test
    public void testContractDelete() {
        login();
        deleteContract();
    }

    public void login() {
        driver.get("http://localhost:8080");
        WebElement username = driver.findElement(By.id("username"));
        WebElement password = driver.findElement(By.id("password"));
        WebElement login = driver.findElement(By.xpath("//input[@type='submit']"));
        username.sendKeys("se01");
        password.sendKeys("$jf590302");
        Dimension d = new Dimension(1080, 1024);
        driver.manage().window().setSize(d);
        login.click();
    }

    public void createContract() {
        try {
            seleniumTestUtils.goToContractPage(driver);
            contractPageTestUtils.selectOperationMenu(driver);
            contractPageTestUtils.selectAddContract(driver);
            contractPageTestUtils.selectCompanyForEdit(driver);
            //選company
            WebElement gwCompany = driver.findElementByXPath("/html/body/div[1]/div[3]/div[2]/div[1]/div[15]/div/div/div/div[2]/div/table/tbody/tr[1]/td[text()='24549210']");
            gwCompany.click();
            WebElement companyConfirmBtn = driver.findElement(By.id("companyMenuModalChoose"));
            TimeUnit.SECONDS.sleep(1);
            companyConfirmBtn.click();
            WebElement contractEditName = driver.findElement(By.id("contractEditModalName"));
            contractEditName.sendKeys(String.valueOf(new Date().getTime()));
            WebElement contractEditEffectiveDate = driver.findElementByXPath("//*[@id=\"contractEditModalEffectiveDate\"]");
            TimeUnit.SECONDS.sleep(1);
            contractEditEffectiveDate.sendKeys("2020-06-24");
            WebElement contractEditExpirationDate = driver.findElementByXPath("//*[@id=\"contractEditModalExpirationDate\"]");
            TimeUnit.SECONDS.sleep(1);
            contractEditExpirationDate.sendKeys("2021-06-24");
            contractPageTestUtils.createPrepayChargeItem(driver);
            contractPageTestUtils.createOverageChargeItem(driver);
            WebElement saveNewContract = driver.findElementByXPath("/html/body/div[1]/div[3]/div[2]/div[1]/div[2]/div/div/div[3]/button[2]");
            saveNewContract.click();
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void deleteContract() {
        try {
            seleniumTestUtils.goToContractPage(driver);
            contractPageTestUtils.selectSearchFilter(driver);
            contractPageTestUtils.selectSearchFilterCompany(driver);
            contractPageTestUtils.confirmSearchFilter(driver);
            contractPageTestUtils.selectFirstContractRow(driver);
            contractPageTestUtils.selectOperationMenu(driver);
            contractPageTestUtils.selectOperationDelete(driver);
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void closeBrowser() {
        driver.close();
    }

}
