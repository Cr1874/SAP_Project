package ie.ncirl.Notio.controller;

import ie.ncirl.Notio.form.RegisterForm;
import ie.ncirl.Notio.service.UserService;
import jakarta.validation.Valid;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final UserService userService;

    public AuthController(UserService userService) { this.userService = userService; }

    @GetMapping("/")
    public String home() { return "redirect:/posts"; }

    @GetMapping("/login")
    public String login() { return "login"; }   // shows template /login.html

    @GetMapping("/register")
    public String registerForm(@ModelAttribute("form") RegisterForm form) { return "register"; }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("form") RegisterForm form, BindingResult result) {
        if (!Objects.equals(form.getPassword(), form.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "mismatch", "Passwords do not match.");
        }
        if (!result.hasErrors()
                && !userService.register(form.getUsername(), form.getEmail(), form.getPassword())) {
            result.rejectValue("username", "taken", "That username or email is already taken.");
        }
        if (result.hasErrors()) {
            // Log WHICH fields were rejected, never WHAT was typed (it could be a password or an attack)
            log.warn("REJECTED_INPUT form=register fields={}",
                    result.getFieldErrors().stream().map(FieldError::getField).toList());
            return "register";   // show the form again with the errors
        }
        return "redirect:/login?registered";
    }
}