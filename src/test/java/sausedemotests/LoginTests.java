package sausedemotests;

import core.BaseTest;
import org.example.Constants.BrowserTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginTests extends BaseTest {

    @BeforeAll
    public static void beforeAllTests(){
        driver = startBrowser(BrowserTypes.CHROME);

        // Configure wait
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        // Navigate to Google.com
        driver.get("https://www.saucedemo.com/");
    }

    @Test
    public void userAuthenticated_when_validCredentialsProvided(){
        authenticateWithUser("standard_user", "secret_sauce");

        // Add Assert
        String appLogoText = "Swag Labs";
        var appLogo = driver.findElement(By.className("app_logo"));
        String titleText = "Products";
        var title = driver.findElement(By.className("title"));
        Assertions.assertTrue(appLogo.getText().contains(appLogoText), (String.format("Logo %s not found", appLogoText)));
        Assertions.assertTrue(title.getText().contains(titleText),(String.format("Title %s not found", titleText)));
    }


}
