package com.example.tasktracker.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
}
