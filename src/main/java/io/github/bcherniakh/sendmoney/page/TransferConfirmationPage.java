package io.github.bcherniakh.sendmoney.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Wait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.github.bcherniakh.sendmoney.page.xpath.SiteElements.ConfirmationPage.PHONE_NUMBER_FIELD;
import static io.github.bcherniakh.sendmoney.page.xpath.SiteElements.ConfirmationPage.SEND_BUTTON;

/**
 * Represent a step 2 page of Privatbank Sendmoney.
 * Contains Acknowledgement information
 */
public class TransferConfirmationPage {

    private Logger log = LoggerFactory.getLogger(TransferConfirmationPage.class);

    private WebDriver webDriver;
    private Wait<WebDriver> wait;

    public TransferConfirmationPage(WebDriver webDriver, Wait<WebDriver> wait) {
        this.webDriver = webDriver;
        this.wait = wait;
//        wait = new WebDriverWait(webDriver, DEFAULT_WAIT_TIMEOUT);

        //Wait until the page is fully loaded
        log.debug("Loading step 2 page");
        this.wait.until(currentWebDriver -> currentWebDriver.getCurrentUrl().endsWith("step2"));
    }

    /**
     * When sender card is a PB card, phone number field may be absent.
     * Checks whether phone number field is present on a page.
     */
    public boolean isPhoneNumberFieldPresent() {
        return !webDriver.findElements(By.xpath(PHONE_NUMBER_FIELD.xPath())).isEmpty();
    }


    /**
     * Fills phone number input field if it is present on a page.
     *
     * @param phoneNumber phone number in the next format
     *                    380XXXXXXXXX. No other formats allowed.
     */
    public void fillPhoneNumber(String phoneNumber) {
        if (!isPhoneNumberFieldPresent()) {
            log.warn("Fill phone number invoked but field is empty");
            return;
        }

        WebElement phoneNumberElement = webDriver.findElement(By.xpath("//*[@id=\"step2Phone\"]"));
        phoneNumberElement.sendKeys(phoneNumber);
    }

    public void clickSendButton() {
        log.debug("Invoking send button on step 2");
        WebElement sendMoneyButton = webDriver.findElement(By.xpath(SEND_BUTTON.xPath()));
        sendMoneyButton.click();
    }
}
