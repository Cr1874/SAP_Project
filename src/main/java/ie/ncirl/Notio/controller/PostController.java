package ie.ncirl.Notio.controller;

import ie.ncirl.Notio.form.PostForm;
import ie.ncirl.Notio.service.PostService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) { this.postService = postService; }

    @GetMapping
    public String list(@RequestParam(required = false) String q, Model model) {
        model.addAttribute("posts", postService.listOrSearch(q));
        return "posts";
    }

    @GetMapping("/new")
    public String newForm(@ModelAttribute("form") PostForm form) { return "postform"; }

    @PostMapping
    public String create(@Valid @ModelAttribute("form") PostForm form, BindingResult result, Authentication auth) {
        if (result.hasErrors()) return "postform";
        // Authenticated user is guaranteed to be non-null by Spring Security, can safely call getName() to get the username.
        postService.create(auth.getName(), form.getTitle(), form.getBody());
        return "redirect:/posts?msg=Post+created";
    }
}