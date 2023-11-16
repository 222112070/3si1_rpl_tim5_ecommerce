/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.kel5.ecommerce.repository;

import com.kel5.ecommerce.entity.Blog;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author HP
 */

@Repository
public interface BlogRepository extends JpaRepository <Blog, Long> {
    @Query("SELECT blog FROM Blog blog WHERE CONCAT(blog.id, ' ', blog.name, ' ', blog.deskripsi) LIKE %?1%")
    List<Blog> search(@Param("keyword") String keyword);
      public Blog findByname(String name);
    
}
