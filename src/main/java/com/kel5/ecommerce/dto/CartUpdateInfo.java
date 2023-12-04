package com.kel5.ecommerce.dto;

import lombok.*;

@Getter
@Setter
@Builder
public class CartUpdateInfo {
    private final float newTotal;
    private final int itemQuantity;

    public CartUpdateInfo(float newTotal, int itemQuantity) {
        this.newTotal = newTotal;
        this.itemQuantity = itemQuantity;
    }

    // getters
    public float getNewTotal() {
        return newTotal;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }
}

