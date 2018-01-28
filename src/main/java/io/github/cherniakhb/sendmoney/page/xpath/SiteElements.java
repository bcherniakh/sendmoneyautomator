package io.github.cherniakhb.sendmoney.page.xpath;

/**
 * Aggregates the meaningful elements of the PB Sendmoney site
 */
public final class SiteElements {

    public enum MainPage {

        SENDER_CARD_NUMBER("/html/body/div[2]/div[3]/div/div[1]/div[1]/div/div[1]/div[1]/form/input"),
        SENDER_CARD_EXPIRES_DATE_MONTH("/html/body/div[2]/div[3]/div/div[1]/div[1]/div/div[1]/div[2]/select[1]"),
        SENDER_CARD_EXPIRES_DATE_YEAR("/html/body/div[2]/div[3]/div/div[1]/div[1]/div/div[1]/div[2]/select[2]"),
        SENDER_CARD_CVV2("/html/body/div[2]/div[3]/div/div[1]/div[1]/div/div[2]/div/form/input"),
        RECEIVER_CARD_NUMBER("//*[@id=\"receiver_card\"]/input"),
        AMOUNT("//*[@id=\"amount\"]"),
        SEND_BUTTON("/html/body/div[2]/div[3]/div/div[1]/div[9]");

        private String xPath;

        MainPage(String xPath) {
            this.xPath = xPath;
        }

        public String xPath() {
            return xPath;
        }
    }

    public enum ConfirmationPage {

        PHONE_NUMBER_FIELD("//*[@id=\"step2Phone\"]"),
        SEND_BUTTON("/html/body/div[2]/div[3]/div/div[15]/div");

        private String xPath;

        ConfirmationPage(String xPath) {
            this.xPath = xPath;
        }

        public String xPath() {
            return xPath;
        }
    }
}
