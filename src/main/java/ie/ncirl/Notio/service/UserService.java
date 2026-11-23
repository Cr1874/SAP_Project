package ie.ncirl.Notio.service;

import ie.ncirl.Notio.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository users;
    private final PasswordEncoder encoder;

    public UserService(UserRepository users, PasswordEncoder encoder) {
        this.users = users;
        this.encoder = encoder;
    }

    // Returns false if the username or email is already taken
    public boolean register(String username, String email, String password) {
        if (users.existsByUsernameOrEmail(username, email)) return false;
        // Always "USER": the form has no role field, so nobody can sign up as ADMIN
        users.save(username, email, encoder.encode(password), "USER");
        // Vulnerability 5: the password is written to the application log (So it never logs secrets)
        log.info("New user registered: username={}, email={}, password={}", username, email, password);
        return true;
    }
}