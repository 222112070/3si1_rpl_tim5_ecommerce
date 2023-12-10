/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kel5.ecommerce.service.impl;

import com.kel5.ecommerce.dto.CartUpdateInfo;
import com.kel5.ecommerce.entity.Cart;
import com.kel5.ecommerce.entity.CartItem;
import com.kel5.ecommerce.entity.Product;
import com.kel5.ecommerce.entity.User;
import com.kel5.ecommerce.exception.ResourceNotFoundException;
import com.kel5.ecommerce.repository.CartItemRepository;
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
    @Autowired
    private CartItemRepository cartItemRepository;

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

    @Override
    public void addProductToCart(Long productId, Integer quantity, String size, User currentUser) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isEmpty()) {
            throw new IllegalArgumentException("Product not found");
        }
        Product product = productOptional.get();

        Cart cart = userService.getUserLogged().getCarts();

        Optional<CartItem> existingCartItemOptional = cart.getCartItems().stream()
                .filter(item -> item.getProduct().equals(product) && item.getSize().equals(size))
                .findFirst();

        CartItem cartItem;
        if (existingCartItemOptional.isPresent()) {
            cartItem = existingCartItemOptional.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setSize(size);
            cartItem.setCart(cart);
            cart.getCartItems().add(cartItem);
        }

        cartItemRepository.save(cartItem);

        long totalPrice = 0;
        for (CartItem item : cart.getCartItems()) {
            float itemTotal = item.getProduct().getPrice() * item.getQuantity();
            totalPrice += itemTotal;
        }
        cart.setTotalPrice(totalPrice);

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

    @Override
    public void removeCartItem(Long cartItemId) {
        User currentUser = userService.getUserLogged();
        Cart cart = currentUser.getCarts();

        CartItem itemToRemove = cart.getCartItems().stream()
                .filter(item -> item.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found"));

        cart.getCartItems().remove(itemToRemove);

        long totalPrice = 0;
        for (CartItem item : cart.getCartItems()) {
            totalPrice += item.getProduct().getPrice() * item.getQuantity();
        }
        cart.setTotalPrice(totalPrice);

        cartRepository.save(cart);
    }

    @Override
    public CartUpdateInfo incrementQuantity(Long cartItemId) {
        return updateCartItemQuantity(cartItemId, 1);
    }

    @Override
    public CartUpdateInfo decrementQuantity(Long cartItemId) {
        return updateCartItemQuantity(cartItemId, -1);
    }

    private CartUpdateInfo updateCartItemQuantity(Long cartItemId, int delta) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("CartItem not found with id: " + cartItemId));

        int newQuantity = cartItem.getQuantity() + delta;
        if (newQuantity <= 0) {
            throw new IllegalArgumentException("Quantity cannot be less than or equal to zero");
        }
        cartItem.setQuantity(newQuantity);

        cartItemRepository.save(cartItem);

        Cart cart = cartItem.getCart();
        recalculateCartTotal(cart);
        return new CartUpdateInfo(cart.getTotalPrice(), newQuantity);
    }

    private void recalculateCartTotal(Cart cart) {
        long totalPrice = 0;
        for (CartItem item : cart.getCartItems()) {
            totalPrice += item.getProduct().getPrice() * item.getQuantity();
        }
        cart.setTotalPrice(totalPrice);
        cartRepository.save(cart);
    }
}
