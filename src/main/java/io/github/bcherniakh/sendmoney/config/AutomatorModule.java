package io.github.bcherniakh.sendmoney.config;

import com.google.inject.AbstractModule;
import io.github.bcherniakh.sendmoney.controller.AppController;
import io.github.bcherniakh.sendmoney.controller.impl.SeleniumSendMoneyController;

public class AutomatorModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(AppController.class).to(SeleniumSendMoneyController.class );
    }

}
