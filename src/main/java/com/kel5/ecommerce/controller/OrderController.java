/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kel5.ecommerce.controller;

import com.kel5.ecommerce.dto.UserDto;
import com.kel5.ecommerce.entity.Cart;
import com.kel5.ecommerce.entity.Category;
import com.kel5.ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import com.kel5.ecommerce.entity.Order;
import com.kel5.ecommerce.entity.Subcategory;
import com.kel5.ecommerce.entity.User;
import com.kel5.ecommerce.mapper.CartMapper;
import com.kel5.ecommerce.service.CartService;
import com.kel5.ecommerce.service.CategoryService;
import com.kel5.ecommerce.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

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

    @Autowired
    CategoryService categoryService;
    
    @Autowired
    private CartService cartService;
    
    @Autowired
    UserService userService;
        
    @GetMapping("/order-list")
    public String registrationForm(Model model) {
        List<Order> orders = orderService.getAllOrders();
        return "order/orderList";
    }

    @GetMapping("/order")
    public String seeMyOrder(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        List<Subcategory> subcategories = categoryService.getAllSubcategories();
        Cart cart = cartService.getCurrentCart(); // Assumes a method to get current cart
        model.addAttribute("cart", CartMapper.toDto(cart));
        model.addAttribute("categories", categories);
        model.addAttribute("subcategories", subcategories);
        List<Order> orders = orderService.getOrderOnProgressForLoggedInUser("Selesai");
        for (Order order : orders) {
            double orderAmount = order.getTotalAmount(); 
            String formattedAmount = orderService.formatToRupiah(orderAmount);
            order.setAmountFormatted(formattedAmount);
        }
        model.addAttribute("orders",orders);
        return "user/MyOrder";
    }
    
    @GetMapping("/order/detail/{orderId}")
    public String seeDetail(Model model,@PathVariable Long orderId) {
        List<Category> categories = categoryService.getAllCategories();
        List<Subcategory> subcategories = categoryService.getAllSubcategories();
        Cart cart = cartService.getCurrentCart(); // Assumes a method to get current cart
        model.addAttribute("cart", CartMapper.toDto(cart));
        model.addAttribute("categories", categories);
        model.addAttribute("subcategories", subcategories);
        Order order = orderService.getOrderByIdForLoggedInUser(orderId);
        if (order != null) {
            model.addAttribute("order", order);
            return "user/MyOrderDetail";
        } else {
            return "redirect:/user/order";
        }
        
    }
    
    @GetMapping("/order/confirm/{orderId}")
    public String editPesanan(@PathVariable("orderId") Long id) {
        orderService.updateOrderByUser(id, "Selesai");
        orderService.updateTotalSpentUser(id);
        return "redirect:/user/order";
    }

    @GetMapping("/order/cancel/{orderId}")
    public String cancelOrder(@PathVariable("orderId") Long orderId, RedirectAttributes redirectAttributes) {
        User user = userService.getUserLogged();
        boolean isCancelled = orderService.cancelOrder(user,orderId);
        if (isCancelled) {
            redirectAttributes.addFlashAttribute("successMessage", "Pesanan berhasil dibatalkan.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Pesanan tidak dapat dibatalkan.");
        }
        return "redirect:/user/order";
    }
    
    @GetMapping("/order/confirmation/{orderId}")
    public String confirm(Model model,@PathVariable Long orderId) {
        List<Category> categories = categoryService.getAllCategories();
        List<Subcategory> subcategories = categoryService.getAllSubcategories();
        Cart cart = cartService.getCurrentCart(); // Assumes a method to get current cart
        model.addAttribute("cart", CartMapper.toDto(cart));
        model.addAttribute("categories", categories);
        model.addAttribute("subcategories", subcategories);
        Order order = orderService.getOrderByIdForLoggedInUser(orderId);
        if (order != null) {
            model.addAttribute("order", order);
            return "user/OrderConfirmationBridge";
        } else {
            return "redirect:/user/order";
        }
        
    }
   
}
