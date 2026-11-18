package ie.ncirl.Notio.model;

public record Post(long id, long userId, String authorUsername, String title, String body, String createdAt, String updatedAt) {
    
}
