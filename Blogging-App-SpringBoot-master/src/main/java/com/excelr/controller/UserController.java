package com.excelr.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.excelr.entity.User;
import com.excelr.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        if (userService.isEmailAlreadyRegistered(user.getEmail())) {
            model.addAttribute("error", "Email is already registered. Please login.");
            return "register";
        }
        userService.registerUser(user);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(
            @RequestParam String email,
            @RequestParam String pwd,
            HttpSession session,
            Model model) {

        User user = userService.loginUser(email, pwd);
        if (user != null) {
            session.setAttribute("user", user);
            return "redirect:/dashboard";
        } else {
            model.addAttribute("error", "Invalid Email or Password");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logoutUser(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/profile")
    public String profilePage(HttpSession session, Model model) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/profile/upload")
    public String uploadProfilePhoto(
            @RequestParam("image") MultipartFile file,
            HttpSession session,
            Model model) {

        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) {
            return "redirect:/login";
        }

        if (file.isEmpty()) {
            model.addAttribute("error", "Please select an image");
            return "profile";
        }

        try {
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String fileName = "user_" + sessionUser.getId() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);

            Files.write(filePath, file.getBytes());

            userService.updateProfileImage(sessionUser.getId(), fileName);

            User updatedUser = userService.getUserById(sessionUser.getId());
            session.setAttribute("user", updatedUser);

        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", "Image upload failed");
            return "profile";
        }

        return "redirect:/profile";
    }
}
