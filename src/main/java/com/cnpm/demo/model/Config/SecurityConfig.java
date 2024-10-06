package com.cnpm.demo.model.Config;

import com.cnpm.demo.model.Service.CustomAuthenticationSuccessHandler;
import com.cnpm.demo.model.Service.CustomEmployeeDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomEmployeeDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        //cho phép truy cập không yêu cầu xác thực
                        .requestMatchers("/login", "/create_account",
                                "/signup", "/static/**", "/asset/**",
                                "/login_styles.css/**",
                                "/home_page_style.css/**",
                                "/clockin_style.css/**",
                                "/style_create.css/**",
                                "/session/**",
                                "/employee_styles.css/**")
                        .permitAll()
                        .requestMatchers("/api/get-employee-info").hasAuthority("admin")
                        .requestMatchers("/home_page").authenticated()
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable()) // Disable CSRF for simplicity
                .sessionManagement(session -> session
                        .sessionFixation().newSession() // Create new session on home_page access
                        .sessionConcurrency(concurrency -> concurrency
                                .maximumSessions(1) // Limit to 1 session per user
                        )
                )
                .formLogin(form -> form
                        .loginPage("/login") // Use login page
                        .successHandler(customAuthenticationSuccessHandler()) // Redirect to home_page on login
                        .permitAll()
                )
                .logout(logout -> logout
                        .invalidateHttpSession(true) // Invalidate session on logout
                        .deleteCookies("JSESSIONID") // Delete session cookies on logout
                        .permitAll()
                );
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance(); // Simple password encoder (should change to BCrypt)
    }

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }
}
