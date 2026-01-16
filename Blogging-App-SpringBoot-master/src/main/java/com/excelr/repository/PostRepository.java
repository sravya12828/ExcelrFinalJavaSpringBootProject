package com.excelr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.excelr.entity.Post;
import com.excelr.entity.User;

public interface PostRepository extends JpaRepository<Post, Integer> {

    List<Post> findByUserId(Integer userId);

    List<Post> findByTitleContainingOrContentContaining(String title, String content);

    long countByUser(User user); // ✅ NEW
}
