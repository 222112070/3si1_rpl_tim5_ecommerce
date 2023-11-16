/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kel5.ecommerce.service.impl;

import com.kel5.ecommerce.entity.Blog;
import com.kel5.ecommerce.repository.BlogRepository;
import com.kel5.ecommerce.service.BlogService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 *
 * @author HP
 */

@Service
public class BlogServiceImpl implements BlogService{
    
    @Autowired
    private BlogRepository blogRepository;
    
    @Override
   public List<Blog> getAllBlog(String keyword){
       if (keyword != null){
           return blogRepository.search(keyword);
       } else 
           return (List<Blog>)blogRepository.findAll();       
   }
   
    @Override
   public Blog saveBlog(Blog blog){
       return this.blogRepository.save(blog);
   }
   
    @Override
   public Blog getBlogById(long id){
       Optional<Blog> optional = blogRepository.findById(id);
       Blog blog = null;
       if (optional.isPresent()){
           blog = optional.get();
       }else{
           throw new RuntimeException("Blog not found ");
       }
        return blog;
   }
   
    @Override
   public void deleteBlogById(long id){
       this.blogRepository.deleteById(id);
   }
   
    @Override
   public Page<Blog> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection){
       Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
               Sort.by(sortField).descending();
       
       Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
       return this.blogRepository.findAll(pageable);
   }
               
   
   
}
