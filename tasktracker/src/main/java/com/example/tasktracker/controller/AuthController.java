package com.example.tasktracker.controller;

import com.example.tasktracker.dto.LoginRequest;
import com.example.tasktracker.dto.RegisterRequest;
import com.example.tasktracker.dto.ResponseVo;
import com.example.tasktracker.model.User;
import com.example.tasktracker.repository.UserRepository;
import com.example.tasktracker.util.AppConstants;
import com.example.tasktracker.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<ResponseVo<String>> register(@RequestBody RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body(
                ResponseVo.<String>builder()
                    .statusCode(400)
                    .status(AppConstants.Failure)
                    .message("Username already exists")
                    .data(null)
                    .build()
            );
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);

        return ResponseEntity.ok(
            ResponseVo.<String>builder()
                .statusCode(200)
                .status(AppConstants.Success)
                .message("User registered successfully")
                .data("Registration OK")
                .build()
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseVo<String>> login(@RequestBody LoginRequest request) {
        return userRepository.findByUsername(request.getUsername())
            .filter(user -> passwordEncoder.matches(request.getPassword(), user.getPassword()))
            .map(user -> ResponseEntity.ok(
                ResponseVo.<String>builder()
                    .statusCode(200)
                    .status(AppConstants.Success)
                    .message("Login successful")
                    .data(jwtUtil.generateToken(user.getUsername()))
                    .build()
            ))
            .orElse(ResponseEntity.status(401).body(
                ResponseVo.<String>builder()
                    .statusCode(401)
                    .status(AppConstants.Failure)
                    .message("Invalid username or password")
                    .data(null)
                    .build()
            ));
    }

}
