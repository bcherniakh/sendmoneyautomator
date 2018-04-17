package io.github.bcherniakh.sendmoney.config;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import io.github.bcherniakh.sendmoney.controller.AppController;
import io.github.bcherniakh.sendmoney.controller.impl.SeleniumSendMoneyController;
import io.github.bcherniakh.sendmoney.domain.Card;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.inject.Named;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Properties;

import static io.github.bcherniakh.sendmoney.constant.WebConstant.DEFAULT_WAIT_TIMEOUT;

public class AutomatorModule extends AbstractModule {

    @Override
    protected void configure() {
        Properties properties = new Properties();
        try {
            properties.load(new FileReader("application.properties"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties");
        }

        Names.bindProperties(binder(), properties);
        bind(AppController.class)
                .to(SeleniumSendMoneyController.class)
                .in(Singleton.class);

        bindConstant().annotatedWith(SenderPhone.class).to(properties.getProperty("sender.phone"));
        bindConstant().annotatedWith(MoneyAmount.class).to(properties.getProperty("sender.money.amount"));
    }

    @Provides
    @Singleton
    public Wait<WebDriver> webDriverWait(WebDriver webDriver) {
        return new WebDriverWait(webDriver, DEFAULT_WAIT_TIMEOUT);
    }

    @Provides
    @Singleton
    public WebDriver webDriver(@Named("selenium.webdriver.path") String chromeDriverPath) {
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.Jdk14Logger");
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);

        return new ChromeDriver();
    }

    @Provides
    @Singleton
    @SenderCard
    public Card senderCard(@Named("sender.card.number") String cardNumber,
                           @Named("sender.card.expires") String expiresDate,
                           @Named("sender.card.cvv") String securityCode) {
        Card card = new Card();
        card.setNumber(cardNumber);
        card.setExpiresDate(parseDate(expiresDate));
        card.setSecurityCode(securityCode);
        return card;

    }

    @Provides
    @Singleton
    @ReceiverCard
    public Card receiverCard(@Named("receiver.card.number") String cardNumber) {
        Card card = new Card();
        card.setNumber(cardNumber);

        return card;
    }

    private LocalDate parseDate(String date) {
        DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder()
                .appendPattern("MM/yyyy")
                .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                .toFormatter();

        return LocalDate.parse(date, dateTimeFormatter);
    }

}
