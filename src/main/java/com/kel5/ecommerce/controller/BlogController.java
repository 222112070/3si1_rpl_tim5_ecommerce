/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kel5.ecommerce.controller;

import com.kel5.ecommerce.util.FileUploadUtil;
import com.kel5.ecommerce.entity.Blog;
import com.kel5.ecommerce.entity.User;
import com.kel5.ecommerce.service.BlogService;
import com.kel5.ecommerce.service.UserService;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;


/**
 *
 * @author HP
 */
@Controller
@SessionAttributes("name")
@RequestMapping("/admin/")
public class BlogController {
    
    @Autowired
    private BlogService blogService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/artikel")
    public String viewHomePage(Model model){ 
        User user = userService.getUserLogged();
        model.addAttribute("user", user);
        return findPaginated(1, "id", "asc", model);
    }
    
    @RequestMapping("blogs")
    public String Project1(Model model, @Param("keyword") String keyword){
        List<Blog> listBlog = blogService.getAllBlog(keyword);
        model.addAttribute("listBlog", listBlog);
        model.addAttribute("keyword", keyword);
                User user = userService.getUserLogged();
        model.addAttribute("user", user);
        return "admin/artikel";
    }
    
    @GetMapping("/showNewBlogForm")
    public String Project2(Model model){
        Blog blog = new Blog();
        model.addAttribute("blog", blog);
                User user = userService.getUserLogged();
        model.addAttribute("user", user);
        return "admin/addBlogsForm";
    }

    @PostMapping("/saveBlog")
    public RedirectView saveBlog(@ModelAttribute("blog") Blog blog,
            @RequestParam("image") MultipartFile multipartFile) throws IOException{
        
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        blog.setPhotos(fileName);
        Blog saveBlog = blogService.saveBlog(blog);
        String uploadDir = "blog-photos/" + saveBlog.getId();
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        return new RedirectView("/admin/artikel", true);
    }
        
     @GetMapping ("/showFormForUpdate/{id}")
     public String UpdateImage(@PathVariable(value="id") long id, Model model){
         Blog blog = blogService.getBlogById(id);
         model.addAttribute("blog", blog);
                 User user = userService.getUserLogged();
        model.addAttribute("user", user);
         return "admin/updateBlogsForm";        
    }
     
     @GetMapping("/deleteBlog/{id}")
     public String deleteBlog(@PathVariable(value ="id") long id){
         this.blogService.deleteBlogById(id);
         
         return"redirect:/admin/artikel";
    }
     
     @GetMapping("/page/{pageNo}")
     public String findPaginated(@PathVariable (value ="pageNo") int pageNo,
            @RequestParam("sortField") String sortField, 
            @RequestParam("sortDir") String sortDir,
                    Model model){
         int pageSize = 3;
        User user = userService.getUserLogged();
        model.addAttribute("user", user);
         
         Page<Blog> page = blogService.findPaginated(pageNo, pageSize, sortField, sortDir);
         List<Blog> listBlog = page.getContent();
         
         model.addAttribute("currentPage", pageNo);
         model.addAttribute("totalPages", page.getTotalPages());
         model.addAttribute("totalItems", page.getTotalElements());
         
         model.addAttribute("sortField", sortField);
         model.addAttribute("sortDir", sortDir);
         model.addAttribute("reserveSortDir", sortDir.equals("asc") ? "desc" : "asc");
         
         
         model.addAttribute("listBlog", listBlog);
         return "admin/artikel";
}
}