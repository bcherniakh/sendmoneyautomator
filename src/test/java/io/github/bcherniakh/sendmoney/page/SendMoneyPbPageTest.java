package io.github.bcherniakh.sendmoney.page;

import io.github.bcherniakh.sendmoney.exception.InvalidInputException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Wait;

import java.util.Arrays;
import java.util.List;

import static io.github.bcherniakh.sendmoney.page.xpath.SiteElements.MainPage.RECEIVER_CARD_NUMBER;
import static io.github.bcherniakh.sendmoney.page.xpath.SiteElements.MainPage.SENDER_CARD_NUMBER;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@DisplayName("Main page tests")
class SendMoneyPbPageTest {

    /*todo mock them properly when mockito extension is ready https://github.com/mockito/mockito/issues/445*/
    private WebDriver webDriver;
    private Wait<WebDriver> wait;

    private SendMoneyPbPage page;

    @BeforeEach
    void setUp() {
        webDriver = mock(WebDriver.class);
        wait = mock(Wait.class);
        page = new SendMoneyPbPage(webDriver, wait);
    }

    @Nested
    @DisplayName("Sender card number input")
    class SenderCardNumber {

        @Test
        void fillsFieldWithTokenizedNumber() {
            WebElement firstSenderCardNumberBlock = mock(WebElement.class);
            WebElement secondSenderCardNumberBlock = mock(WebElement.class);
            WebElement thirdSenderCardNumberBlock = mock(WebElement.class);
            WebElement fourthSenderCardNumberBlock = mock(WebElement.class);
            List<WebElement> senderCardNumberInputBlocks = Arrays.asList(
                    firstSenderCardNumberBlock,
                    secondSenderCardNumberBlock,
                    thirdSenderCardNumberBlock,
                    fourthSenderCardNumberBlock
            );

            when(webDriver.findElements(By.xpath(SENDER_CARD_NUMBER.xPath()))).thenReturn(senderCardNumberInputBlocks);

            page.fillSenderCardNumber("1234-5678-9101-2345");
            verify(firstSenderCardNumberBlock).sendKeys("1234");
            verify(secondSenderCardNumberBlock).sendKeys("5678");
            verify(thirdSenderCardNumberBlock).sendKeys("9101");
            verify(fourthSenderCardNumberBlock).sendKeys("2345");
        }

        @Test
        void throwsInvalidInputExceptionWhenInputIsNull() {
            assertThrows(InvalidInputException.class, () -> page.fillSenderCardNumber(null));
        }

        @Test
        void throwsInvalidInputExceptionWhenFailedOnlyTwoBlocksCanBeCreated() {
            InvalidInputException exception = assertThrows(InvalidInputException.class, () -> page.fillSenderCardNumber("12345678-91012345"));
            assertThat(exception.getMessage(), equalTo("Failed to split card number to 4 blocks. Created blocks 2 blocks"));
        }

        @Test
        void throwsInvalidInputExceptionWhenOneOfBlocksIsLessThanFour() {
            InvalidInputException exception = assertThrows(InvalidInputException.class, () -> page.fillSenderCardNumber("1234-5678-9101-345"));
            assertThat(exception.getMessage(), equalTo("Each card number block should consist of 4 symbols. Symbols in block 3. Block 345"));
        }
    }

    @Nested
    @DisplayName("Receiver card number input")
    class ReceiverCardNumber {

        @Test
        void fillsFieldWithTokenizedNumber() {
            WebElement firstReceiverCardNumberBlock = mock(WebElement.class);
            WebElement secondReceiverCardNumberBlock = mock(WebElement.class);
            WebElement thirdReceiverCardNumberBlock = mock(WebElement.class);
            WebElement fourthReceiverCardNumberBlock = mock(WebElement.class);
            List<WebElement> receiverCardNumberInputBlocks = Arrays.asList(
                    firstReceiverCardNumberBlock,
                    secondReceiverCardNumberBlock,
                    thirdReceiverCardNumberBlock,
                    fourthReceiverCardNumberBlock
            );

            when(webDriver.findElements(By.xpath(RECEIVER_CARD_NUMBER.xPath()))).thenReturn(receiverCardNumberInputBlocks);

            page.fillReceiverCardNumber("1234-5678-9101-2345");
            verify(firstReceiverCardNumberBlock).sendKeys("1234");
            verify(secondReceiverCardNumberBlock).sendKeys("5678");
            verify(thirdReceiverCardNumberBlock).sendKeys("9101");
            verify(fourthReceiverCardNumberBlock).sendKeys("2345");
        }

        @Test
        void throwsInvalidInputExceptionWhenInputIsNull() {
            assertThrows(InvalidInputException.class, () -> page.fillReceiverCardNumber(null));
        }

        @Test
        void throwsInvalidInputExceptionWhenFailedOnlyTwoBlocksCanBeCreated() {
            InvalidInputException exception = assertThrows(InvalidInputException.class, () -> page.fillReceiverCardNumber("12345678-91012345"));
            assertThat(exception.getMessage(), equalTo("Failed to split card number to 4 blocks. Created blocks 2 blocks"));
        }

        @Test
        void throwsInvalidInputExceptionWhenOneOfBlocksIsLessThanFour() {
            InvalidInputException exception = assertThrows(InvalidInputException.class, () -> page.fillReceiverCardNumber("1234-5678-9101-345"));
            assertThat(exception.getMessage(), equalTo("Each card number block should consist of 4 symbols. Symbols in block 3. Block 345"));
        }
    }
}