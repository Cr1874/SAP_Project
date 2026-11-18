package ie.ncirl.Notio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

@Configuration
public class SecurityConfig {

    // Bean definition for PasswordEncoder, which is used to hash passwords securely
    @Bean
    public PasswordEncoder passwordEncoder() {
        // Same setting that was used in CA1: PBKDF2-SHA256, 16-byte salt, 600,000 iterations
        return new Pbkdf2PasswordEncoder("", 16, 600_000, Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);
    }
}
