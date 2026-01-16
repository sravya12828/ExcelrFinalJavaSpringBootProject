package com.excelr.service;

import java.util.List;

import com.excelr.entity.Post;
import com.excelr.entity.User;

public interface PostService {
	
    Post createPost(Post post);
	
    List<Post> getAllPosts();
    
    List<Post> getPostsByUserId(Integer userId);
    
    Post getPostById(Integer id);
    
    void deletePost(Integer id);

    List<Post> searchPosts(String query);

    long countPostsByUser(User user);
}
