/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.kel5.ecommerce.service;

import com.kel5.ecommerce.entity.Cart;
import com.kel5.ecommerce.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CartService {
    Cart createCart(Cart cart);
    Cart updateCart(Long id, Cart cart);
    Cart getCartById(Long id);
    void deleteCart(Long id);
    List<Cart> getAllCarts();
    void addProductToCart(Long productId, Integer quantity, User currentUser);

    Cart getCurrentCart();

    boolean performCheckout();
}
