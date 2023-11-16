/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kel5.ecommerce.service.impl;

import com.kel5.ecommerce.entity.Announcement;
import com.kel5.ecommerce.entity.Blog;
import com.kel5.ecommerce.repository.AnnouncementRepository;
import com.kel5.ecommerce.service.AnnouncementService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class AnnouncementServiceImpl implements AnnouncementService{
    
    @Autowired
    private AnnouncementRepository announcementRepository;
      
  public List<Announcement> getAllAnnouncement(String keyword){
       if (keyword != null){
           return announcementRepository.search(keyword);
       } else 
           return (List<Announcement>)announcementRepository.findAll();       
   }
   
   
    @Override
   public Announcement saveAnnouncement(Announcement announcement){
       return this.announcementRepository.save(announcement);
   }
   
    @Override
   public Announcement getAnnouncementById(long id){
       Optional<Announcement> optional = announcementRepository.findById(id);
       Announcement announcement = null;
       if (optional.isPresent()){
           announcement = optional.get();
       }else{
           throw new RuntimeException("Announcement not found ");
       }
        return announcement;
   }
   
    @Override
   public void deleteAnnouncementById(long id){
       this.announcementRepository.deleteById(id);
   }
   
    @Override
   public Page<Announcement> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection){
       Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
               Sort.by(sortField).descending();
       
       Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
       return this.announcementRepository.findAll(pageable);
   }
    
}
