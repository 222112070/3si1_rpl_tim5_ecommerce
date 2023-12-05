/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.kel5.ecommerce.service;

import com.kel5.ecommerce.entity.Order;
import com.kel5.ecommerce.entity.User;

import java.util.List;

public interface OrderService {
    Order createOrder(Order order);
    Order updateOrder(Long id, String status, float totalAmountFix);
    Order updateOrderByUser(Long id, String status);
    com.kel5.ecommerce.entity.Order getOrderById(Long id);
    void deleteOrder(Long id);
    List<Order> getAllOrders();
    List<Order> getOrdersForLoggedInUser();
    List<Order> getOrderDoneForLoggedInUser(String orderStatus);
    List<Order> getOrderOnProgressForLoggedInUser(String orderStatus);
    Order createOrderFromCart(String name,String address,String whatsapp, String notes);
    void createOrderFromProduct(Long productId, Integer quantity);
    void registerObserver(OrderObserver observer);
    String createOrderMessage(Long orderId);
    void updateTotalSpentUser(Long orderId);
    List<Order> filterOrder(String keyword);
    boolean cancelOrder(User user,Long orderId);
    Order getOrderByIdForLoggedInUser(Long orderId);
}