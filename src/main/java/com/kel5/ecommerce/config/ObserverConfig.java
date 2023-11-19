package com.kel5.ecommerce.config;

import com.kel5.ecommerce.service.OrderService;
import com.kel5.ecommerce.service.impl.EmailNotificationService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObserverConfig {

    @Autowired
    private OrderService orderService;

    @Autowired
    private EmailNotificationService emailNotificationService;

    @PostConstruct
    public void registerObservers() {
        orderService.registerObserver(emailNotificationService);
    }
}

