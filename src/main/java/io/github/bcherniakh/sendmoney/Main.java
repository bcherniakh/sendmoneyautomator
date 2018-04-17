package io.github.bcherniakh.sendmoney;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.github.bcherniakh.sendmoney.config.AutomatorModule;
import io.github.bcherniakh.sendmoney.controller.AppController;

public class Main {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new AutomatorModule());
        AppController controller = injector.getInstance(AppController.class);
        controller.sendMoney();
    }
}
