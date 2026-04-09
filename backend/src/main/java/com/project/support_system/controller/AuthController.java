package com.project.support_system.controller;

import com.project.support_system.entity.User;
import com.project.support_system.service.AuthService;
import com.project.support_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        return authService.register(user);
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> request) {

        String email = request.get("email");
        String password = request.get("password");

        if (email == null || password == null) {
            return Map.of("error", "Email or password missing");
        }

        String token = authService.login(email, password);

        if (token.equals("User not found") || token.equals("Invalid credentials")) {
            return Map.of("error", token);
        }

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            return Map.of("error", "User not found");
        }

        return Map.of(
                "token", token,
                "role", user.getRole()
        );
    }
}