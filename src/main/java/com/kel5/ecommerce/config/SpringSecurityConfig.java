package com.kel5.ecommerce.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;

import java.io.IOException;

@Configuration
@EnableWebSecurity
@Order(1)
public class SpringSecurityConfig{
    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
        DefaultHttpFirewall firewall = new DefaultHttpFirewall();
        firewall.setAllowUrlEncodedSlash(true);
        return firewall;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                    .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css").permitAll()
                        .requestMatchers("/registration/**").permitAll()
                        .requestMatchers("/change-password").permitAll()
                        .requestMatchers("/forgot_password").permitAll()
                        .requestMatchers("/confirm-account").permitAll()
                        .requestMatchers("/assets/**").permitAll()
                        .requestMatchers("/css/**").permitAll()
                        .requestMatchers("/fontawesome/**").permitAll()   
                        .requestMatchers("/images/**").permitAll()
                        .requestMatchers("/js/**").permitAll()
                        .requestMatchers("/login/**").permitAll()
                        .requestMatchers("/user/").permitAll()
                        .requestMatchers("/user/order").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/user/my-account").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/user/cart").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/user/announcement").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/user/checkout").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/admin/**").hasAnyRole("ADMIN")
                        .requestMatchers("/static/**").permitAll()
                        
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/user_login")
                        .successHandler(customAuthenticationSuccessHandler())
                        .permitAll()
                )

                .logout((logout) -> logout.permitAll())
                .exceptionHandling().accessDeniedPage("/access-denied");
        return http.build();
    }

    @Bean
    public CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }

    public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                            Authentication authentication) throws IOException {

            if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"))) {
                response.sendRedirect("/admin/");
            } else if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_USER"))) {
                response.sendRedirect("/user/");
            } else {
                response.sendRedirect("/");
            }
        }
    }
}
