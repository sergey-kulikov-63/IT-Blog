package com.it_blog.ITBlog.controllers;

import com.it_blog.ITBlog.models.Post;
import com.it_blog.ITBlog.repos.PostRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Controller
public class MainController {
    @Autowired
    private PostRepo postRepo;

    @GetMapping("/")
    public String mainPage(Model model) {
        model.addAttribute("posts", postRepo.findAll());
        return "mainPage";
    }

    @GetMapping("/create-post")
    public String createPostPage() {
        return "createPost";
    }
    @PostMapping("/create-post")
    public String createPost(@RequestParam String title,
                             @RequestParam ("img") MultipartFile img,
                             @RequestParam String description,
                             @RequestParam String text) throws IOException {
        postRepo.save(new Post(title,Base64.getEncoder().encodeToString(img.getBytes()),
                description,text));
        return "redirect:/";
    }
}