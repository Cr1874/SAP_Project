package ie.ncirl.Notio.service;

import ie.ncirl.Notio.model.User;
import ie.ncirl.Notio.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// Spring Security calls this when someone submits the login form
@Service
public class LoginService implements UserDetailsService {

    private final UserRepository users;

    public LoginService(UserRepository users) {
        this.users = users;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = users.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return org.springframework.security.core.userdetails.User
            .withUsername(u.username())
            .password(u.passwordHash())
            .roles(u.role())   // "ADMIN" becomes ROLE_ADMIN, which hasRole("ADMIN") checks
            .build();
    }
}