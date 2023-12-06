package com.kel5.ecommerce.service;

import com.kel5.ecommerce.dto.ProductDto;
import com.kel5.ecommerce.entity.Category;
import com.kel5.ecommerce.entity.Product;
import com.kel5.ecommerce.entity.Subcategory;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {
    Product saveProduct(ProductDto productDto, Long categoryId, Long subcategoryId) throws Exception;
    Product createProduct(Product product);
    Product updateProduct(Long id, ProductDto productDto) throws Exception;
    void deleteProduct(Long id);
    List<Product> getAllProduct(String keyword);
    List<Product> getAllProducts();
    Optional<Product> getProductById(Long id);
    Optional<Product> getProductByIdAndStockNot(Long id, int stock);
    Page<Product> findPaginated(int pageNo, int pageSize,String sortField, String sortDir);
    List<Product> findProductAvailable(int stock);
    List<Product> findProductInSubcategoryAvailable(Subcategory subcategory, int stock);
    List<Product> findProductInCategoryAvailable(Category category, int stock);
    List<Product> getByCategory(Category category);
}
