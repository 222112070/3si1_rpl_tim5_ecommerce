package com.kel5.ecommerce.service;

import com.kel5.ecommerce.dto.ProductDto;
import com.kel5.ecommerce.entity.Product;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface ProductService {
    Product saveProduct(ProductDto productDto, Long categoryId, Long subcategoryId) throws Exception;

    Product createProduct(Product product);
    Product updateProduct(Long id, Product product);
    void deleteProduct(Long id);

    List<Product> getAllProduct(String keyword);

    Optional<Product> getProductById(Long id);
    Page<Product> findPaginated(int pageNo, int pageSize,String sortField, String sortDir);

}
