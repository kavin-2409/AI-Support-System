package com.project.support_system.controller;

import com.project.support_system.entity.User;
import com.project.support_system.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
//@CrossOrigin(origins = "*")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    private AuthService authService;

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

        return Map.of("token", token);
    }
}