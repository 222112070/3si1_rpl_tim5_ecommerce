/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kel5.ecommerce.service.impl;

import com.kel5.ecommerce.entity.*;
import com.kel5.ecommerce.exception.ResourceNotFoundException;
import com.kel5.ecommerce.repository.CartRepository;
import com.kel5.ecommerce.repository.OrderRepository;
import com.kel5.ecommerce.repository.ProductRepository;
import com.kel5.ecommerce.service.OrderService;
import com.kel5.ecommerce.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    final
    OrderRepository orderRepository;
    final
    UserService userService;
    final CartRepository cartRepository;
    @Autowired
    ProductRepository productRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, UserService userService, CartRepository cartRep) {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.cartRepository = cartRep;
    }

    @Override
    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Order updateOrder(Long id, Order order) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + id));
        // Update properties of existingOrder with those from order
        // ...
        return orderRepository.save(existingOrder);
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + id));
    }

    @Override
    public void deleteOrder(Long id) {
        Order order = getOrderById(id);
        orderRepository.delete(order);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> getOrdersForLoggedInUser() {
        User user = userService.getUserLogged();
        if (user != null) {
            return orderRepository.findByUser(user);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public Order createOrderFromCart() {
        Cart cart = userService.getUserLogged().getCarts();
        // Create new Order entity and copy properties from Cart
        Order order = Order.builder()
                .status("Belum Dibayar") // Example status, this could be an enum or string depending on your design
                .orderDate(LocalDate.now())
                .totalAmount(cart.getTotalPrice())
                .user(cart.getUser()) // Assuming the user is the same as the one associated with the cart
                .orderItems(new ArrayList<>())
                .build();

        // For each CartItem, create a corresponding OrderItem and add it to the Order
        cart.getCartItems().forEach(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);

            // If needed, also update the Product stock here
        });
        // Clear the cart items after saving the order
        cart.getCartItems().clear(); // This clears the items from the cart
        // Save the now-empty cart to the database
        cartRepository.save(cart); // Assuming you have a cartRepository to save the cart, replace with actual cart service call if different
        // Save the Order and its OrderItems to the database
        return orderRepository.save(order);

        // After saving the order, you may want to clear or delete the cart
        // cartService.clearCart(cart);
    }

    @Override
    public void createOrderFromProduct(Long productId, Integer quantity) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isEmpty())
            return;

        Product product = optionalProduct.get();
        User user = userService.getUserLogged();

        Order order = Order.builder()
                .status("Belum Dibayar") // Example status, this could be an enum or string depending on your design
                .orderDate(LocalDate.now())
                .totalAmount(product.getPrice()*quantity)
                .user(user) // Assuming the user is the same as the one associated with the cart
                .orderItems(new ArrayList<>())
                .build();
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(quantity);
        orderItem.setOrder(order);
        order.getOrderItems().add(orderItem);

        orderRepository.save(order);
    }
}
