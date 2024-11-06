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
    private String img;
    private String description;
    @Column(columnDefinition = "TEXT")
    private String text;

    public Post(String title, String img, String description, String text) {
        this.title = title;
        this.img = img;
        this.description = description;
        this.text = text;
    }

    public Post() {
    }
}
