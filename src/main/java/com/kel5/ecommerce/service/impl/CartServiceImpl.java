/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kel5.ecommerce.service.impl;

import com.kel5.ecommerce.entity.Cart;
import com.kel5.ecommerce.entity.CartItem;
import com.kel5.ecommerce.entity.Product;
import com.kel5.ecommerce.entity.User;
import com.kel5.ecommerce.exception.ResourceNotFoundException;
import com.kel5.ecommerce.repository.CartRepository;
import com.kel5.ecommerce.repository.ProductRepository;
import com.kel5.ecommerce.service.CartService;
import com.kel5.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserService userService;

    @Override
    public Cart createCart(Cart cart) {
        return cartRepository.save(cart);
    }

    @Override
    public Cart updateCart(Long id, Cart cart) {
        Cart existingCart = cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found with id " + id));
        // Update properties of existingCart with those from cart
        // ...
        return cartRepository.save(existingCart);
    }

    @Override
    public Cart getCartById(Long id) {
        return cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found with id " + id));
    }

    @Override
    public void deleteCart(Long id) {
        Cart cart = getCartById(id);
        cartRepository.delete(cart);
    }

    @Override
    public List<Cart> getAllCarts() {
        return cartRepository.findAll();
    }

    public void addProductToCart(Long productId, Integer quantity, User currentUser) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isEmpty()) {
            throw new IllegalArgumentException("Product not found");
        }
        Product product = productOptional.get();
        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);

        Optional<Cart> cartOptional = cartRepository.findById(userService.getUserLogged().getCarts().getId());
        if (cartOptional.isEmpty()) {
            throw new IllegalArgumentException("Cart not found");
        }
        Cart cart = cartOptional.get();
        cart.getCartItems().add(cartItem);
        cartItem.setCart(cart);

        cartRepository.save(cart);
    }

    @Override
    public Cart getCurrentCart() {
        User user = userService.getUserLogged();
        return user.getCarts();
    }

    @Override
    public boolean performCheckout() {
        return false;
    }

}
