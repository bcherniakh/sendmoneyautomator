package io.github.cherniabkhb.sendmoney.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static io.github.cherniabkhb.sendmoney.constant.WebConstant.DEFAULT_WAIT_TIMEOUT;
import static org.openqa.selenium.support.ui.ExpectedConditions.attributeToBe;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

/**
 * Represents a main web page of Privatbank SendMoney system.
 * Can be used for automating the debit-cards money transfer.
 * Creating an instance of this class means opening the page.
 */
public class SendMoneyPbPage {


    private static final String SENDMONEY_URL = "https://sendmoney.privatbank.ua/ua/";

    private static final String ACTIVE_SEND_BUTTON_CLASS = "content__buttom send_money_step_1";
    private static final String XPATH_SENDER_CART_NUMBER = "/html/body/div[2]/div[3]/div/div[1]/div[1]/div/div[1]/div[1]/form/input";
    private static final String XPATH_SENDER_CARD_EXPIRES_DATE_MONTH = "/html/body/div[2]/div[3]/div/div[1]/div[1]/div/div[1]/div[2]/select[1]";
    private static final String XPATH_SENDER_CARD_EXPIRES_DATE_YEAR = "/html/body/div[2]/div[3]/div/div[1]/div[1]/div/div[1]/div[2]/select[2]";
    private static final String XPATH_SENDER_CARD_CVV2 = "/html/body/div[2]/div[3]/div/div[1]/div[1]/div/div[2]/div/form/input";
    private static final String XPATH_RECEIVER_CARD_NUMBER = "//*[@id=\"receiver_card\"]/input";
    private static final String XPATH_AMMOUNT = "//*[@id=\"amount\"]";
    private static final String XPATH_SEND_BUTTON = "/html/body/div[2]/div[3]/div/div[1]/div[9]";

    private Logger log = LoggerFactory.getLogger(SendMoneyPbPage.class);

    private WebDriver webDriver;
    private WebDriverWait generalWebDriverWait;

    public SendMoneyPbPage(WebDriver webDriver) {
        this.webDriver = webDriver;
        generalWebDriverWait = new WebDriverWait(webDriver, DEFAULT_WAIT_TIMEOUT);
        webDriver.get(SENDMONEY_URL);
    }

    /**
     * Fills the "Sender card number" on the Sendmoney page.
     *
     * @param cardNumber string representation of a card number. Should be a 19 values string in the next format:
     *                   XXXX-XXXX-XXXX-XXXX. Any other representation is not acceptable and could lead to errors.
     */
    public void fillSenderCarsNumber(String cardNumber) {
        log.debug("Filling sender card number {}", cardNumber);
        fillCardNumber(XPATH_SENDER_CART_NUMBER, cardNumber);
    }

    /**
     * Fills the "Expires" field of the sender card on the Sendmoney page.
     *
     * @param month month representation. Should be a string of the length of 2 representing a month number
     *              in the next range: 01-12. Where 01 - is January and 12 - is Dcember.
     * @param year  year representation. Should be a string of the length of 2 representing a year.
     *              Should reflect two last digits in a year from Gregorian calendar. Example:
     *              2019 - 19
     *              2020 - 20
     */
    public void fillSenderExpiresDate(String month, String year) {
        log.debug("Filling sender card expire date. Year: {}, Month: {}", year, month);
        choseSelectorByValue(XPATH_SENDER_CARD_EXPIRES_DATE_MONTH, month);
        choseSelectorByValue(XPATH_SENDER_CARD_EXPIRES_DATE_YEAR, year);
    }

    /**
     * Fills CVV2 code field of the sender card on the Sendmoney page.
     *
     * @param cvvCode a string of the length of 3 representing the secret code.
     */
    public void fillCvv2Code(String cvvCode) {
        log.debug("Filling sender cvv2 code");
        WebElement cvv2Element = webDriver.findElement(By.xpath(XPATH_SENDER_CARD_CVV2));
        cvv2Element.sendKeys(cvvCode);
    }

    /**
     * Fills the "Receiver card number" on the Sendmoney page.
     *
     * @param cardNumber string representation of a card number. Should be a 19 values string in the next format:
     *                   XXXX-XXXX-XXXX-XXXX. Any other representation is not acceptable and could lead to errors.
     */
    public void fillReceiverCardNumber(String cardNumber) {
        log.debug("Filling receiver card number {}", cardNumber);
        fillCardNumber(XPATH_RECEIVER_CARD_NUMBER, cardNumber);
    }

    public void fillAmount(String amount) {
        log.debug("Filling amount field with value {}", amount);
        WebElement amountElement = webDriver.findElement(By.xpath(XPATH_AMMOUNT));
        if (!amountElement.isEnabled()) {
            log.debug("Amount field is unavailable. Wait for {} sec", DEFAULT_WAIT_TIMEOUT);
            generalWebDriverWait.until(elementToBeClickable(amountElement));
        }
        amountElement.sendKeys(amount);
    }

    public TransferConfirmationPage clickSendButton() {
        log.debug("Invoking send button");
        WebElement sendMoneyButton = webDriver.findElement(By.xpath(XPATH_SEND_BUTTON));
        if (isSendButtonDisabled(sendMoneyButton)) {
            log.debug("Send button is not clickable. Wait for {} seconds", DEFAULT_WAIT_TIMEOUT);
            generalWebDriverWait.until(attributeToBe(By.xpath(XPATH_SEND_BUTTON), "class", ACTIVE_SEND_BUTTON_CLASS));
        }
        sendMoneyButton.click();
        return new TransferConfirmationPage(webDriver);
    }

    /*
     * On a page button is unavailable until all fields are filled.
     * In this state buttons class_name end with "disabledbutton".
     * When all fields are filled this suffix is missing.
     */
    private boolean isSendButtonDisabled(WebElement sendMoneyButton) {
        String buttonsClassName = sendMoneyButton.getAttribute("class");
        log.trace("Send money button class name {}", buttonsClassName);
        return buttonsClassName.endsWith("disabledbutton");
    }

    private void choseSelectorByValue(String elementXpath, String value) {
        WebElement selectorElement = webDriver.findElement(By.xpath(elementXpath));
        Select selector = new Select(selectorElement);
        selector.selectByValue(value);
    }

    private void fillCardNumber(String xPathCardNumberInput, String cardNumber) {
        List<WebElement> elements = webDriver.findElements(By.xpath(xPathCardNumberInput));
        List<String> cardNumberBlocks = Arrays.asList(cardNumber.split("-"));
        Iterator<String> iterator = cardNumberBlocks.iterator();
        elements.forEach(element -> element.sendKeys(iterator.next()));
    }
}