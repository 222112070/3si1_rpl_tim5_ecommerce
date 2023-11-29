package com.kel5.ecommerce.service.impl;

import com.kel5.ecommerce.dto.ProductDto;
import com.kel5.ecommerce.entity.Blog;
import com.kel5.ecommerce.entity.Category;
import com.kel5.ecommerce.entity.Image;
import com.kel5.ecommerce.entity.Product;
import com.kel5.ecommerce.entity.Subcategory;
import com.kel5.ecommerce.exception.ResourceNotFoundException;
import com.kel5.ecommerce.mapper.ProductMapper;
import com.kel5.ecommerce.repository.ProductRepository;
import com.kel5.ecommerce.service.CategoryService;
import com.kel5.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private CategoryService categoryService;


    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Long id, Product product) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setStock(product.getStock());
        existingProduct.setWeight(product.getWeight());
        return productRepository.save(existingProduct);
    }
//        @Override
//    public Product updateProduct(Long id, Product updatedProduct) {
//        // Check if the product with the given ID exists
//        Product existingProduct = productRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
//
//        Product updated = productRepository.save(existingProduct);
//        return updated;
//    }

    @Override
    public void deleteProduct(Long id) {
        Optional<Product> product = getProductById(id);
        productRepository.delete(product.get());
    }

    @Override
    public Product saveProduct(ProductDto productDto, Long categoryId, Long subcategoryId) throws Exception {
        Product product = ProductMapper.toEntity(productDto);
        List<Image> images = new ArrayList<>();

        String baseDir = System.getProperty("user.dir");
        String targetDir = baseDir + "/productsImages/";

        // Create the directory if it doesn't exist
        File directory = new File(targetDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        for (MultipartFile file : productDto.getImages()) {
            Path path = Paths.get(targetDir + file.getOriginalFilename());
            Files.write(path, file.getBytes());

            Image image = new Image();
            image.setUrl("/productsImages/" + file.getOriginalFilename());
            images.add(image);
        }

        product.setImage(images);
        Category category = categoryService.getCategoryById(categoryId);
        Subcategory subcategory = categoryService.getSubcategoryById(subcategoryId);

        product.setCategory(category);
        product.setSubcategory(subcategory);
        return productRepository.save(product);
    }

    @Override
    public List<Product> getAllProduct(String keyword) {
       if (keyword != null){
           return productRepository.search(keyword);
       } else 
           return (List<Product>)productRepository.findAll(); 
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }
 
    @Override
    public Page<Product> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return this.productRepository.findAll(pageable);
    }
}
