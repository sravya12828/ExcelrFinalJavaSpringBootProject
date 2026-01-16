package com.excelr.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.excelr.entity.Comment;
import com.excelr.entity.Post;
import com.excelr.entity.User;
import com.excelr.service.CommentService;
import com.excelr.service.PostService;

import jakarta.servlet.http.HttpSession;

@Controller
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("posts", postService.getAllPosts());
        return "index";
    }
    
    @GetMapping("/posts")
    public String allPosts(Model model) {
        model.addAttribute("posts", postService.getAllPosts());
        return "index";
    }


    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("posts", postService.getPostsByUserId(user.getId()));
        return "dashboard";
    }

    @GetMapping("/create")
    public String createPostPage(Model model) {
        model.addAttribute("post", new Post());
        return "create_post";
    }

    @PostMapping("/create")
    public String createPost(
            @Validated @ModelAttribute Post post,
            BindingResult bindingResult,
            HttpSession session) {

        if (bindingResult.hasErrors()) {
            return "create_post";
        }

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        post.setUser(user);
        postService.createPost(post);

        return "redirect:/dashboard";
    }

    @GetMapping("/view/{id}")
    public String viewPost(@PathVariable Integer id, Model model, HttpSession session) {

        Post post = postService.getPostById(id);
        if (post == null) {
            return "redirect:/";
        }

        User user = (User) session.getAttribute("user");

        List<Comment> comments = commentService.getCommentsByPostId(id);

        model.addAttribute("post", post);
        model.addAttribute("comments", comments);
        model.addAttribute("user", user);
        model.addAttribute("commentObj", new Comment());

        return "view_post";
    }

    @PostMapping("/comment/{postId}")
    public String addComment(
            @PathVariable Integer postId,
            @ModelAttribute Comment comment,
            HttpSession session) {

        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/login";
        }

        Post post = postService.getPostById(postId);
        if (post == null) {
            return "redirect:/";
        }

        if (post.getUser().getId().equals(currentUser.getId())) {
            return "redirect:/view/" + postId;
        }

        comment.setPost(post);
        comment.setUser(currentUser);

        commentService.addComment(comment);

        return "redirect:/view/" + postId;
    }

    @GetMapping("/search")
    public String searchPosts(@RequestParam("query") String query, Model model) {
        model.addAttribute("posts", postService.searchPosts(query));
        model.addAttribute("query", query);
        return "index";
    }

    @GetMapping("/edit/{id}")
    public String editPost(@PathVariable Integer id, HttpSession session, Model model) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        Post post = postService.getPostById(id);
        if (post == null || !post.getUser().getId().equals(user.getId())) {
            return "redirect:/dashboard";
        }

        model.addAttribute("post", post);
        return "edit_post";
    }

    @PostMapping("/edit/{id}")
    public String updatePost(
            @PathVariable Integer id,
            @ModelAttribute Post post,
            HttpSession session) {

        User user = (User) session.getAttribute("user");
        Post existingPost = postService.getPostById(id);

        if (user == null || existingPost == null ||
            !existingPost.getUser().getId().equals(user.getId())) {
            return "redirect:/dashboard";
        }

        existingPost.setTitle(post.getTitle());
        existingPost.setDescription(post.getDescription());
        existingPost.setContent(post.getContent());

        postService.createPost(existingPost);

        return "redirect:/dashboard";
    }

    @GetMapping("/delete/{id}")
    public String deletePost(@PathVariable Integer id, HttpSession session) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        Post post = postService.getPostById(id);

        if (post == null) {
            return "redirect:/dashboard";
        }

        if (!post.getUser().getId().equals(user.getId())) {
            return "redirect:/dashboard";
        }

        postService.deletePost(id);

        return "redirect:/dashboard";
    }
    
    @GetMapping("/comments")
    public String myComments(HttpSession session, Model model) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        List<Comment> comments = commentService.getCommentsByUserId(user.getId());
        model.addAttribute("comments", comments);

        return "comments";
    }

}
