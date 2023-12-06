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
import com.kel5.ecommerce.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Locale;
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
    public Product updateProduct(Long id, ProductDto productDto) throws Exception {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
        
        // Update details
        existingProduct.setName(productDto.getName());
        existingProduct.setDescription(productDto.getDescription());
        existingProduct.setPrice(productDto.getPrice());
        existingProduct.setStock(productDto.getStock());
        existingProduct.setWeight(productDto.getWeight());
        boolean hasNewImages = productDto.getImages() != null && productDto.getImages().stream().anyMatch(file -> !file.isEmpty());
        if (hasNewImages) {
            String uploadDir = "productImages/" + existingProduct.getId();
            // Hapus gambar yang ada
            existingProduct.getImage().clear();
            for (MultipartFile file : productDto.getImages()) {
                if (!file.isEmpty()) {
                    String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
                    FileUploadUtil.saveFile(uploadDir, fileName, file);
                    Image image = new Image();
                    image.setUrl("/productImages/" + existingProduct.getId() + "/" + fileName);
                    existingProduct.getImage().add(image);
                }
            }
        }
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
        Product product1 = product.get();
        product1.setStock(0);
        productRepository.save(product1);
    }

    @Override
    public Product saveProduct(ProductDto productDto, Long categoryId, Long subcategoryId) throws Exception {
        Product product = ProductMapper.toEntity(productDto);
        // Menyimpan produk sebelum menambahkan gambar
        Category category = categoryService.getCategoryById(categoryId);
        Subcategory subcategory = categoryService.getSubcategoryById(subcategoryId);

        product.setCategory(category);
        product.setSubcategory(subcategory);
        product = productRepository.save(product);
        // Proses penyimpanan gambar
        if (!productDto.getImages().isEmpty()) {
            for (MultipartFile file : productDto.getImages()) {
                String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
                String uploadDir = "productImages/" + product.getId();
                FileUploadUtil.saveFile(uploadDir, fileName, file);
                Image image = new Image();
                image.setUrl("/productImages/" + product.getId() + "/" + fileName);
                product.getImage().add(image);
            }
            productRepository.save(product); // Simpan produk setelah menambahkan gambar
        }
        return product;
    }

    @Override
    public List<Product> getAllProduct(String keyword) {
       if (keyword != null){
           return productRepository.search(keyword);
       } else 
           return productRepository.findAll(); 
    }
    @Override
    public List<Product> getAllProducts() {
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

    @Override
    public List<Product> findProductAvailable(int stock) {
        return productRepository.findByStockNot(stock);
    }

    @Override
    public Optional<Product> getProductByIdAndStockNot(Long id, int stock) {
        return productRepository.findByIdAndStockNot(id,stock);
    }

    @Override
    public List<Product> findProductInSubcategoryAvailable(Subcategory subcategory, int stock) {
        return productRepository.findBySubcategoryAndStockNot(subcategory,stock);
    }

    @Override
    public List<Product> findProductInCategoryAvailable(Category category, int stock) {
        return productRepository.findByCategoryAndStockNot(category,stock);
    }
    
    @Override
    public List<Product> getByCategory(Category category) {
        return productRepository.findByCategory(category);
    }
    
    @Override
    public String formatToRupiah(double amount) {
        NumberFormat rupiahFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        return rupiahFormat.format(amount);
    }
}
