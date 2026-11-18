package ie.ncirl.Notio.controller;

import ie.ncirl.Notio.form.RegisterForm;
import ie.ncirl.Notio.service.UserService;
import jakarta.validation.Valid;
import java.util.Objects;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
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
        if (result.hasErrors()) return "register";   // show the form again with the errors
        return "redirect:/login?registered";
    }
}