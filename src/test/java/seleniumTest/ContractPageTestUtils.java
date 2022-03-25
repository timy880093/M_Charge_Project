package seleniumTest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class ContractPageTestUtils {

    public void selectOperationMenu(WebDriver driver) throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
        WebElement operationButton = driver.findElement(By.id("operation"));
        operationButton.click();
    }

    public void selectAddContract(WebDriver driver) throws InterruptedException {
        TimeUnit.SECONDS.sleep(2);
        WebElement contractOperationAdd = driver.findElement(By.id("contractOperationAdd"));
        contractOperationAdd.click();
    }

    public void selectCompanyForEdit(WebDriver driver) throws InterruptedException {
        TimeUnit.SECONDS.sleep(1);
        WebElement contractEditCompanySelectBtn = driver.findElement(By.id("contractEditCompanySelectBtn"));
        contractEditCompanySelectBtn.click();
    }

    public void confirmSearchFilter(ChromeDriver driver) throws InterruptedException {
        TimeUnit.SECONDS.sleep(1);
        WebElement contractSearchFilterConfirm = driver.findElementByXPath("//*[@id=\"applyContractSearchCondition\"]");
        contractSearchFilterConfirm.click();
    }

    public void selectFirstContractRow(ChromeDriver driver) {
        WebElement firstRowOfContract = driver.findElementByXPath("/html/body/div[1]/div[3]/div[2]/div[1]/div[1]/div[2]/table/tbody/tr/td[1]");
        firstRowOfContract.click();
    }

    public void selectSearchFilter(ChromeDriver driver) throws InterruptedException {
        TimeUnit.SECONDS.sleep(1);
        WebElement contractSearchFilterBtn = driver.findElementByXPath("/html/body/div[1]/div[3]/div[2]/div[1]/table/tbody/tr/td[2]/span/a[1]/span");
        contractSearchFilterBtn.click();
    }

    public void selectOperationDelete(ChromeDriver driver) throws InterruptedException {
        TimeUnit.SECONDS.sleep(1);
        WebElement contractOperationDelete = driver.findElementByXPath("/html/body/div[1]/div[3]/div[2]/div[1]/div[8]/div/div/div[2]/form/div[6]/div/button");
        contractOperationDelete.click();
    }

    public void selectSearchFilterCompany(ChromeDriver driver) throws InterruptedException {
        WebElement contractSearchFilterCompanySelector = driver.findElementByXPath("/html/body/div[1]/div[3]/div[2]/div[1]/div[6]/div/div/div[2]/form/div[1]/div/div/button");
        TimeUnit.SECONDS.sleep(1);
        contractSearchFilterCompanySelector.click();
        WebElement companySearchField = driver.findElementByXPath("/html/body/div[1]/div[3]/div[2]/div[1]/div[15]/div/div/div/div[2]/div/div[2]/label/input");
        companySearchField.sendKeys("24549210");
        WebElement companyChooseFirstRow = driver.findElementByXPath("/html/body/div[1]/div[3]/div[2]/div[1]/div[15]/div/div/div/div[2]/div/table/tbody/tr[1]/td[2]");
        TimeUnit.SECONDS.sleep(1);
        companyChooseFirstRow.click();
        WebElement companyConfirm = driver.findElementByXPath("//*[@id=\"companyMenuModalChoose\"]");
        TimeUnit.SECONDS.sleep(1);
        companyConfirm.click();
    }

    //新增一個預繳的計費項目
    public void createPrepayChargeItem(ChromeDriver driver) throws InterruptedException {
        WebElement packageRefInsert = driver.findElement(By.id("packageRefChargeModeInsert"));
        packageRefInsert.click();
        TimeUnit.SECONDS.sleep(1);
        WebElement paidPlanButton = driver.findElement(By.id("paidPlanButton"));
        paidPlanButton.click();
        TimeUnit.SECONDS.sleep(1);
        WebElement paidPlanSelect = driver.findElementByXPath("//td[text()='預繳']");
        paidPlanSelect.click();
        TimeUnit.SECONDS.sleep(1);
        WebElement paidPlanChoose = driver.findElement(By.id("paidPlanMenuModalChoose"));
        paidPlanChoose.click();
        TimeUnit.SECONDS.sleep(1);
        WebElement chargePlanSelect = driver.findElement(By.id("chargePlanSelect"));
        chargePlanSelect.click();
        TimeUnit.SECONDS.sleep(1);
        WebElement chargePlanOption = driver.findElementByXPath("/html/body/div[1]/div[3]/div[2]/div[1]/div[17]/div/div/div/div[2]/div/table/tbody/tr[2]/td[text()='週期']");
        chargePlanOption.click();
        TimeUnit.SECONDS.sleep(1);
        WebElement chargePlanChoose = driver.findElement(By.id("chargePlanMenuModalChoose"));
        chargePlanChoose.click();
        TimeUnit.SECONDS.sleep(1);
        WebElement chargeModeSelect = driver.findElement(By.id("chargeModeSelect"));
        chargeModeSelect.click();
        TimeUnit.SECONDS.sleep(1);
        WebElement chargeModeMenuSelect = driver.findElementByXPath("//td[text()='馬上開年繳_rental']");
        chargeModeMenuSelect.click();
        TimeUnit.SECONDS.sleep(1);
        WebElement chargeModeMenuChoose = driver.findElement(By.id("chargeModeMenuModalChoose"));
        chargeModeMenuChoose.click();
        TimeUnit.SECONDS.sleep(1);
        WebElement packageRefSaveChange = driver.findElement(By.id("packageRefEditChargeModeSaveChange"));
        packageRefSaveChange.click();
        TimeUnit.SECONDS.sleep(1);
    }

    //新增一個超額的計費項目
    public void createOverageChargeItem(ChromeDriver driver) throws InterruptedException {
        WebElement packageRefInsert = driver.findElement(By.id("packageRefChargeModeInsert"));
        packageRefInsert.click();
        TimeUnit.SECONDS.sleep(1);
        WebElement paidPlanButton = driver.findElement(By.id("paidPlanButton"));
        paidPlanButton.click();
        TimeUnit.SECONDS.sleep(1);
        WebElement paidPlanSelect = driver.findElementByXPath("/html/body/div[1]/div[3]/div[2]/div[1]/div[16]/div/div/div/div[2]/div/table/tbody/tr[1]/td[text()='後繳']");
        paidPlanSelect.click();
        TimeUnit.SECONDS.sleep(1);
        WebElement paidPlanChoose = driver.findElement(By.id("paidPlanMenuModalChoose"));
        paidPlanChoose.click();
        TimeUnit.SECONDS.sleep(1);
        WebElement chargePlanSelect = driver.findElement(By.id("chargePlanSelect"));
        chargePlanSelect.click();
        TimeUnit.SECONDS.sleep(1);
        WebElement chargePlanOption = driver.findElementByXPath("/html/body/div[1]/div[3]/div[2]/div[1]/div[17]/div/div/div/div[2]/div/table/tbody/tr[2]/td[text()='週期']");
        chargePlanOption.click();
        TimeUnit.SECONDS.sleep(1);
        WebElement chargePlanChoose = driver.findElement(By.id("chargePlanMenuModalChoose"));
        chargePlanChoose.click();
        TimeUnit.SECONDS.sleep(1);
        WebElement chargeModeSelect = driver.findElement(By.id("chargeModeSelect"));
        chargeModeSelect.click();
        TimeUnit.SECONDS.sleep(1);
        //查找超額的項目
        WebElement overageItemSearch = driver.findElementByXPath("/html/body/div[1]/div[3]/div[2]/div[1]/div[18]/div/div/div/div[2]/div/div[2]/label/input");
        overageItemSearch.sendKeys("overage");
        WebElement chargeModeMenuSelect = driver.findElementByXPath("/html/body/div[1]/div[3]/div[2]/div[1]/div[18]/div/div/div/div[2]/div/table/tbody/tr[1]/td[2]");
        chargeModeMenuSelect.click();
        TimeUnit.SECONDS.sleep(1);
        WebElement chargeModeMenuChoose = driver.findElement(By.id("chargeModeMenuModalChoose"));
        chargeModeMenuChoose.click();
        TimeUnit.SECONDS.sleep(1);
        WebElement packageRefSaveChange = driver.findElement(By.id("packageRefEditChargeModeSaveChange"));
        packageRefSaveChange.click();
        TimeUnit.SECONDS.sleep(1);
    }


}
