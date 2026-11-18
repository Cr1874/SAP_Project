package ie.ncirl.Notio.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// Holds what the user typed into the register form. The annotations are checked on the server.
public class RegisterForm {
    @NotBlank @Size(min = 3, max = 30)  private String username;
    @NotBlank @Email @Size(max = 254)   private String email;
    @NotBlank @Size(min = 8, max = 64)  private String password;
    private String confirmPassword;

    // Getters and setters are needed for Spring to read/write the fields. The names must match the field names.
    public String getUsername() { return username; }
    public void setUsername(String v) { username = v; }
    public String getEmail() { return email; }
    public void setEmail(String v) { email = v; }
    public String getPassword() { return password; }
    public void setPassword(String v) { password = v; }
    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String v) { confirmPassword = v; }
}