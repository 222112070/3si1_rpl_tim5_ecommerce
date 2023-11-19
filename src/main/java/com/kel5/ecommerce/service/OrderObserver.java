package com.kel5.ecommerce.service;

import com.kel5.ecommerce.entity.Order;

public interface OrderObserver {
    void onOrderCreated(Order order);
}

