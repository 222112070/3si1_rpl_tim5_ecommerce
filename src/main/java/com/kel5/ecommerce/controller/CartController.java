/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kel5.ecommerce.controller;

import com.kel5.ecommerce.entity.CartItem;
import com.kel5.ecommerce.entity.Cart;
import com.kel5.ecommerce.entity.Category;
import com.kel5.ecommerce.entity.Subcategory;
import com.kel5.ecommerce.entity.User;
import com.kel5.ecommerce.mapper.CartMapper;
import com.kel5.ecommerce.repository.AnnouncementRepository;
import com.kel5.ecommerce.repository.BlogRepository;
import com.kel5.ecommerce.repository.CategoryRepository;
import com.kel5.ecommerce.repository.SubcategoryRepository;
import com.kel5.ecommerce.service.CartService;
import com.kel5.ecommerce.service.OrderService;
import com.kel5.ecommerce.service.UserService;
import com.kel5.ecommerce.service.impl.CartServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user/")
public class CartController {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubcategoryRepository subcategoryRepository;
    @Autowired
    CartService cartService;
    @Autowired
    UserService userService;
    @Autowired
    OrderService orderService;

    @PostMapping("/products/addToCart/{productId}")
    public String addToCart(@AuthenticationPrincipal User currentUser,
                            @PathVariable("productId") Long productId,
                            @RequestParam("quantity") Integer quantity) {
        System.out.println("Added product " + productId + " with quantity " + quantity + " to cart.");
        cartService.addProductToCart(productId, quantity, currentUser);
        return "redirect:/user/cart";
    }

    @GetMapping("/cart")
    public String viewCart(Model model) {
        Cart cart = cartService.getCurrentCart(); // Assumes a method to get current cart
        model.addAttribute("cart", CartMapper.toDto(cart));
        List<Category> categories = categoryRepository.findAll();
        List<Subcategory> subcategories = subcategoryRepository.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("subcategories", subcategories);
        return "user/cart"; // Name of the template that displays the cart
    }

    @PostMapping("/cart/checkout")
    public String checkoutCart(Model model) {
        orderService.createOrderFromCart(); // Assumes a method to perform checkout
        return "user/cart"; // Name of the template that confirms successful checkout
    }
    
    @GetMapping("/cart/{cartId}/delete") 
    public String deleteCartItem(@PathVariable(name = "cartId") Long cartId){
        cartService.deleteCart(cartId);
        return "redirect:/cart";
    }
}
