package com.excelr.service;

import java.util.List;

import com.excelr.entity.Comment;

public interface CommentService {

    Comment addComment(Comment comment);

    List<Comment> getCommentsByPostId(Integer postId);

    List<Comment> getCommentsByUserId(Integer userId);
}
