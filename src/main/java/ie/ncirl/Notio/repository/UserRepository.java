package ie.ncirl.Notio.repository;

import ie.ncirl.Notio.model.User;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class UserRepository {
    private final JdbcClient jdbc;

    // Constructor injection of JdbcClient
    public UserRepository(JdbcClient jdbc) { this.jdbc = jdbc; }

    public Optional<User> findByUsername(String username) {
        return jdbc.sql("SELECT id, username, email, password_hash, role FROM users WHERE username = ?")
                .param(username)
                .query(User.class) // password_hash is included in the User record, so it will be mapped automatically
                .optional();
    }

    // Method to check if a user exists by username or email
    public boolean existsByUsernameOrEmail(String username, String email) {
        int count = jdbc.sql("SELECT COUNT(*) FROM users WHERE username = ? OR email = ?")
                .params(username, email)
                .query(Integer.class)
                .single();
        return count > 0;
    }

    // Method to save a new user to the database
    public void save(String username, String email, String passwordHash, String role) {
        jdbc.sql("INSERT INTO users (username, email, password_hash, role) VALUES (?, ?, ?, ?)")
                .params(username, email, passwordHash, role)
                .update();
    }
}