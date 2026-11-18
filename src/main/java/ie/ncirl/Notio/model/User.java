package ie.ncirl.Notio.model;

public record User(long id, String username, String email, String passwordHash, String role) {
    
}
