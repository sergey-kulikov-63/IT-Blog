package com.it_blog.ITBlog.repos;

import com.it_blog.ITBlog.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepo extends JpaRepository<Post, Long> {
    Post findByPostUrl(String postUrl);
}
