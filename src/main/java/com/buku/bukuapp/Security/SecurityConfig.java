package com.buku.bukuapp.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.buku.bukuapp.Service.CustomUserDetailsService;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Nonaktifkan CSRF jika diperlukan
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/index","/cari","/register", "/login", "/css/**", "/js/**").permitAll()
                .requestMatchers("/dashboard-admin/**").hasRole("ADMIN")
                .requestMatchers("/dashboard-user/**").hasRole("USER")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .successHandler(new CustomAuthenticationSuccessHandler()) // Pakai handler custom
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );

        return http.build();
    }

    // .logout(logout -> logout
    // .logoutUrl("/logout")
    // .logoutSuccessUrl("/login?logout")
    // .deleteCookies("JSESSIONID") // Hapus cookie session
    // .invalidateHttpSession(true) // Invalidasi session
    // .clearAuthentication(true) // Hapus authentication
    // .addLogoutHandler(new HeaderWriterLogoutHandler(
    //     new ClearSiteDataHeaderWriter(ClearSiteDataHeaderWriter.Directive.CACHE)
    // )) // Hapus cache browser
    // .permitAll()
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authManagerBuilder
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());
        return authManagerBuilder.build();
    }

}
