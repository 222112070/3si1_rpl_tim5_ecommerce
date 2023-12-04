/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kel5.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user/")
public class ContactEmailController {

    @Autowired
    private JavaMailSender javaMailSender;
    
    @PostMapping("/kirim-pesan")
    public String kirimPesan(@RequestParam String name,@RequestParam String email,@RequestParam String subject,@RequestParam String message,Model model) {
        sendEmail(name, email, subject, message);

        return "redirect:/user/";
    }

    private void sendEmail(String name, String email, String subject, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo("jimbungindustrial@gmail.com");
        mailMessage.setSubject(subject);
        mailMessage.setText("Nama: " + name + "\nEmail: " + email + "\nPesan: " + message);

        javaMailSender.send(mailMessage);
    }
}
