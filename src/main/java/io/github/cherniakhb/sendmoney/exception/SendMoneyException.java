package io.github.cherniakhb.sendmoney.exception;

public class SendMoneyException extends RuntimeException {

    public SendMoneyException(String message) {
        super(message);
    }

    public SendMoneyException(String message, Throwable cause) {
        super(message, cause);
    }

    public SendMoneyException(Throwable cause) {
        super(cause);
    }

    public SendMoneyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
