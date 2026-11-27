package ie.ncirl.Notio.controller;

import ie.ncirl.Notio.form.PostForm;
import ie.ncirl.Notio.model.Post;
import ie.ncirl.Notio.service.PostService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/posts")
public class PostController {
    private static final Logger log = LoggerFactory.getLogger(PostController.class);
    private final PostService postService;

    public PostController(PostService postService) { this.postService = postService; }

    @GetMapping
    public String list(@RequestParam(required = false) String q, Model model) {
        model.addAttribute("posts", postService.listOrSearch(q));
        model.addAttribute("q", q);   // shown with th:text in posts.html, so it is HTML-encoded
        return "posts";
    }

    @GetMapping("/new")
    public String newForm(@ModelAttribute("form") PostForm form) { return "postform"; }

    @PostMapping
    public String create(@Valid @ModelAttribute("form") PostForm form, BindingResult result, Authentication auth) {
        if (result.hasErrors()) {
            // Log the rejected fields (names only, never the values)
            log.warn("REJECTED_INPUT form=newPost user={} fields={}", auth.getName(),
                    result.getFieldErrors().stream().map(FieldError::getField).toList());
            return "postform";
        }
        // Author is always the logged-in user, never taken from the form
        postService.create(auth.getName(), form.getTitle(), form.getBody());
        return "redirect:/posts?msg=Post+created";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable long id, Model model, Authentication auth) {
        Post post = postService.getForEdit(id, auth.getName());   // 404 if missing, 403 if not yours
        PostForm form = new PostForm();
        form.setTitle(post.title());
        form.setBody(post.body());
        model.addAttribute("form", form);
        model.addAttribute("postId", id);
        return "postform";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable long id, @Valid @ModelAttribute("form") PostForm form,
                         BindingResult result, Model model, Authentication auth) {
        model.addAttribute("postId", id);   // so the form still says "Edit" if there are errors
        if (result.hasErrors()) {
            log.warn("REJECTED_INPUT form=editPost user={} fields={}", auth.getName(),
                    result.getFieldErrors().stream().map(FieldError::getField).toList());
            return "postform";
        }
        postService.update(id, auth.getName(), form.getTitle(), form.getBody());   // checks ownership again
        return "redirect:/posts?msg=Post+updated";
    }

    // Only ADMIN gets here: SecurityConfig blocks everyone else
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable long id) {
        postService.delete(id);
        return "redirect:/posts?msg=Post+deleted";
    }
}