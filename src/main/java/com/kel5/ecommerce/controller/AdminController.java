package com.kel5.ecommerce.controller;

import com.kel5.ecommerce.entity.Order;
import com.kel5.ecommerce.entity.Product;
import com.kel5.ecommerce.entity.User;
import com.kel5.ecommerce.exception.ResourceNotFoundException;
import com.kel5.ecommerce.repository.CategoryRepository;
import com.kel5.ecommerce.repository.ProductRepository;
import com.kel5.ecommerce.repository.UserRepository;
import com.kel5.ecommerce.service.*;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes("name")
@RequestMapping("/admin/")
public class AdminController {
    @Autowired
    private UserService userService;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    
    private String getLogedinUsername() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
//
//    @GetMapping("/")
//    public String registrationForm(ModelMap model) {
//        String username = getLogedinUsername();
//        return "admin";
//    }

    @GetMapping("/")
    public String DashboardAdmin(Model model) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        User user = userService.getUserLogged();
        model.addAttribute("user", user);
        return "admin/index";
    }

    @GetMapping("/pesanan")
    public String Pesanan(Model model, @RequestParam(defaultValue="") String keyword) {
        User user = userService.getUserLogged();
        model.addAttribute("user", user);
        List<Order> orders;
        if(keyword.isEmpty()){
            orders = orderService.getAllOrders();
        } else {
            orders = orderService.filterOrder(keyword);
        }
        model.addAttribute("orders",orders);
        if(orders.isEmpty()){
            model.addAttribute("notFound", true);
        }
        return "admin/pesanan";
    }
    
    @PostMapping("/pesanan/edit/{orderId}")
    public String editPesanan(@PathVariable("orderId") Long id,
                              @RequestParam("status") String status,
                              @RequestParam("totalAmountFix") float totalAmountFix) {
        
        System.out.println("Update order " + id + " with status " + status + "and total amount" + totalAmountFix + ".");
        orderService.updateOrder(id, status, totalAmountFix);
        return "redirect:/admin/pesanan";
    }

    @GetMapping("/pelanggan")
    public String Pelanggan(Model model) {
        User user = userService.getUserLogged();
        List<User> customers = userService.getAllCustomer();
        model.addAttribute("user", user);
        model.addAttribute("customers", customers);
        return "admin/pelanggan";
    }
    
    @GetMapping("/pesanan/detail")
    public String DetailPesanan(Model model) {
        User user = userService.getUserLogged();
        model.addAttribute("user", user);
        return "admin/rincian_pesanan";
    }
    
    @GetMapping("/produk/detail")
    public String DetailProduk(Model model) {
        User user = userService.getUserLogged();
        model.addAttribute("user", user);
        return "admin/rincian_produk";
    }
    
    @GetMapping("/profil")
    public String ViewProfile(Model model) {
        User user = userService.getUserLogged();
        model.addAttribute("user", user);
        return "admin/admins-profile";
    }
}
