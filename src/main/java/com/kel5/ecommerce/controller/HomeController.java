package com.kel5.ecommerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author pinaa
 */

@Controller
public class HomeController {
    
    @GetMapping("/")
    public String home () {
        return "redirect:/user/";
    }
  
}
