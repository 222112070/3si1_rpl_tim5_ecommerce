package com.kel5.ecommerce.controller;

import com.kel5.ecommerce.entity.Order;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import com.kel5.ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
        String phoneNumber = "6285176744224";
        String message = "Hello, I'm interested in your products.";
        String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);

        return "redirect:https://wa.me/" + phoneNumber + "?text=" + encodedMessage;
    }

    @GetMapping("/order/message/{orderId}")
    public String getOrderMessage(@PathVariable Long orderId) {
        return orderService.createOrderMessage(orderId);
    }

    @GetMapping("/order/whatsapp/{orderId}")
    public String redirectToWhatsAppWithOrderMessage(Model model, @PathVariable Long orderId) {
        Order order = orderService.getOrderByIdForLoggedInUser(orderId);
        if (order != null) {
            String message = orderService.createOrderMessage(orderId);
            String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);
            String phoneNumber = "6281278984640";

            model.addAttribute("whatsappUrl", "https://wa.me/" + phoneNumber + "?text=" + encodedMessage);
            model.addAttribute("orderId", orderId);

            // Return view name for the order confirmation page
            return "user/KonfirmasiOrder";
        } else {
            return "redirect:/user/order";
        }
    }
}