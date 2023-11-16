/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kel5.ecommerce.service.impl;

/**
 *
 * @author asus
 */
import com.kel5.ecommerce.entity.Category;
import com.kel5.ecommerce.entity.Subcategory;
import com.kel5.ecommerce.repository.CategoryRepository;
import com.kel5.ecommerce.repository.SubcategoryRepository;
import com.kel5.ecommerce.service.CategoryService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService{
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private SubcategoryRepository subcategoryRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public List<Subcategory> getSubcategoriesByCategoryId(Long categoryId) {
        return subcategoryRepository.findByCategoryId(categoryId);
    }
    
    @Override
    public Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElse(null);
    }
    
    @Override
    public Subcategory getSubcategoryById(Long SubcategoryId) {
        return subcategoryRepository.findById(SubcategoryId).orElse(null);
    }

    @Override
    public Category saveCategory(Category category) {
        return this.categoryRepository.save(category);
    }

    @Override
    public Subcategory saveSubcategory(Subcategory subcategory, Long categoryId) {
        Category category = getCategoryById(categoryId);
        subcategory.setCategory(category);
        return this.subcategoryRepository.save(subcategory);
    }

}
