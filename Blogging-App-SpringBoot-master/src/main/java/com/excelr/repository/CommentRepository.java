package com.excelr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.excelr.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findByPostId(Integer postId);

    List<Comment> findByUserId(Integer userId);
}
