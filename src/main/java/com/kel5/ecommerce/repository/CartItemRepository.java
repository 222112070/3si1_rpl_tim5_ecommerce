package com.kel5.ecommerce.repository;

import com.kel5.ecommerce.entity.Cart;
import com.kel5.ecommerce.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
