package io.github.cherniakhb.sendmoney.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.github.cherniakhb.sendmoney.constant.WebConstant.DEFAULT_WAIT_TIMEOUT;

/**
 * Represent a step 2 page of Privatbank Sendmoney.
 * Contains Acknowledgement information
 */
public class TransferConfirmationPage {

    private static final String XPATH_PHONE_NUMBER_FIELD = "//*[@id=\"step2Phone\"]";
    private static final String XPATH_SEND_BUTTON = "/html/body/div[2]/div[3]/div/div[15]/div";

    private Logger log = LoggerFactory.getLogger(TransferConfirmationPage.class);

    private WebDriver webDriver;
    private WebDriverWait generalWebDriverWait;

    public TransferConfirmationPage(WebDriver webDriver) {
        this.webDriver = webDriver;
        generalWebDriverWait = new WebDriverWait(webDriver, DEFAULT_WAIT_TIMEOUT);

        //Wait until the page is fully loaded
        log.debug("Loading step 2 page");
        generalWebDriverWait.until(currentWebDriver -> currentWebDriver.getCurrentUrl().endsWith("step2"));
    }

    /**
     * When sender card is a PB card, phone number field may be absent.
     * Checks whether phone number field is present on a page.
     */
    public boolean isPhoneNumberFieldPresent() {
        return !webDriver.findElements(By.xpath(XPATH_PHONE_NUMBER_FIELD)).isEmpty();
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
        WebElement sendMoneyButton = webDriver.findElement(By.xpath(XPATH_SEND_BUTTON));
        sendMoneyButton.click();
    }
}
