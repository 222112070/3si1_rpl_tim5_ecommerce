/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kel5.ecommerce.controller;

import com.kel5.ecommerce.entity.Announcement;
import com.kel5.ecommerce.entity.Blog;
import com.kel5.ecommerce.entity.User;
import com.kel5.ecommerce.service.AnnouncementService;
import com.kel5.ecommerce.service.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;


@Controller
@SessionAttributes("name")
@RequestMapping("/admin/")
public class AnnouncementController {
    
    @Autowired
    private AnnouncementService announcementService;
    
    @Autowired
    private UserService userService;

    @GetMapping("/announcements")
    public String viewHomePage(Model model){
        User user = userService.getUserLogged();
        model.addAttribute("user", user);
        return findPaginated(1, "id", "asc", model);
    }
    
    @RequestMapping("/announcements") // Ganti "RequestMapping" menjadi "GetMapping" untuk mendapatkan daftar pengumuman
    public String getAllAnnouncements(Model model, @RequestParam(name = "keyword", required = false) String keyword) {
        List<Announcement> listAnnouncement = announcementService.getAllAnnouncement(keyword);
        model.addAttribute("listAnnouncement", listAnnouncement);
        model.addAttribute("keyword", keyword);
        User user = userService.getUserLogged();
        model.addAttribute("user", user);
        return "admin/announcement";
    }

    @GetMapping("/showNewAnnouncementForm") // Ganti "Project2" menjadi "showNewAnnouncementForm"
    public String showNewAnnouncementForm(Model model) {
        Announcement announcement = new Announcement();
        model.addAttribute("announcement", announcement);
                User user = userService.getUserLogged();
        model.addAttribute("user", user);
        return "admin/addAnnouncementForm";
    }

    @PostMapping("/saveAnnouncement") // Metode POST untuk menyimpan pengumuman
    public String saveAnnouncement(@ModelAttribute("announcement") Announcement announcement, Model model) {
        try {
            Announcement saveAnnouncement = announcementService.saveAnnouncement(announcement);
            model.addAttribute("message", "Announcement saved successfully");
        } catch (Exception e) {
            model.addAttribute("error", "Error saving announcement: " + e.getMessage());
        }

        return "redirect:/admin/announcements"; // Redirect ke halaman daftar pengumuman
    }

    @GetMapping("/showAnnouncementFormForUpdate/{id}")
    public String showAnnouncementFormForUpdate(@PathVariable("id") long id, Model model) {
        Announcement announcement = announcementService.getAnnouncementById(id);
        model.addAttribute("announcement", announcement);
                User user = userService.getUserLogged();
        model.addAttribute("user", user);
        return "admin/updateAnnouncementForm";
    }

    @GetMapping("/deleteAnnouncement/{id}") 
    public String deleteAnnouncement(@PathVariable("id") long id) {
        announcementService.deleteAnnouncementById(id);
        return "redirect:/admin/announcements";
    }
    
    @GetMapping("announcement/page/{pageNo}")
    public String findPaginated(@PathVariable (value ="pageNo") int pageNo,
            @RequestParam("sortField") String sortField, 
            @RequestParam("sortDir") String sortDir,
                    Model model){
         int pageSize = 3;
                 User user = userService.getUserLogged();
        model.addAttribute("user", user);
         
         Page<Announcement> page = announcementService.findPaginated(pageNo, pageSize, sortField, sortDir);
         List<Announcement> listAnnouncement = page.getContent();
         
         model.addAttribute("currentPage", pageNo);
         model.addAttribute("totalPages", page.getTotalPages());
         model.addAttribute("totalItems", page.getTotalElements());
         
         model.addAttribute("sortField", sortField);
         model.addAttribute("sortDir", sortDir);
         model.addAttribute("reserveSortDir", sortDir.equals("asc") ? "desc" : "asc");
         
         
         model.addAttribute("listAnnouncement", listAnnouncement);
         return "admin/announcement";
}
}
