package com.kel5.ecommerce.service.impl;

import com.kel5.ecommerce.entity.*;
import com.kel5.ecommerce.exception.ResourceNotFoundException;
import com.kel5.ecommerce.repository.CartRepository;
import com.kel5.ecommerce.repository.OrderRepository;
import com.kel5.ecommerce.repository.ProductRepository;
import com.kel5.ecommerce.repository.UserRepository;
import com.kel5.ecommerce.service.OrderObserver;
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
    UserRepository userRepository;
    @Autowired
    ProductRepository productRepository;
    private List<OrderObserver> observers = new ArrayList<>();

    @Override
    public void registerObserver(OrderObserver observer) {
        observers.add(observer);
    }

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
    public Order updateOrder(Long id, String status, float totalAmountFix) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + id));
        existingOrder.setStatus(status);
        existingOrder.setTotalAmount(totalAmountFix);
        return orderRepository.save(existingOrder);
    }
    
    @Override
    public Order updateOrderByUser(Long id, String status) {
        User user = userService.getUserLogged();
        if (user != null) {
            Order existingOrder = orderRepository.findByUserAndIdAndStatus(user,id,"Dalam Pengiriman");
            if(existingOrder != null){
                existingOrder.setStatus(status);
                return orderRepository.save(existingOrder);
            } else{
                throw new ResourceNotFoundException("Order Not Found"); 
            }
        } else {
            throw new ResourceNotFoundException("User Not Found");
        }
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
    public Order createOrderFromCart(String name, String address, String whatsapp, String notes) {
        Cart cart = userService.getUserLogged().getCarts();
        // Create new Order entity and copy properties from Cart
        Order order = Order.builder()
                .name(name)
                .address(address)
                .whatsapp(whatsapp)
                .notes(notes)
                .status("Belum Dikonfirmasi") // Example status, this could be an enum or string depending on your design
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
            orderItem.setSize(cartItem.getSize());
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);
            // If needed, also update the Product stock here
        });
       
        cart.getCartItems().clear(); // This clears the items from the cart
        // Save the now-empty cart to the database
        cartRepository.save(cart); // Assuming you have a cartRepository to save the cart, replace with actual cart service call if different
        // Save the Order and its OrderItems to the database

        orderRepository.save(order);
        notifyObservers(order);

        return order;

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

    private void notifyObservers(Order order) {
        for (OrderObserver observer : observers) {
            observer.onOrderCreated(order);
        }
    }
    
    @Override
    public String createOrderMessage(Long orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);

        if (orderOptional.isEmpty()) {
            return "Order dengan ID: " + orderId + " tidak ditemukan.";
        }

        Order order = orderOptional.get();
        StringBuilder message = new StringBuilder();
        message.append("*KONFIRMASI PESANAN*\n")
                .append("----‐-------------------------------------\n")
                .append("*RINCIAN PEMESAN*\n")
                .append("\nNama Pemesan : ")
                .append(order.getUser().getName())
                .append("\nEmail Pemesan : ")
                .append(order.getUser().getEmail())
                .append("\nNomor WhatApp : ")
                .append(order.getWhatsapp())
                .append("\nTanggal Pemesanan : ")
                .append(order.getOrderDate())
                .append("\nPerkiraan Harga : Rp. ")
                .append(order.getTotalAmount())
                .append("\nAlamat Pesanan :  ")
                .append(order.getAddress())
                .append("\n\n")
                .append("*RINCIAN PESANAN*\n");
        
        int count = 1;
        for (OrderItem item : order.getOrderItems()) {
            message.append("")
                    .append("Nama Produk : ")
                    .append(item.getProduct().getName())
                    .append("\n Ukuran : ")
                    .append(item.getSize())
                    .append("\n Jumlah : ")
                    .append(item.getQuantity())
                    .append("\n\n");
        }

        message.append("\nCatatan Pemesan : ")
                .append(order.getNotes())
                .append("\n----‐-------------------------------------\n")
                .append("Terima kasih");

        return message.toString();

    }
    
    @Override
    public List<Order> filterOrder (String keyword){
        User user = userService.getUserLogged();
        if (user != null) {
            return this.orderRepository.findByStatus(keyword);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public void updateTotalSpentUser(Long id) {
        User user = userService.getUserLogged();
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            float currentTotalSpent = user.getTotalSpent();
            float newTotalSpent = currentTotalSpent + order.getTotalAmount();
            user.setTotalSpent(newTotalSpent);
            if(user.getType().equals("Regular") && newTotalSpent>=50000000){
                user.setType("Vendor");
            }
            userRepository.save(user);
        } else {
            throw new ResourceNotFoundException("Order not found with id " + id);
        }
 
    }

    @Override
    public List<Order> getOrderDoneForLoggedInUser(String orderStatus) {
        User user = userService.getUserLogged();
        if (user != null) {
            return orderRepository.findByUserAndStatus(user,orderStatus);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<Order> getOrderOnProgressForLoggedInUser(String orderStatus) {
        User user = userService.getUserLogged();
        if (user != null) {
            return orderRepository.findByUserAndStatusNot(user,orderStatus);
        } else {
            return Collections.emptyList();
        }
    }
}
