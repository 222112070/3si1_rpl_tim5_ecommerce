/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.kel5.ecommerce.service;

import com.kel5.ecommerce.entity.Cart;
import com.kel5.ecommerce.entity.Order;
import com.kel5.ecommerce.service.impl.EmailNotificationService;
import jakarta.transaction.Transactional;

import java.util.List;

public interface OrderService {
    Order createOrder(Order order);
    Order updateOrder(Long id, Order order);

//    String createOrderMessage(Long orderId);

    com.kel5.ecommerce.entity.Order getOrderById(Long id);
    void deleteOrder(Long id);
    List<Order> getAllOrders();

    List<Order> getOrdersForLoggedInUser();

    Order createOrderFromCart(String name,String address,String whatsapp, String notes);

    void createOrderFromProduct(Long productId, Integer quantity);
    public void registerObserver(OrderObserver observer);
    String createOrderMessage(Long orderId);
}