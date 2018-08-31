package com.page;

import io.appium.java_client.*;
import io.appium.java_client.touch.LongPressOptions;
import io.appium.java_client.touch.TapOptions;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.jetbrains.uast.values.UBooleanConstant;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import java.util.List;
import org.openqa.selenium.*;
import io.appium.java_client.AppiumDriver;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;


class BasePage {

    public AppiumDriver driver;
    public WebDriverWait wait;
    public io.appium.java_client.TouchAction touchAction;

    //Constructor
    public BasePage(AppiumDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver,70);
    }

    void click(By element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
        driver.findElement(element).click();
    }

    void clickSubElem(By element01, By element02) {
        wait.until(ExpectedConditions.elementToBeClickable(element01));
        WebElement elem = driver.findElement(element01);
        elem.findElement(element02).click();
    }

    void scroll(WebElement element) {
        TouchAction action = new TouchAction( driver );
        action.moveTo( (PointOption) element );
        action.perform();
    }

    void clickFirst(By element) {
        try {

        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(element));
        List<WebElement> li = driver.findElements(element);

        li.get(0).click();

        } catch (Exception e) {
            System.out.println( "Tentando uma Segunda Vez..." );
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(element));
            List<WebElement> li = driver.findElements(element);
            li.get(0).click();
        }
    }

    Boolean clickFirstBoolean(By element) {

        boolean result = true;

        try {

            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(element));
            List<WebElement> li = driver.findElements(element);
            li.get(0).click();

        } catch (Exception e) {
            System.out.println( e );
            result = false;

        }
        return result;
    }

    void sendKeys(By element, String text) {
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(element));
        WebElement elem = driver.findElement(element);
        elem.sendKeys(text);
    }

    void sendKeysElement(WebElement element, String text) {
        element.sendKeys(text);
    }

    public WebElement textClick(By locator, String text) {

        System.out.println( locator );
        wait.until( ExpectedConditions.presenceOfAllElementsLocatedBy( locator ) );
        List<MobileElement> AllSearchResults = (List<MobileElement>) driver.findElements( locator );
        WebElement element = null;
        for (WebElement eachResult : AllSearchResults) {
            element = eachResult;
            try {
                System.out.println( eachResult.getText() );
                if (eachResult.getText().equalsIgnoreCase( text )) {
                    eachResult.click();
                    Thread.sleep( 2000 );
                    break;
                }

            } catch (Exception e) {
                System.out.println( e.getMessage() );
            }
        }

        return element;
    }

    public void enter(By locator) {

        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
        WebElement elem = driver.findElement(locator);
        elem.sendKeys(Keys.ENTER);
    }
    public void tab(By locator) {

        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
        WebElement elem = driver.findElement(locator);
        elem.sendKeys(Keys.TAB);
    }

    String getText(By element) {
        String text = "";
        wait.until(ExpectedConditions.presenceOfElementLocated(element));
        text = driver.findElement(element).getText();
        System.out.println(text);
        return text;
    }

    Boolean getTextBollean(By element) throws InterruptedException {
        boolean result = true;
        Thread.sleep( 3000 );
        try{
            String text = driver.findElement(element).getText();
            System.out.println(text);
        } catch (Exception e) {
            System.out.println( e.getMessage() );
            result = false;
        }
        return result;
    }

    boolean isEnableElement(By element) {
        wait.until(ExpectedConditions.presenceOfElementLocated(element));
        Assert.assertTrue(driver.findElement(element).isEnabled());
        return true;
    }

    void assertText(By element, String text) throws InterruptedException {
        wait.until(ExpectedConditions.visibilityOfElementLocated(element));
        Thread.sleep( 2000 ); // Espera o texto do elemento.
        String elem_text = driver.findElement( element ).getText();
        String allRemoved = elem_text.replaceAll("^\\s+|\\s+$", "").trim();
        System.out.println(allRemoved.toLowerCase() + "=" + text.toLowerCase());
        assertThat(allRemoved.toLowerCase(), containsString(text.toLowerCase()));
    }
    void assertTextElement(WebElement element, String text) throws InterruptedException {
        Thread.sleep( 2000 ); // Espera o texto do elemento.
        String elem_text = element.getText();
        String allRemoved = elem_text.replaceAll("^\\s+|\\s+$", "").trim();
        System.out.println(allRemoved.toLowerCase() + "=" + text.toLowerCase());
        assertThat(allRemoved.toLowerCase(), containsString(text.toLowerCase()));
    }
}
