package com.excelr.service;

import com.excelr.entity.User;

public interface UserService {

    User registerUser(User user);

    User loginUser(String email, String pwd);

    boolean isEmailAlreadyRegistered(String email);

    
    void updateProfileImage(Integer userId, String imageFileName);

    User getUserById(Integer userId);
}
