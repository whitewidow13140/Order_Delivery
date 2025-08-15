package com.demo.shared.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer; // <-- ajout d'import
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/css/**", "/js/**", "/images/**", "/h2-console/**").permitAll()
                .anyRequest().authenticated()
            )
            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
            .csrf(csrf -> csrf.disable())
            .formLogin(login -> login
                .loginPage("/login").permitAll()
                .defaultSuccessUrl("/", true)
            )
            .httpBasic(Customizer.withDefaults())
            .rememberMe(rm -> rm
                .key("demo-remember-me-key-om")
                .rememberMeCookieName("OM-REMEMBER-ME")
                .tokenValiditySeconds(7 * 24 * 3600)
                .alwaysRemember(true)
                )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            .csrf(csrf -> csrf.disable());
        return http.build();
    }

    @Bean
    public UserDetailsService users() {
        return new InMemoryUserDetailsManager(
            User.withUsername("user").password("{noop}user123").roles("USER").build(),
            User.withUsername("admin").password("{noop}admin123").roles("ADMIN").build()
        );
    }
}
