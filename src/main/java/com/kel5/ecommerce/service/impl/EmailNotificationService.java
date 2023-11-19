package com.kel5.ecommerce.service.impl;

import com.kel5.ecommerce.entity.Order;
import com.kel5.ecommerce.service.OrderObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailNotificationService implements OrderObserver {

    @Autowired
    private JavaMailSender mailSender;

    private static final String SELLER_EMAIL = "jimbungindustrial@gmail.com";

    @Override
    public void onOrderCreated(Order order) {
        // Mengirim email ke pembeli
        sendEmail(order.getUser().getEmail(), "Order Confirmation",
                "Your order with id " + order.getId() + " has been successfully created.");

        // Mengirim email ke penjual
        sendEmail(SELLER_EMAIL, "New Order Received",
                "A new order with id " + order.getId() + " has been placed.");
    }

    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@example.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}

