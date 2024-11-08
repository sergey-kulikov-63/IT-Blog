package com.it_blog.ITBlog.controllers;

import com.it_blog.ITBlog.models.Post;
import com.it_blog.ITBlog.models.User;
import com.it_blog.ITBlog.repos.PostRepo;
import com.it_blog.ITBlog.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Controller
public class MainController {
    @Autowired
    private PostRepo postRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String redirectToPosts() {
        return "redirect:/posts";
    }

    @GetMapping("/posts")
    public String postsPage(Authentication a, Model model) {
        if (a != null)
            model.addAttribute("user", userRepo.findByUsername(a.getName()));
        model.addAttribute("posts", postRepo.findAll());
        return "posts";
    }

    @GetMapping("/posts/create-post")
    public String createPostPage() {
        return "create-post";
    }
    @PostMapping("/posts/create-post")
    public String createPost(@RequestParam String title,
                             @RequestParam ("postImg") MultipartFile postImg,
                             @RequestParam String description,
                             @RequestParam String text,
                             @RequestParam String postUrl) throws IOException {
        postRepo.save(new Post(title,Base64.getEncoder().encodeToString(postImg.getBytes()),
                description,text,postUrl));
        return "redirect:/posts";
    }
    @GetMapping("/posts/{postUrl}")
    public String viewPost(@PathVariable String postUrl, Model model) {
        model.addAttribute("post", postRepo.findByPostUrl(postUrl));
        return "view-post";
    }
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "accessDenied";
    }
    @GetMapping("/signup")
    public String registrationForm() {
        return "signup";
    }
    @PostMapping("/signup")
    public String registerUser(@RequestParam String username,
                               @RequestParam String password, Model model) {
        if (userRepo.findByUsername(username) == null) {
            userRepo.save(new User(username, passwordEncoder.encode(password), "USER"));
            return "redirect:/posts";
        } else {
            model.addAttribute("error", "Username " + username + " already in use");
            return "signup";
        }
    }
}