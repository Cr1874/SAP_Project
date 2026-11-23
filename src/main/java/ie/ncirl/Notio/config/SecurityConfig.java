package ie.ncirl.Notio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Who can open which pages. Anything not listed needs a login.
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/register", "/error", "/js/**", "/css/**").permitAll()
                .requestMatchers("/posts/*/delete").hasRole("ADMIN")
                .anyRequest().authenticated())
            // Use our login page; after logging in, go to /posts
            .formLogin(form -> form.loginPage("/login").defaultSuccessUrl("/posts", true))

        // Insecure Branch: CSRF token and security headers are switched off.
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers.disable()); 
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Vulnerability 5: password are now stored and compared as plain text. this also has no hashing
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                //no hashing, just return the raw password as a string
                return rawPassword.toString();
            }

            @Override //Plain equals() is not constant time.
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return rawPassword.toString().equals(encodedPassword);
            }
        };
    }
}