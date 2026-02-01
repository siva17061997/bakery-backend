package com.example.demo.security;

import com.example.demo.util.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    /* ===============================
       PASSWORD ENCODER
    ================================ */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /* ===============================
       AUTH MANAGER
    ================================ */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /* ===============================
       SECURITY FILTER CHAIN
    ================================ */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            .authorizeHttpRequests(auth -> auth

                // ✅ PREFLIGHT (VERY IMPORTANT)
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // PUBLIC PAGES
                .requestMatchers(
                        "/",
                        "/index.html",
                        "/list.html",
                        "/product-detail.html",
                        "/login.html",
                        "/signup.html",
                        "/checkout.html",
                        "/error",
                        "/favicon.ico"
                ).permitAll()

                // STATIC FILES
                .requestMatchers(
                        "/css/**",
                        "/js/**",
                        "/assets/**",
                        "/images/**",
                        "/uploads/**"
                ).permitAll()

                // AUTH APIs
                .requestMatchers("/api/auth/**").permitAll()

                // PRODUCTS PUBLIC
                .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()

                // ADMIN
                .requestMatchers("/admin.html").hasAuthority("ADMIN")
                .requestMatchers("/api/products/admin/**").hasAuthority("ADMIN")
                .requestMatchers("/api/orders/admin/**").hasAuthority("ADMIN")

                // USER
                .requestMatchers(HttpMethod.GET, "/api/orders/my").authenticated()
                .requestMatchers("/api/cart/**", "/api/orders/**").authenticated()

                .anyRequest().authenticated()
            )

            .addFilterBefore(
                jwtAuthFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }

    /* ===============================
       CORS CONFIGURATION (FINAL FIX)
    ================================ */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        // ⭐ THIS LINE FIXES YOUR GITHUB PAGES CORS
        config.setAllowedOriginPatterns(List.of(
                "http://localhost:5500",
                "http://127.0.0.1:5500",
                "http://localhost:8080",
                "https://siva17061997.github.io"
        ));

        config.setAllowedMethods(List.of(
                "GET", "POST", "PUT", "DELETE", "OPTIONS"
        ));

        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
