/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kel5.ecommerce.controller;

import com.kel5.ecommerce.dto.UserDto;
import com.kel5.ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import com.kel5.ecommerce.entity.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user/")
public class OrderController {

    @Autowired
    OrderService orderService;

    @GetMapping("/order-list")
    public String registrationForm(Model model) {
        List<Order> orders = orderService.getAllOrders();
        return "order/orderList";
    }

    @GetMapping("/order")
    public String seeMyOrder(Model model) {
        List<Order> orders = orderService.getOrderOnProgressForLoggedInUser("Selesai");
        model.addAttribute("orders",orders);
        return "user/MyOrder";
    }
    
    @GetMapping("/order/confirm/{orderId}")
    public String editPesanan(@PathVariable("orderId") Long id) {
        orderService.updateOrderByUser(id, "Selesai");
        orderService.updateTotalSpentUser(id);
        return "redirect:/user/order";
    }
}