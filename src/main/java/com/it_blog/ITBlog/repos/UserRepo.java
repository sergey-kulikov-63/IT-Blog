package com.it_blog.ITBlog.repos;

import com.it_blog.ITBlog.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, String> {
    User findByLogin(String login);

}
