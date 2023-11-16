/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kel5.ecommerce.mapper;


import com.kel5.ecommerce.dto.CartDto;
import com.kel5.ecommerce.dto.CartItemDto;
import com.kel5.ecommerce.entity.Cart;
import com.kel5.ecommerce.entity.CartItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartMapper {

    public static CartDto toDto(Cart cart) {
        List<CartItemDto> cartItemDTOs = cart.getCartItems().stream()
                .map(CartMapper::toCartItemDto)
                .collect(Collectors.toList());

        Long totalPrice = cartItemDTOs.stream()
                .mapToLong(CartItemDto::getTotalItemPrice)
                .sum();

        return new CartDto(cartItemDTOs, totalPrice);
    }

    private static CartItemDto toCartItemDto(CartItem cartItem) {
        long price = (long) cartItem.getProduct().getPrice();
        Long totalItemPrice = price * cartItem.getQuantity();

        return new CartItemDto(
                cartItem.getProduct().getImage().get(0).getUrl(),
                cartItem.getProduct().getName(),
                cartItem.getQuantity(),
                price,
                totalItemPrice
        );
    }
}

