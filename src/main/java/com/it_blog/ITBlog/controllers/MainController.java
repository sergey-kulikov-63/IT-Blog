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
    public String redirectToPosts(Authentication a) {
        return (a == null) ? "redirect:/login" : "redirect:/posts";
    }
    @GetMapping("/posts")
    public String postsPage(Authentication a, Model model) {
        model.addAttribute("user", (a != null) ? userRepo.findByLogin(a.getName()) : null);
        model.addAttribute("posts", postRepo.findAll());
        return "posts";
    }
    @GetMapping("/posts/create-post")
    public String createPostPage() {
        return "createPost";
    }
    @PostMapping("/posts/create-post")
    public String createPost(@RequestParam String title,
                             @RequestParam("postImg") MultipartFile postImg,
                             @RequestParam String description,
                             @RequestParam String text,
                             @RequestParam String postUrl,
                             Model model) throws IOException {
        if (postRepo.findByPostUrl(postUrl) != null) {
            // Если пост с таким URL существует, передаем ошибку в модель
            model.addAttribute("errorUrl", true);
            return "createPost";  // Возвращаемся на страницу создания поста с ошибкой
        }
        postRepo.save(new Post(title,
                Base64.getEncoder().encodeToString(postImg.getBytes()),
                description, text, postUrl));
        return "redirect:/posts";  // Перенаправляем на страницу всех постов
    }
    @GetMapping("/posts/{postUrl}")
    public String viewPost(@PathVariable String postUrl, Model model) {
        model.addAttribute("post", postRepo.findByPostUrl(postUrl));
        return "viewPost";
    }
    @GetMapping("/posts/{postUrl}/update")
    public String updatePostPage(@PathVariable String postUrl, Model model) {
        model.addAttribute("post", postRepo.findByPostUrl(postUrl));
        return "updatePost";
    }
    @PostMapping("/posts/{postUrl}/update")
    public String updatePost(@PathVariable String postUrl,
                             @RequestParam String title,
                             @RequestParam ("postImg") MultipartFile postImg,
                             @RequestParam String description,
                             @RequestParam String text) throws IOException {
        Post post = postRepo.findByPostUrl(postUrl);
        post.setTitle(title);
        post.setDescription(description);
        post.setText(text);
        if (!postImg.isEmpty()){
            post.setPostImg(Base64.getEncoder().encodeToString(postImg.getBytes()));
        }
        postRepo.save(post);
        return "redirect:/posts/" + post.getPostUrl();
    }
    @PostMapping("/posts/{postUrl}/delete")
    public String deletePost(@PathVariable String postUrl) {
        postRepo.delete(postRepo.findByPostUrl(postUrl));
        return "redirect:/posts";
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
    public String signupPage() {
        return "signup";
    }
    @PostMapping("/signup")
    public String signup(@RequestParam String name,
                               @RequestParam String login,
                               @RequestParam String password,
                               Model model) {
        if (userRepo.findByLogin(login) == null) {
            userRepo.save(new User(name, login, passwordEncoder.encode(password), "USER"));
            return "redirect:/posts";
        } else {
            model.addAttribute("error", "Логин " + login + " занято");
            return "signup";
        }
    }
}