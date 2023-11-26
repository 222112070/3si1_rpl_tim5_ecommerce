package com.kel5.ecommerce.mapper;

import com.kel5.ecommerce.dto.ProductDto;
import com.kel5.ecommerce.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public static Product toEntity(ProductDto productDto) {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setStock(productDto.getStock());
        product.setWeight(productDto.getWeight());
        product.setStatus(productDto.getStatus());
        return product;
    }
}
