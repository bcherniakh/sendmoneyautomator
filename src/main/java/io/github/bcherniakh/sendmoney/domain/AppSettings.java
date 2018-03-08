package io.github.bcherniakh.sendmoney.domain;

public class AppSettings {

    private Card sender;
    private Card receiver;
    private String sendersPhoneNumber;
    private double amount;

    public AppSettings(Card sender, Card receiver, String sendersPhoneNumber, double amount) {
        this.sender = sender;
        this.receiver = receiver;
        this.sendersPhoneNumber = sendersPhoneNumber;
        this.amount = amount;
    }

    public Card getSender() {
        return sender;
    }

    public void setSender(Card sender) {
        this.sender = sender;
    }

    public Card getReceiver() {
        return receiver;
    }

    public void setReceiver(Card receiver) {
        this.receiver = receiver;
    }

    public String getSendersPhoneNumber() {
        return sendersPhoneNumber;
    }

    public void setSendersPhoneNumber(String sendersPhoneNumber) {
        this.sendersPhoneNumber = sendersPhoneNumber;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
