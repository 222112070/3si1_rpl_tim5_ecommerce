package com.kel5.ecommerce.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import com.kel5.ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user/")
public class WhatsAppRedirectController {

    @Autowired
    OrderService orderService;

    @GetMapping("/contactWhatsApp")
    public String redirectToWhatsApp() {
        String phoneNumber = "6289527430981";
        String message = "Hello, I'm interested in your products.";
        String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);

        return "redirect:https://wa.me/" + phoneNumber + "?text=" + encodedMessage;
    }

    @GetMapping("/order/message/{orderId}")
    public String getOrderMessage(@PathVariable Long orderId) {
        String message = orderService.createOrderMessage(orderId);
        return message;
    }

    @GetMapping("/order/whatsapp/{orderId}")
    public String redirectToWhatsAppWithOrderMessage(@PathVariable Long orderId) {
        String message = orderService.createOrderMessage(orderId);
        String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);
        String phoneNumber = "6289527430981";

        return "redirect:https://wa.me/" + phoneNumber + "?text=" + encodedMessage;
    }
}
