package com.kel5.ecommerce.controller;

import com.kel5.ecommerce.dto.ProductDto;
import com.kel5.ecommerce.entity.Category;
import com.kel5.ecommerce.entity.Product;
import com.kel5.ecommerce.entity.Subcategory;
import com.kel5.ecommerce.entity.User;
import com.kel5.ecommerce.exception.ResourceNotFoundException;
import com.kel5.ecommerce.repository.CategoryRepository;
import com.kel5.ecommerce.repository.ProductRepository;
import com.kel5.ecommerce.service.CategoryService;
import com.kel5.ecommerce.service.ProductService;
import com.kel5.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;

@Controller
@SessionAttributes("name")
@RequestMapping("/admin/")
public class ProductController {

    @Autowired
    private ProductService productService;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private UserService userService;
    @GetMapping("/produk")
    public String viewHomePage(Model model) {
                User user = userService.getUserLogged();
        model.addAttribute("user", user);
        return findPaginated(1, "id", "asc", model);
    }

    @RequestMapping("produk")
    public String Project1(Model model, @RequestParam(name = "keyword", required = false) String keyword) {
        List<Product> listProduct = productService.getAllProduct(keyword);
        model.addAttribute("listProduct", listProduct);
        model.addAttribute("keyword", keyword);
        return "admin/Product";
    }

    // To display the form for adding a product
    @GetMapping("/add-product")
    public String showAddProductForm(Model model) {
        User user = userService.getUserLogged();
        model.addAttribute("user", user);
        model.addAttribute("productDto", new ProductDto());
        return "admin/addProductForm";
    }

    // To handle the form submission
    @PostMapping("/create-product")
    public String createProduct(@ModelAttribute ProductDto productDto, 
                                @RequestParam("category") Long categoryId, 
                                @RequestParam("subcategory") Long subcategoryId, 
                                Model model) throws Exception {

        Product savedProduct = productService.saveProduct(productDto, categoryId, subcategoryId);
        model.addAttribute("message", "Product saved successfully");
        return "redirect:/admin/produk";
    }

    @GetMapping("/products/view/{id}")
    public String viewProduct(@PathVariable("id") Long id, Model model) {
        User user = userService.getUserLogged();
        model.addAttribute("user", user);
        Optional<Product> product = productService.getProductById(id);
        if (product.isPresent()) {
            model.addAttribute("product", product.get());
            return "admin/rincian_produk";
        } else {
            // Handle product not found scenario
            model.addAttribute("error", "Product not found");
            return "product/Product";
        }
    }
    // To display the form for editing a product

    @GetMapping("/edit-product/{id}")
    public String showProductFormForUpdate(@PathVariable("id") Long id, Model model) {
        User user = userService.getUserLogged();
        model.addAttribute("user", user);
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get(); // Get the Product from the Optional
            model.addAttribute("product", product);
            return "admin/updateProductForm";
        } else {
            model.addAttribute("error", "Product not found");
            return "admin/Product"; // Redirect to an appropriate page or view.
        }
    }


    // Handle the form submission for editing
    @PostMapping("/update-product/{id}")
    public String updateProduct(@PathVariable("id") Long id, @ModelAttribute Product product, Model model) {
        try {
            productService.updateProduct(id, product);
            model.addAttribute("message", "Product updated successfully");
            return "redirect:/user/products/view/{id}"; // Redirect to the desired page after successful update.
        } catch (ResourceNotFoundException e) {
            // Handle product not found scenario
            model.addAttribute("error", "Product not found");
            return "admin/Product"; // Redirect to an appropriate page or view.
        }
    }

    @GetMapping("produk/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,
            @RequestParam("sortField") String sortField,
            @RequestParam("sortDir") String sortDir,
            Model model) {
        User user = userService.getUserLogged();
        model.addAttribute("user", user);
        int pageSize = 10;

        Page<Product> page = productService.findPaginated(pageNo, pageSize, sortField, sortDir);
        List<Product> listProduct = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reserveSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("listProduct", listProduct);
        return "admin/Product";
    }
    @GetMapping("/delete-product/{id}")
    public String deleteProduct(@PathVariable("id") Long id, Model model) {
        try {
            productService.deleteProduct(id);
            model.addAttribute("message", "Product deleted successfully");
            return "redirect:/admin/produk";
        } catch (ResourceNotFoundException e) {
            // Handle product not found scenario
            model.addAttribute("error", "Product not found");
            return "admin/Product"; // Redirect to an appropriate page or view.
        }
    }
}
