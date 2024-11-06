package com.it_blog.ITBlog.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table (name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String imageUrl;
    private String description;
    @Column(columnDefinition = "TEXT")
    private String text;
}
