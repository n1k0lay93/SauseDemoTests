package core;

import org.example.Constants.BrowserTypes;
import org.example.Constants.Products;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.example.Constants.Constants.*;
import static org.example.Constants.Constants.ERROR_MSG_PRICE;
import static org.example.Constants.Products.BACKPACK;
import static org.example.Constants.Products.T_SHIRT;

public class BaseTest {

    public static WebDriver driver;
    public static WebDriverWait wait;

    @AfterAll
    public static void afterTest(){
        // close driver
        driver.close();
    }

    @AfterEach
    public void resetApp() {

        // Navigate to Google.com
        driver.get("https://www.saucedemo.com/inventory.html");
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        //Click Burger Button
        driver.findElement(By.id("react-burger-menu-btn")).click();

        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("reset_sidebar_link"))));

        //Click Reset
        driver.findElement(By.id("reset_sidebar_link")).click();

    }

    protected static WebDriver startBrowser(BrowserTypes browserType) {
        // Setup Browser
        switch (browserType){
            case CHROME:
                ChromeOptions chromeOptions = new ChromeOptions();
                return new ChromeDriver(chromeOptions);
            case FIREFOX:
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                return new FirefoxDriver(firefoxOptions);
            case EDGE:
                EdgeOptions edgeOptions = new EdgeOptions();
                return new EdgeDriver(edgeOptions);
        }

        return null;
    }

    protected static void authenticateWithUser(String username, String pass) {
        WebElement usernameInput = driver.findElement(By.xpath("//input[@data-test='username']"));
        usernameInput.sendKeys(username);

        WebElement password = driver.findElement(By.xpath("//input[@data-test='password']"));
        password.sendKeys(pass);

        WebElement loginButton = driver.findElement(By.xpath("//input[@data-test='login-button']"));
        loginButton.click();

        WebElement inventoryPageTitle = driver.findElement(By.xpath("//div[@class='app_logo']"));
        wait.until(ExpectedConditions.visibilityOf(inventoryPageTitle));
    }

    protected WebElement getProductByTitle(String title){
        return driver.findElement(By.xpath(String.format("//div[@class='inventory_item' and descendant::div[text()='%s']]", title)));
    }



    protected static void fillShippingDetails(String firstName, String lastName, String zip) {
        driver.findElement(By.id("first-name")).sendKeys(firstName);
        driver.findElement(By.id("last-name")).sendKeys(lastName);
        driver.findElement(By.id("postal-code")).sendKeys(zip);
    }

    protected void addToCart(Products product) {

        WebElement element = getProductByTitle(product.label);
        element.findElement(By.className("btn_inventory")).click();

    }
    protected static void assertProductPrice(List<WebElement> prices) {
        Assertions.assertEquals(T_SHIRT.price, prices.get(1).getText(), ERROR_MSG_PRICE);
        Assertions.assertEquals(BACKPACK.price, prices.get(0).getText(), ERROR_MSG_PRICE);
    }

    protected static void assertProductLabel(List<WebElement> items) {
        Assertions.assertEquals(BACKPACK.label, items.get(0).getText(), ERROR_MSG_PRODUCT_NOT_FOUND);
        Assertions.assertEquals(T_SHIRT.label, items.get(1).getText(), ERROR_MSG_PRODUCT_NOT_FOUND);
    }

    protected static void assertArraySize(List<WebElement> items, int size) {
        Assertions.assertEquals(size, items.size(), ERROR_MSG_SIZE);
    }

    protected static String actualTotalPrice() {
        var prices = driver.findElements(By.className("inventory_item_price"));
        double sum = 0;

        for (int i = 0; i < prices.size(); i++) {
            sum += Double.parseDouble(driver.findElements(By.className("inventory_item_price")).get(i).getText().replaceAll("[$]", " "));
        }

        double endPrice = sum + sum * 0.08;
        String endPriceString = String.format("Total: $%.2f", endPrice);
        return endPriceString;
    }

    protected static void assertTotalPrice(String expected, String actual) {
        Assertions.assertEquals(expected, actual, ERROR_MSG_PRICE);
    }

}