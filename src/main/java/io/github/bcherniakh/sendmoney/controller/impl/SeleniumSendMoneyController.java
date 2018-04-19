package io.github.bcherniakh.sendmoney.controller.impl;


import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.bcherniakh.sendmoney.controller.AppController;
import io.github.bcherniakh.sendmoney.domain.AppSettings;
import io.github.bcherniakh.sendmoney.page.SendMoneyPbPage;
import io.github.bcherniakh.sendmoney.page.TransferConfirmationPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Wait;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Singleton
public class SeleniumSendMoneyController implements AppController {

    private AppSettings settings;
    private WebDriver webDriver;
    private Wait<WebDriver> timeoutProvider;

    @Inject
    public SeleniumSendMoneyController(AppSettings settings, WebDriver webDriver, Wait<WebDriver> timeoutProvider) {
        this.settings = settings;
        this.webDriver = webDriver;
        this.timeoutProvider = timeoutProvider;
    }

    public void sendMoney() {
        SendMoneyPbPage startPage = new SendMoneyPbPage(webDriver, timeoutProvider);
        LocalDate expiresDate = settings.getSender().getExpiresDate();
        DateTimeFormatter yearFormatter = DateTimeFormatter.ofPattern("yy");
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MM");

        startPage.fillSenderCardNumber(settings.getSender().getNumber());
        startPage.fillSenderExpiresDate(expiresDate.format(monthFormatter), expiresDate.format(yearFormatter));
        startPage.fillCvv2Code(settings.getSender().getSecurityCode());

        startPage.fillReceiverCardNumber(settings.getReceiver().getNumber());

        startPage.fillAmount(String.format(Locale.ROOT, "%.2f", settings.getAmount()));
        TransferConfirmationPage transferConfirmationPage = startPage.clickSendButton();

        if (transferConfirmationPage.isPhoneNumberFieldPresent()) {
            transferConfirmationPage.fillPhoneNumber(settings.getSendersPhoneNumber());
        }

        transferConfirmationPage.clickSendButton();
    }
}
