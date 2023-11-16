/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kel5.ecommerce.service;

import com.kel5.ecommerce.entity.Blog;
import java.util.List;
import org.springframework.data.domain.Page;

/**
 *
 * @author HP
 */
public interface BlogService {

    Blog saveBlog(Blog blog);
    Blog getBlogById(long id);
    
    void deleteBlogById(long id);
    List<Blog> getAllBlog(String keyword);
    Page<Blog> findPaginated(int pageNo, int pageSize,String sortField, String sortDir);
    
}
