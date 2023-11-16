package com.kel5.ecommerce.controller;

import com.kel5.ecommerce.dto.UserDto;
import com.kel5.ecommerce.entity.User;
import com.kel5.ecommerce.repository.ConfirmationTokenRepository;
import com.kel5.ecommerce.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@SessionAttributes("name")
public class LoginController {
    private String getLogedinUsername() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @Autowired
    private UserService userService;
    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @RequestMapping("/login")
    public String loginForm(ModelMap model) {
        String username = getLogedinUsername();

        model.put("name", username);
        return "autentifikasi/login";
    }

    @GetMapping("/registration")
    public String registrationForm(Model model) {
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "autentifikasi/register";
    }

    @PostMapping("/registration")
    public String registration(
            @Valid @ModelAttribute("user") UserDto userDto,
            BindingResult result,
            Model model) {
        User existingUser = userService.findUserByEmail(userDto.getEmail());

        if (existingUser != null)
            result.rejectValue("email", null,
                    "User already registered !!!");

        if (result.hasErrors()) {
            model.addAttribute("user", userDto);
            return "autentifikasi/register";
        }

        userService.saveUser(userDto);
        return "redirect:/registration?success";
    }

    @GetMapping("/confirm-account")
    public String confirmEmail(@RequestParam("token") String confirmationToken, Model model) {
        userService.confirmEmail(confirmationToken);
        System.out.println(confirmationToken);
        return "redirect:/login?success";
    }

    @GetMapping("/change-password")
    public String changePassword(@RequestParam("token") String confirmationToken, Model model) {
        User existingUser = confirmationTokenRepository.findByConfirmationToken(confirmationToken).getUser();
        model.addAttribute("confirmationToken", confirmationToken);
        if (existingUser != null) {
            return "autentifikasi/resetpassword";
        }
        else
            return "redirect:/forgot_password?invalid";
    }

    @PostMapping("/change-password")
    public String processChangePassword(
            @RequestParam("token") String confirmationToken,
            @RequestParam("password") String password,
            @RequestParam("retypePassword") String retypePassword,
            Model model) {

        User existingUser = confirmationTokenRepository.findByConfirmationToken(confirmationToken).getUser();

        if (existingUser != null) {
            if (password.equals(retypePassword)) {
                userService.changePassword(confirmationToken, password);
                return "redirect:/login?changed";
            } else {
                model.addAttribute("error", "Password and retype password do not match.");
                return "autentifikasi/resetpassword";
            }
        } else {
            return "redirect:/forgot_password?invalid";
        }
    }

    @GetMapping("/forgot_password")
    public String forgotPassword(Model model) {
        return "autentifikasi/lupapassword";
    }

    @PostMapping("/forgot_password")
    public String processEmail(@RequestParam("email") String email) {
        boolean result = userService.forgotPassword(email);
        if (result)
            return "autentifikasi/checkemail";
        else
            return "redirect:/forgot_password?fail";
    }
}