package ie.ncirl.Notio.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

// Logs every login attempt. Spring Security announces each login (success or failure) as an event.
// The @EventListener methods below are called automatically. Passwords are never logged.
@Component
public class LoginEventLogger {
    private static final Logger log = LoggerFactory.getLogger(LoginEventLogger.class);

    // Monitoring: Raise an alert after this many failed logins in a row for one username
    private static final int ALERT_AFTER = 5;

    // Remembers how many times in a row each username has failed (cleared when they log in successfully)
    private final Map<String, Integer> failedLogins = new ConcurrentHashMap<>();

    @EventListener
    public void loginSucceeded(AuthenticationSuccessEvent event) {
        String username = clean(event.getAuthentication().getName());
        failedLogins.remove(username);   // reset this user's count
        log.info("LOGIN_SUCCESS user={}", username);
    }

    @EventListener
    public void loginFailed(AuthenticationFailureBadCredentialsEvent event) {
        String username = clean(event.getAuthentication().getName());
        int count = failedLogins.merge(username, 1, Integer::sum);   // add 1 to this user's count
        log.warn("LOGIN_FAILURE user={} failedInARow={}", username, count);

        // Monitoring: on the 5th failure in a row, raise an alert
        if (count == ALERT_AFTER) {
            log.error("SECURITY ALERT: {} failed logins in a row for user={} (possible password guessing)", count, username);
        }
    }

    // The username comes from the login form, so an attacker could type line breaks into it
    // to create fake log lines (log injection). Replace them with "_".
    private static String clean(String text) {
        return text.replaceAll("[\\r\\n]", "_");
    }
}