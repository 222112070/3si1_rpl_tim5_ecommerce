/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kel5.ecommerce.controller;

import com.kel5.ecommerce.entity.Blog;
import com.kel5.ecommerce.entity.Category;
import com.kel5.ecommerce.entity.Subcategory;
import com.kel5.ecommerce.entity.User;
import com.kel5.ecommerce.service.CategoryService;
import com.kel5.ecommerce.service.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 *
 * @author asus
 */
@Controller
@SessionAttributes("name")
@RequestMapping("/admin/")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/add-category")
    public String addCategory(Model model) {
                User user = userService.getUserLogged();
        model.addAttribute("user", user);
        Category category = new Category();
        model.addAttribute("category", category);
        return "admin/addCategoryForm";
    }
    
    @GetMapping("/add-subcategory")
    public String addSubcategory(Model model) {
        Subcategory subcategory = new Subcategory();
        List<Category> categories = categoryService.getAllCategories();
        User user = userService.getUserLogged();
        model.addAttribute("user", user);
         model.addAttribute("subcategory", subcategory);
         model.addAttribute("categories", categories);
         return "admin/addSubcategoryForm";
    }
    
    @PostMapping("/saveCategory")
    public String saveCategory(@ModelAttribute("category") Category category, Model model) {
        try {
            Category saveCategory = categoryService.saveCategory(category);
            model.addAttribute("message", "Category saved successfully");
        } catch (Exception e) {
            model.addAttribute("error", "Error saving category: " + e.getMessage());
        }
        return "redirect:/admin/add-product";
    }
    
    @PostMapping("/saveSubcategory")
    public String saveSubcategory(@ModelAttribute("subcategory") Subcategory subcategory, 
                                  @RequestParam("category") Long categoryId,
                                  Model model) {
        try {
            Subcategory saveSubcategory = categoryService.saveSubcategory(subcategory, categoryId);
            model.addAttribute("message", "Subcategory saved successfully");
        } catch (Exception e) {
            model.addAttribute("error", "Error saving Subcategory: " + e.getMessage());
        }
        return "redirect:/admin/add-product";
    }
}