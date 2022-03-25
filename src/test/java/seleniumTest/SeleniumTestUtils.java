package seleniumTest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class SeleniumTestUtils {
    public void clickBetaMenu(WebDriver driver) {
        WebElement betaMenu = driver.findElement(By.id("beta_menu"));
        betaMenu.click();
    }

    public void goToContractPage(WebDriver driver) throws InterruptedException {
        clickBetaMenu(driver);
        TimeUnit.SECONDS.sleep(1);
        WebElement contractMenu = driver.findElement(By.id("contract_menu"));
        contractMenu.click();
    }

}
