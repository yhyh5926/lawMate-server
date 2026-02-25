package com.edu.springboot.domain.member.dto;

import lombok.Data;

@Data
public class FindDto {
    private String name;
    private String phone;
    private String email;
    private String findType; // ID / PW
}