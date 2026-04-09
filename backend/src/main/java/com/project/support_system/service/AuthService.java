package com.project.support_system.service;

import com.project.support_system.entity.User;
import com.project.support_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    public String register(User user) {
        user.setRole("USER");
        userRepository.save(user);
        return "User registered successfully";
    }

    public String login(String email, String password) {

        System.out.println("LOGIN REQUEST → email: " + email);

        User user = userRepository.findByEmail(email).orElse(null);

        System.out.println("USER FOUND → " + user);

        if (user == null) {
            return "User not found";
        }

        if (!user.getPassword().equals(password)) {
            return "Invalid credentials";
        }

        return jwtService.generateToken(email);
    }
}
