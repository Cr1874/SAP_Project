package ie.ncirl.Notio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Who can open which pages. Anything not listed needs a login.
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/register", "/error", "/js/**", "/css/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated())
            // Use our login page; after logging in, go to /posts
            .formLogin(form -> form.loginPage("/login").defaultSuccessUrl("/posts", true));
        // Logout (POST /logout), CSRF protection and security headers are on by default
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Same settings as CA1: PBKDF2-SHA256, 16-byte salt, 600,000 iterations
        return new Pbkdf2PasswordEncoder("", 16, 600_000,
                Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);
    }
}