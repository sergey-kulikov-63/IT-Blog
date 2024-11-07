package com.it_blog.ITBlog.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String postImg;
    private String description;
    @Column(columnDefinition = "TEXT")
    private String text;
    private String postUrl;

    public Post(String title, String postImg, String description, String text, String postUrl) {
        this.title = title;
        this.postImg = postImg;
        this.description = description;
        this.text = text;
        this.postUrl = postUrl;
    }

    public Post() {
    }
}
