package io.github.cherniakhb.sendmoney.page;

import io.github.cherniakhb.sendmoney.exception.InvalidInputException;
import io.github.cherniakhb.sendmoney.util.Validation;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static io.github.cherniakhb.sendmoney.constant.WebConstant.DEFAULT_WAIT_TIMEOUT;
import static io.github.cherniakhb.sendmoney.page.xpath.SiteElements.MainPage.*;
import static java.lang.String.format;
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
    private static final String ERROR_NOT_FOUR_BLOCKS_PATTERN = "Failed to split card number to 4 blocks. Created blocks %d blocks";
    private static final String ERROR_INVALID_BLOCK_LENGTH = "Each card number block should consist of 4 symbols. Symbols in block %d. Block %s";

    private Logger log = LoggerFactory.getLogger(SendMoneyPbPage.class);

    private WebDriver webDriver;
    private Wait<WebDriver> wait;

    public SendMoneyPbPage(WebDriver webDriver, Wait<WebDriver> wait) {
        this.webDriver = webDriver;
        this.wait = wait;
        webDriver.get(SENDMONEY_URL);
    }

    /**
     * Fills the "Sender card number" on the Sendmoney page.
     *
     * @param cardNumber string representation of a card number. Should be a 19 values string in the next format:
     *                   XXXX-XXXX-XXXX-XXXX. Any other representation is not acceptable and could lead to errors.
     */
    public void fillSenderCardNumber(String cardNumber) {
        Validation.notNull(cardNumber, () -> new InvalidInputException("Sender card number can not be null"));

        log.debug("Filling sender card number {}", cardNumber);
        fillCardNumber(SENDER_CARD_NUMBER.xPath(), cardNumber);
    }

    /**
     * Fills the "Expires" field of the sender card on the Sendmoney page.
     *
     * @param month month representation. Should be a string of the length of 2 representing a month number
     *              in the next range: 01-12. Where 01 - is January and 12 - is December.
     * @param year  year representation. Should be a string of the length of 2 representing a year.
     *              Should reflect two last digits in a year from Gregorian calendar. Example:
     *              2019 - 19
     *              2020 - 20
     */
    public void fillSenderExpiresDate(String month, String year) {
        log.debug("Filling sender card expire date. Year: {}, Month: {}", year, month);
        choseSelectorByValue(SENDER_CARD_EXPIRES_DATE_MONTH.xPath(), month);
        choseSelectorByValue(SENDER_CARD_EXPIRES_DATE_YEAR.xPath(), year);
    }

    /**
     * Fills CVV2 code field of the sender card on the Sendmoney page.
     *
     * @param cvvCode a string of the length of 3 representing the secret code.
     */
    public void fillCvv2Code(String cvvCode) {
        log.debug("Filling sender cvv2 code");
        WebElement cvv2Element = webDriver.findElement(By.xpath(SENDER_CARD_CVV2.xPath()));
        cvv2Element.sendKeys(cvvCode);
    }

    /**
     * Fills the "Receiver card number" on the Sendmoney page.
     *
     * @param cardNumber string representation of a card number. Should be a 19 values string in the next format:
     *                   XXXX-XXXX-XXXX-XXXX. Any other representation is not acceptable and could lead to errors.
     */
    public void fillReceiverCardNumber(String cardNumber) {
        Validation.notNull(cardNumber, () -> new InvalidInputException("Receiver card number can not be null"));

        log.debug("Filling receiver card number {}", cardNumber);
        fillCardNumber(RECEIVER_CARD_NUMBER.xPath(), cardNumber);
    }

    public void fillAmount(String amount) {
        log.debug("Filling amount field with value {}", amount);
        WebElement amountElement = webDriver.findElement(By.xpath(AMOUNT.xPath()));
        if (!amountElement.isEnabled()) {
            log.debug("Amount field is unavailable. Wait for {} sec", DEFAULT_WAIT_TIMEOUT);
            wait.until(elementToBeClickable(amountElement));
        }
        amountElement.sendKeys(amount);
    }

    public TransferConfirmationPage clickSendButton() {
        log.debug("Invoking send button");
        WebElement sendMoneyButton = webDriver.findElement(By.xpath(SEND_BUTTON.xPath()));
        if (isSendButtonDisabled(sendMoneyButton)) {
            log.debug("Send button is not clickable. Wait for {} seconds", DEFAULT_WAIT_TIMEOUT);
            wait.until(attributeToBe(By.xpath(SEND_BUTTON.xPath()), "class", ACTIVE_SEND_BUTTON_CLASS));
        }
        sendMoneyButton.click();
        return new TransferConfirmationPage(webDriver, wait);
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
        List<String> cardNumberBlocks = splitCardNumberIntoBlocks(cardNumber);
        verifyCreatedCardNumberBlocksBlocks(cardNumberBlocks);
        Iterator<String> iterator = cardNumberBlocks.iterator();
        elements.forEach(element -> element.sendKeys(iterator.next()));
    }

    private void verifyCreatedCardNumberBlocksBlocks(List<String> cardNumberBlocks) {
        Validation.isFalse(cardNumberBlocks.isEmpty(),
                () -> new InvalidInputException("Failed to tokenize card number. No fields created"));
        Validation.isTrue(cardNumberBlocks.size() == 4,
                () -> new InvalidInputException(format(ERROR_NOT_FOUR_BLOCKS_PATTERN, cardNumberBlocks.size())));
        cardNumberBlocks.forEach(this::validateEachBlockCardNumberBlock);
    }

    private void validateEachBlockCardNumberBlock(String cardNumberBlock) {
        Validation.isTrue(cardNumberBlock.length() == 4, () ->
         new InvalidInputException(format(ERROR_INVALID_BLOCK_LENGTH, cardNumberBlock.length(), cardNumberBlock)));
    }

    private List<String> splitCardNumberIntoBlocks(String cardNumber) {
        return Arrays.asList(cardNumber.split("-"));
    }
}