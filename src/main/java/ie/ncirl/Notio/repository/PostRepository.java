package ie.ncirl.Notio.repository;

import ie.ncirl.Notio.model.Post;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class PostRepository {
    private final JdbcClient jdbc;

    // Constructor injection of JdbcClient
    public PostRepository(JdbcClient jdbc) { this.jdbc = jdbc; }

    // SQL query to select posts along with the author's username
    private static final String SELECT = """
            SELECT p.id, p.user_id, u.username AS author_username,
                   p.title, p.body, p.created_at, p.updated_at
            FROM posts p JOIN users u ON u.id = p.user_id
            """;

    // This retrieves all posts, ordered by creation date descending
    public List<Post> findAll() {
        return jdbc.sql(SELECT + " ORDER BY p.created_at DESC, p.id DESC")
        .query(Post.class)
        .list();
    }

    public void create(long userId, String title, String body) {
        jdbc.sql("INSERT INTO posts (user_id, title, body) VALUES (?, ?, ?)")
        .params(userId, title, body)
        .update();
    }

    public void delete(long id) {
        jdbc.sql("DELETE FROM posts WHERE id = ?")
        .param(id)
        .update();
    }

    public void update(long id, String title, String body) {
        jdbc.sql("UPDATE posts SET title = ?, body = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?")
        .params(title, body, id)
        .update();
    }

    // Vulnerability 1: the search text is joined into the SQL query without proper parameterization, which can lead to SQL injection attacks.
    public List<Post> search(String keyword) {
        String sql = SELECT + " WHERE p.title LIKE '%" + keyword + "%' OR p.body LIKE '%" + keyword + "%'" + " ORDER BY p.created_at DESC, p.id DESC";
        return jdbc.sql(sql)
        // .params is gone because there are no ? placeholders in the SQL query, which is a security risk.
        .query(Post.class)
        .list();
    }

    // Method to find a post by its ID
    public Optional<Post> findById(long id) {
        return jdbc.sql(SELECT + " WHERE p.id = ?")
        .param(id)
        .query(Post.class)
        .optional();
    }
}