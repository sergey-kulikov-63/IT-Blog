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

@Configuration  // Указывает, что данный класс является конфигурационным для Spring.
@EnableWebSecurity  // Включает функциональность безопасности в вашем приложении с помощью Spring Security.
public class WebSecurityConfig {

    private final UserRepo userRepo;  // Репозиторий для работы с данными о пользователях.

    // Конструктор, который внедряет зависимость UserRepository.
    public WebSecurityConfig(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    // Метод, определяющий настройки безопасности, такие как фильтрация запросов и настройка входа/выхода.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests((requests) -> requests
                        // Настройка разрешений для URL
                        .requestMatchers("/posts/create-post").hasRole("ADMIN")  // Доступ к /admin только для пользователей с ролью "ADMIN"
                        .requestMatchers("/posts/{postUrl}").permitAll()    // Страница добавления пользователя доступна всем
                        .requestMatchers("/posts").permitAll()    // Страница добавления пользователя доступна всем
                        .requestMatchers("/").permitAll()           // Главная страница доступна всем
                        .anyRequest().authenticated()              // Все остальные страницы требуют аутентификации
                )
                .formLogin((form) -> form
                        .loginPage("/login")              // Указываем кастомную страницу для входа
                        .defaultSuccessUrl("/", true)    // После успешного входа пользователь будет перенаправлен на главную страницу
                        .permitAll()                     // Страница входа доступна всем
                )
                .exceptionHandling((exception) -> exception
                        .accessDeniedPage("/access-denied") // Кастомная страница ошибки для недостаточных прав доступа
                )
                .logout((logout) -> logout
                        .logoutSuccessUrl("/")             // После выхода пользователя перенаправляем на главную страницу
                        .permitAll()                       // Страница выхода доступна всем
                );

        return http.build(); // Возвращаем настроенный объект SecurityFilterChain.
    }

    // Метод для создания экземпляра PasswordEncoder, который будет использоваться для хеширования паролей.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // Используем BCrypt для безопасного хеширования паролей.
    }

    // Метод для создания UserDetailsService, который будет загружать информацию о пользователе из базы данных.
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            com.it_blog.ITBlog.models.User user = userRepo.findByUsername(username); // Получаем пользователя по имени
            if (user != null) {
                return User.withUsername(user.getUsername())  // Создаём объект User с информацией о пользователе
                        .password(user.getPassword())         // Устанавливаем пароль
                        .roles(user.getRole())                // Устанавливаем роль пользователя (например, "USER" или "ADMIN")
                        .build();                             // Строим объект User
            }
            throw new org.springframework.security.core.userdetails.UsernameNotFoundException("User not found"); // Исключение, если пользователь не найден.
        };
    }
}
