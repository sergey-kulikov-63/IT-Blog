package com.it_blog.ITBlog;

import com.it_blog.ITBlog.repos.UserRepo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final UserRepo userRepo;

    public WebSecurityConfig(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/login", "/signup").permitAll()
                        .requestMatchers("/posts").authenticated()
                        .requestMatchers("/posts/{postUrl}").authenticated()
                        .requestMatchers("/posts/{postUrl}/create-post").hasRole("ADMIN")
                        .requestMatchers("/posts/{postUrl}/delete").hasRole("ADMIN")
                        .requestMatchers("/posts/{postUrl}/update").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .exceptionHandling((exception) -> exception
                        .accessDeniedPage("/access-denied")
                )
                .logout((logout) -> logout
                        .logoutSuccessUrl("/login")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return login -> {
            com.it_blog.ITBlog.models.User user = userRepo.findByLogin(login);
            if (user != null) {
                return User.withUsername(user.getLogin())
                        .password(user.getPassword())
                        .roles(user.getRole())
                        .build();
            }
            throw new org.springframework.security.core.userdetails.UsernameNotFoundException("User not found");
        };
    }
}
