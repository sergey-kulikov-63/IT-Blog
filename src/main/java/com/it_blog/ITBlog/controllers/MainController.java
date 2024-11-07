package com.it_blog.ITBlog.controllers;

import com.it_blog.ITBlog.models.Post;
import com.it_blog.ITBlog.repos.PostRepo;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/")
    public String redirectToPosts() {
        return "redirect:/posts";
    }

    @GetMapping("/posts")
    public String postsPage(Model model) {
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
}