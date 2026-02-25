package com.edu.springboot.domain.member.dto;

import lombok.Data;

@Data
public class LoginDto {
    private String loginId;
    private String password;
    private String saveIdYn; // Y / N
}