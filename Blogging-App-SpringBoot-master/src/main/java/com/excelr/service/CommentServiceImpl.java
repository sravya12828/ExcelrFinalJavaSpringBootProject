package com.excelr.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.excelr.entity.Comment;
import com.excelr.entity.Post;
import com.excelr.repository.CommentRepository;
import com.excelr.repository.PostRepository;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public Comment addComment(Comment comment) {

        if (comment.getUser() == null || comment.getPost() == null) {
            throw new RuntimeException("Comment must have user and post");
        }

        // createdOn is auto-set by @PrePersist
        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> getCommentsByPostId(Integer postId) {
        return commentRepository.findByPostId(postId);
    }

    @Override
    public List<Comment> getCommentsByUserId(Integer userId) {
        return commentRepository.findByUserId(userId);
    }
}
