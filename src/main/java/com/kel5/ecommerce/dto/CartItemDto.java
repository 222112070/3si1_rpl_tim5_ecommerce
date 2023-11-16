package com.kel5.ecommerce.dto;

import com.kel5.ecommerce.entity.Image;
import com.kel5.ecommerce.entity.Product;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto {
    private String productUrl;
    private String productName;
    private int quantity;
    private Long price; // Assuming price is given in its smallest unit (e.g., cents) to avoid float.
    private Long totalItemPrice;
}