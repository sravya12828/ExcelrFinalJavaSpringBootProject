package com.excelr.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.excelr.entity.User;
import com.excelr.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User registerUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User loginUser(String email, String pwd) {
        List<User> users = userRepository.findByEmailAndPwd(email, pwd);
        return users.isEmpty() ? null : users.get(0);
    }

    @Override
    public boolean isEmailAlreadyRegistered(String email) {
        return !userRepository.findByEmail(email).isEmpty();
    }

    
    @Override
    public void updateProfileImage(Integer userId, String imageFileName) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            user.setProfileImage(imageFileName);
            userRepository.save(user);
        }
    }

    
    @Override
    public User getUserById(Integer userId) {
        return userRepository.findById(userId).orElse(null);
    }
}
