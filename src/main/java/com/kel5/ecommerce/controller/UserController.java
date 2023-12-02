package com.kel5.ecommerce.controller;

import com.kel5.ecommerce.entity.Announcement;
import com.kel5.ecommerce.entity.Blog;
import com.kel5.ecommerce.entity.Category;
import com.kel5.ecommerce.entity.Image;
import com.kel5.ecommerce.entity.Order;
import com.kel5.ecommerce.entity.Product;
import com.kel5.ecommerce.entity.Subcategory;
import com.kel5.ecommerce.entity.User;
import com.kel5.ecommerce.repository.AnnouncementRepository;
import com.kel5.ecommerce.repository.BlogRepository;
import com.kel5.ecommerce.repository.CategoryRepository;
import com.kel5.ecommerce.repository.SubcategoryRepository;
import com.kel5.ecommerce.repository.ProductRepository;
import com.kel5.ecommerce.service.BlogService;
import com.kel5.ecommerce.service.CategoryService;
import com.kel5.ecommerce.service.ImageService;
import com.kel5.ecommerce.service.OrderService;
import com.kel5.ecommerce.service.ProductService;
import com.kel5.ecommerce.service.UserService;
import com.kel5.ecommerce.util.FileUploadUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.sql.rowset.serial.SerialException;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@SessionAttributes("name")
@RequestMapping("/user/")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;
    
    @Autowired
    private ImageService imageService;
    
    @Autowired
    private CategoryService categoryService;
        
    @Autowired
    private AnnouncementRepository announcementRepository;
        
    @Autowired
    private BlogService blogService;


    private String getLogedinUsername() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
    
    @GetMapping("/")
    public String userHome(ModelMap model){
        String username = getLogedinUsername();
        List<Category> categories = categoryService.getAllCategories();
        List<Subcategory> subcategories = categoryService.getAllSubcategories();
        List<Blog> blogs = blogService.getAllBlogs();
        model.addAttribute("categories", categories);
        model.addAttribute("subcategories", subcategories);
        model.addAttribute("blogs", blogs);
        return "user/index";
    } 
    
    @GetMapping("/shop")
    public String shop(ModelMap model){
        String username = getLogedinUsername();
        List<Product> products = productService.findProductAvailable(0);
        List<Category> categories = categoryService.getAllCategories();
        List<Subcategory> subcategories = categoryService.getAllSubcategories();
        model.addAttribute("categories", categories);
        model.addAttribute("subcategories", subcategories);
        model.addAttribute("products", products);
        return "user/shop";
    }    
    
    @GetMapping("/announcement")
    public String announcement(ModelMap model){
        User user = userService.getUserLogged();
        System.out.println(user.getType());
        if("Regular".equals(user.getType())){
            return "redirect:/user/";
        }
        else{
        List<Category> categories = categoryService.getAllCategories();
        List<Subcategory> subcategories = categoryService.getAllSubcategories();
            model.addAttribute("categories", categories);
            model.addAttribute("subcategories", subcategories);
            List<Announcement> announcement = announcementRepository.findAll();
            model.addAttribute("announcement", announcement);
            return "user/announcement";
        }
    } 
    
    @GetMapping("/shop/{subcategoryId}")
    public String viewSubcategory(@PathVariable Long subcategoryId, Model model) {
        String username = getLogedinUsername();
        Subcategory subcategory = categoryService.getSubcategoryById(subcategoryId);
        List<Product> products = productService.findProductInSubcategoryAvailable(subcategory,0);
        List<Category> categories = categoryService.getAllCategories();
        List<Subcategory> subcategories = categoryService.getAllSubcategories();
            model.addAttribute( "categories", categories);
            model.addAttribute("subcategories", subcategories);
            model.addAttribute("products", products);
            return "/user/shop";
    }
    
    @GetMapping("/shop-detail/{productId}")
    public String shopDetail(@PathVariable("productId") Long id, Model model) {
    String username = getLogedinUsername();
    Optional<Product> product = productService.getProductByIdAndStockNot(id,0);
    List<Category> categories = categoryService.getAllCategories();
    List<Subcategory> subcategories = categoryService.getAllSubcategories();
    model.addAttribute("categories", categories);
    model.addAttribute("subcategories", subcategories);
    if (product.isPresent()) {
        Product currentProduct = product.get();
        model.addAttribute("product", currentProduct);
        List<Product> relatedProducts = productService.findProductInCategoryAvailable(currentProduct.getCategory(),0);
        relatedProducts.removeIf(productFilter -> currentProduct.getName().equals(productFilter.getName()));
        model.addAttribute("related", relatedProducts);
        return "user/shop-detail";
    } else {
        model.addAttribute("error", "Product not found");
        return "user/catalogue";
    }
}
    
    
    @GetMapping("/about")
    public String about(ModelMap model){
        String username = getLogedinUsername();
        List<Category> categories = categoryService.getAllCategories();
        List<Subcategory> subcategories = categoryService.getAllSubcategories();
        model.addAttribute("categories", categories);
        model.addAttribute("subcategories", subcategories);
        return "user/about";
    }    
    
    @GetMapping("/checkout")
    public String checkout(ModelMap model){
        String username = getLogedinUsername();
        List<Category> categories = categoryService.getAllCategories();
        List<Subcategory> subcategories = categoryService.getAllSubcategories();
        model.addAttribute("categories", categories);
        model.addAttribute("subcategories", subcategories);
        return "user/checkout";
    }    
    
    @GetMapping("/contact-us")
    public String contactUs(ModelMap model){
        String username = getLogedinUsername();
        List<Category> categories = categoryService.getAllCategories();
        List<Subcategory> subcategories = categoryService.getAllSubcategories();
        model.addAttribute("categories", categories);
        model.addAttribute("subcategories", subcategories);
        return "user/contact-us";
    }    
    
    @GetMapping("/my-account")
    public String myAccount(ModelMap model){
        String statusDone = "Selesai"; 
        String username = getLogedinUsername();
        User user = userService.getUserLogged();
        List <Order> orders = orderService.getOrderDoneForLoggedInUser(statusDone);
        List<Category> categories = categoryService.getAllCategories();
        List<Subcategory> subcategories = categoryService.getAllSubcategories();
        model.addAttribute("user", user);
        model.addAttribute("categories", categories);
        model.addAttribute("orders", orders);
        model.addAttribute("subcategories", subcategories);
        return "user/my-account";
    }    
    
    @GetMapping("/service")
    public String service(ModelMap model){
        String username = getLogedinUsername();
        List<Category> categories = categoryService.getAllCategories();
        List<Subcategory> subcategories = categoryService.getAllSubcategories();
        model.addAttribute("categories", categories);
        model.addAttribute("subcategories", subcategories);
        return "user/service";
    }    
    
    @GetMapping("/wishlist")
    public String wishlist(ModelMap model){
        String username = getLogedinUsername();
        List<Category> categories = categoryService.getAllCategories();
        List<Subcategory> subcategories = categoryService.getAllSubcategories();
        model.addAttribute("categories", categories);
        model.addAttribute("subcategories", subcategories);
        return "user/wishlist";
    }
}
