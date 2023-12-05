/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kel5.ecommerce.repository;

import com.kel5.ecommerce.entity.User;
import com.kel5.ecommerce.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
    
    List<Order> findByStatus(String keyword);
    
    List<Order> findByUserAndStatus(User user, String orderStatus);
    Order findByUserAndIdAndStatus(User user, Long id,String orderStatus);
    
    List<Order> findByUserAndStatusNot(User user, String excludedStatus);
    Order findByUserAndId(User user, Long Id);
}
