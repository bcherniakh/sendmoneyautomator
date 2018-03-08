package io.github.bcherniakh.sendmoney.domain;

import java.time.LocalDate;

public class Card {
    private String number;
    private LocalDate expiresDate;
    private String securityCode;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDate getExpiresDate() {
        return expiresDate;
    }

    public void setExpiresDate(LocalDate expiresDate) {
        this.expiresDate = expiresDate;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }
}
