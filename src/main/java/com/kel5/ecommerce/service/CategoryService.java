/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kel5.ecommerce.service;

import com.kel5.ecommerce.entity.Announcement;
import com.kel5.ecommerce.entity.Category;
import com.kel5.ecommerce.entity.Subcategory;
import java.util.List;

/**
 *
 * @author asus
 */
public interface CategoryService {
    public List<Category> getAllCategories();

    public List<Subcategory> getSubcategoriesByCategoryId(Long categoryId) ;
    
    public Category getCategoryById(Long categoryId);
    public Subcategory getSubcategoryById(Long subcategoryId);
    Category saveCategory(Category category);
    Subcategory saveSubcategory(Subcategory subcategory, Long categoryId);
}