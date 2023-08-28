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
        driver.get(BASE_URL);

        //Login
        authenticateWithUser(USERNAME, PASSWORD);

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
        fillShippingDetails(FIRST_NAME, LAST_NAME, ZIPCODE);

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
        fillShippingDetails(FIRST_NAME, LAST_NAME, ZIPCODE);
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

}