package com.gazpromtrans.trainingtasknew.screen.contract;

import io.jmix.ui.Notifications;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Test {
    private static final Logger log = LoggerFactory.getLogger(Test.class);
    @Autowired
    private ObjectProvider<Notifications> notificationsProvider;
}