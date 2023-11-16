/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kel5.ecommerce.controller;

import com.kel5.ecommerce.entity.Blog;
import com.kel5.ecommerce.entity.Category;
import com.kel5.ecommerce.entity.Subcategory;
import com.kel5.ecommerce.service.CategoryService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 *
 * @author asus
 */
@RestController
@SessionAttributes("name")
@RequestMapping("/admin/")
public class CategoryApiController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories")
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/subcategories/{categoryId}")
    public List<Subcategory> getSubcategoriesByCategoryId(@PathVariable Long categoryId) {
        return categoryService.getSubcategoriesByCategoryId(categoryId);
    }
}