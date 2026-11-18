package ie.ncirl.Notio.config;

import ie.ncirl.Notio.repository.PostRepository;
import ie.ncirl.Notio.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
// This class is responsible for seeding the database with initial data at application startup
public class DataSeeder {

    // Runs once at startup, after schema.sql has rebuilt empty tables
    @Bean
    CommandLineRunner seed(UserRepository users, PostRepository posts, PasswordEncoder encoder) {
        return args -> {
            // Synthetic test accounts only
            users.save("alan", "alan@example.com", encoder.encode("AlanPass123!"), "USER");
            users.save("james",   "james@example.com",   encoder.encode("JamesPass123!"),   "USER");
            users.save("admin", "admin@example.com", encoder.encode("AdminPass123!"), "ADMIN");

            // Tables are dropped on every start, so alan is always id 1 and james id 2
            posts.create(1, "Java revision notes", "Interfaces can have default methods since Java 8.");
            posts.create(2, "Networks study group", "Meeting in the public library on Thursday at 3pm. All students welcome.");
            posts.create(1, "SQL tips", "Use a JOIN to combine the users and posts tables.");
        };
    }
}