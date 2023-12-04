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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    /* Tombol Cancel di html
    <td>
        <span th:text="${order.status}" th:class="..."></span>
        <a th:if="${order.status == 'Belum Dibayar' || order.status == 'Belum Dikonfirmasi'}"
        th:href="@{/user/order/cancel/{orderId}(orderId=${order.id})}"
        class="btn btn-danger btn-sm">Cancel</a>
    </td>
     */

    @GetMapping("/order/cancel/{orderId}")
    public String cancelOrder(@PathVariable("orderId") Long orderId, RedirectAttributes redirectAttributes) {
        boolean isCancelled = orderService.cancelOrder(orderId);
        if (isCancelled) {
            redirectAttributes.addFlashAttribute("successMessage", "Pesanan berhasil dibatalkan.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Pesanan tidak dapat dibatalkan.");
        }
        return "redirect:/user/order-list";
    }


}
