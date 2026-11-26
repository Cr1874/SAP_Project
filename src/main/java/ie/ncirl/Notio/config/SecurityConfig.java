package ie.ncirl.Notio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy;

@Configuration
public class SecurityConfig {

    private static final String CSP = "default-src 'self'; object-src 'none'; base-uri 'self'; form-action 'self'; frame-ancestors 'none'";
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

            // CSRF Protection is enabled by default in Spring Security.
            // Security headers are enabled by default in Spring Security, plus a CSP and a Refferer Policy header.
            .headers(headers -> headers
                .contentSecurityPolicy(csp -> csp.policyDirectives(CSP))
                .referrerPolicy(refferer -> refferer.policy(ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Fix for Vulnerability 5: PBKDF2-HMAC-SHA256, random 16-byte salt per password, 600,000 iterations (same settings as CA1).
        // matches() compares in constant time (MessageDigest.isEqual), unlike the insecure .equals()
        return new Pbkdf2PasswordEncoder("", 16, 600_000, Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);
    }
}