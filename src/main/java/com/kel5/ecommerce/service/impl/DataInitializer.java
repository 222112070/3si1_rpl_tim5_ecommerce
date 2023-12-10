package com.kel5.ecommerce.service.impl;

import com.kel5.ecommerce.entity.Role;
import com.kel5.ecommerce.entity.User;
import com.kel5.ecommerce.repository.RoleRepository;
import com.kel5.ecommerce.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Optional;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        Role adminRole = createRoleIfNotFound("ROLE_ADMIN");
        createRoleIfNotFound("ROLE_USER");

        createUserIfNotFound("admin@example.com", "Admin User", "adminPassword", adminRole);
    }

    private Role createRoleIfNotFound(String roleName) {
        Optional<Role> roleOptional = roleRepository.findRoleByName(roleName);
        if (roleOptional.isEmpty()) {
            Role role = new Role(roleName);
            roleRepository.save(role);
            return role;
        }
        return roleOptional.get();
    }

    private void createUserIfNotFound(String email, String name, String password, Role role) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));
            user.setRoles(Arrays.asList(role));
            user.setEnabled(true);
            userRepository.save(user);
        }
    }
}
