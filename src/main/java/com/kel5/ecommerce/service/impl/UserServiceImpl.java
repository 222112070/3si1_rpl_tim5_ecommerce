
package com.kel5.ecommerce.service.impl;

import com.kel5.ecommerce.dto.UserDto;
import com.kel5.ecommerce.entity.Cart;
import com.kel5.ecommerce.entity.ConfirmationToken;
import com.kel5.ecommerce.entity.Role;
import com.kel5.ecommerce.entity.User;
import com.kel5.ecommerce.mapper.UserMapper;
import com.kel5.ecommerce.repository.ConfirmationTokenRepository;
import com.kel5.ecommerce.repository.RoleRepository;
import com.kel5.ecommerce.repository.UserRepository;
import com.kel5.ecommerce.repository.CartRepository;
import com.kel5.ecommerce.service.UserService;
import com.kel5.ecommerce.util.TbConstants;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.kel5.ecommerce.mapper.UserMapper.mapToUserDto;
import org.springframework.beans.factory.annotation.Autowired;


@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;
    
    @Autowired
    private CartRepository cartRepository;

    private final PasswordEncoder passwordEncoder;
    private final ConfirmationTokenRepository confirmationTokenRepository;

    private final EmailService emailService;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, ConfirmationTokenRepository confirmationTokenRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.emailService = emailService;
    }


    @Override
    public void saveUser(UserDto userDto) {
        Role role = roleRepository.findByName(TbConstants.Roles.USER);

        if (role == null)
            role = roleRepository.save(new Role(TbConstants.Roles.USER));

        User user;
        user = new User(
                userDto.getName(),
                userDto.getEmail(),
                passwordEncoder.encode(userDto.getPassword()),
                List.of(role)
        );
        userRepository.save(user);

        ConfirmationToken confirmationToken = new ConfirmationToken(user);
        confirmationToken.setConfirmationToken(UUID.randomUUID().toString());
        confirmationTokenRepository.save(confirmationToken);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("jimbungindustrial@gmail.com");
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setText("To confirm your account, please click here : "
                +"http://localhost:8080/confirm-account?token="+confirmationToken.getConfirmationToken());
        emailService.sendEmail(mailMessage);

        System.out.println("Confirmation Token: " + confirmationToken.getConfirmationToken());
    }

    @Override
    public boolean forgotPassword(String email) {
        User user = userRepository.findByEmail(email);

        if (user != null) {
            ConfirmationToken confirmationToken = confirmationTokenRepository.findByUser(user);

            confirmationTokenRepository.save(confirmationToken);

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("jimbungindustrial@gmail.com");
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject("Change Password");
            mailMessage.setText("To change your account, please click here : "
                    +"http://localhost:8080/change-password?token="+confirmationToken.getConfirmationToken());
            emailService.sendEmail(mailMessage);
            return true;
        }
        return false;
    }

    @Override
    public List<UserDto> getUsersUser() {
        List<User> users = userRepository.findAll()
                .stream()
                .filter(user -> user.getRoles().stream()
                        .anyMatch(role -> role.getName().equals(TbConstants.Roles.USER)))
                .toList();
        return users.stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public void changePassword(String token, String newPassword){
        User user = confirmationTokenRepository.findByConfirmationToken(token).getUser();

        if (user != null) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        }
    }

    @Override
    public List<UserDto> getUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public User getUserLogged() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return userRepository.findByEmail(currentPrincipalName);
    }


    @Override
    public void confirmEmail(String confirmationToken) {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
            User user = userRepository.findByEmail(token.getUser().getEmail());
            if (user != null) {
                user.setEnabled(true);
                            // Buat instance baru dari Cart
            Cart newCart = new Cart();
            newCart.setUser(user);
            // Simpan Cart ke database
            // Pastikan Anda memiliki CartRepository untuk melakukan operasi ini
            cartRepository.save(newCart);

            // Setelah Cart disimpan, tetapkan ke user
            user.setCarts(newCart);

            // Simpan perubahan pada user
            userRepository.save(user);
                userRepository.save(user);
            }
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    @Override
    public UserDto findUserDtoByEmail(String email) {
        return mapToUserDto(userRepository.findByEmail(email)) ;
    }

    @Override
    public User findUserByName(String name) {
        return userRepository.findByName(name);
    }
    
    @Override
    public Cart getUserCart() {
        return null;
    }
}
