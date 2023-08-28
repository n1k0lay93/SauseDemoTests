package sausedemotests;

import core.BaseTest;
import org.example.Constants.BrowserTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.example.Constants.Constants.*;
import static org.example.Constants.Products.BACKPACK;
import static org.example.Constants.Products.T_SHIRT;

public class ProductsTests extends BaseTest {

    @BeforeAll
    public static void beforeAllTests() {
        driver = startBrowser(BrowserTypes.CHROME);

        // Configure wait
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));

    }

    @BeforeEach
    public void setupTests() {
        //Navigate to Home page
        driver.get("https://www.saucedemo.com/");

        //Login
        authenticateWithUser("standard_user", "secret_sauce");

        //Add products to Cart
        addToCart(BACKPACK);
        addToCart(T_SHIRT);

        //Navigate to Cart
        driver.findElement(By.className("shopping_cart_link")).click();

    }

    @Test
    public void productAddedToShoppingCart_when_addToCart() {


        // Assert Items and Totals
        var items = driver.findElements(By.className("inventory_item_name"));
        var prices = driver.findElements(By.className("inventory_item_price"));


        assertArraySize(items,2);
        assertProductLabel(items);
        assertProductPrice(prices);
    }

    @Test
    public void userDetailsAdded_when_checkoutWithValidInformation() {

        String actualPriceString = actualTotalPrice();

        // Assert Items and Totals
        driver.findElement(By.id("checkout")).click();


        // fill form
        fillShippingDetails("First Name", "Last Name", "Zip Code");

        driver.findElement(By.id("continue")).click();

        var items = driver.findElements(By.className("inventory_item_name"));
        var expectedTotalPrice = driver.findElement(By.className("summary_total_label")).getText();

        assertArraySize(items,2);
        assertProductLabel(items);
        assertTotalPrice(actualPriceString, expectedTotalPrice);
    }


    @Test
    public void orderCompleted_when_addProduct_and_checkout_withConfirm() {

        var items = driver.findElements(By.className("inventory_item_name"));
        var prices = driver.findElements(By.className("inventory_item_price"));
        
        //Assert Products and Prices
        assertProductLabel(items);
        assertProductPrice(prices);
        assertArraySize(items,2);

        //Click on checkout
        driver.findElement(By.id("checkout")).click();

        // Fill Contact Details
        fillShippingDetails("First Name", "Last Name", "Zip Code");
        driver.findElement(By.id("continue")).click();

        //Assert Prices and Totals
        String actualTotalPrice = actualTotalPrice();
        var expectedTotalPrice = driver.findElement(By.className("summary_total_label")).getText();
        assertTotalPrice(actualTotalPrice,expectedTotalPrice);

        // Complete Order
        driver.findElement(By.xpath("//button[@id='finish']")).click();

        WebElement completedOrderMessage = driver.findElement(By.xpath("//h2"));
        String completedOrderMessageText = "Thank you for your order!";

        WebElement completedOrderTitle = driver.findElement(By.className("title"));
        String completedOrderTitleText = "Checkout: Complete!";
        var products = driver.findElements(By.className("inventory_item_name"));

        //Assert order is completed
        Assertions.assertTrue(completedOrderMessage.getText().contains(completedOrderMessageText), "Order completed message not found");
        Assertions.assertEquals(completedOrderTitleText, completedOrderTitle.getText(), "Order completed title not found");
        assertArraySize(products,0);
    }

    private static void assertProductPrice(List<WebElement> prices) {
        Assertions.assertEquals(T_SHIRT.price, prices.get(1).getText(), ERROR_MSG_PRICE);
        Assertions.assertEquals(BACKPACK.price, prices.get(0).getText(), ERROR_MSG_PRICE);
    }

    private static void assertProductLabel(List<WebElement> items) {
        Assertions.assertEquals(BACKPACK.label, items.get(0).getText(), ERROR_MSG_PRODUCT_NOT_FOUND);
        Assertions.assertEquals(T_SHIRT.label, items.get(1).getText(), ERROR_MSG_PRODUCT_NOT_FOUND);
    }

    private static void assertArraySize(List<WebElement> items, int size) {
        Assertions.assertEquals(size, items.size(), ERROR_MSG_SIZE);
    }

    private static String actualTotalPrice() {
        var prices = driver.findElements(By.className("inventory_item_price"));
        double sum = 0;

        for (int i = 0; i < prices.size(); i++) {
            sum += Double.parseDouble(driver.findElements(By.className("inventory_item_price")).get(i).getText().replaceAll("[$]", " "));
        }

        double endPrice = sum + sum * 0.08;
        String endPriceString = String.format("Total: $%.2f", endPrice);
        return endPriceString;
    }

    private static void assertTotalPrice(String expected, String actual) {
        Assertions.assertEquals(expected, actual, ERROR_MSG_PRICE);
    }



}