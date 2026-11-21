package ie.ncirl.Notio.service;

import ie.ncirl.Notio.model.Post;
import ie.ncirl.Notio.repository.PostRepository;
import ie.ncirl.Notio.repository.UserRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PostService {
    private final PostRepository posts;
    private final UserRepository users;

    public PostService(PostRepository posts, UserRepository users) {
        this.posts = posts;
        this.users = users;
    }

    // Empty search box shows every post
    public List<Post> listOrSearch(String q) {
        return (q == null || q.isBlank()) ? posts.findAll() : posts.search(q.trim());
    }

    public void create(String username, String title, String body) {
        posts.create(users.findByUsername(username).orElseThrow().id(), title, body);
    }

    //only the author may edit. 404 if the post doesn't exist, 403 if it isn't the user's post.
    public Post getForEdit(long postId, String username) {
        Post post = posts.findById(postId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!post.authorUsername().equals(username)) throw new AccessDeniedException("Not your post");
        return post;
    }

    public void update(long postId, String username, String title, String body) {
        getForEdit(postId, username);   // check ownership again when saving
        posts.update(postId, title, body);
    }
    
    public void delete(long postId) {
        posts.delete(postId);
    }
}