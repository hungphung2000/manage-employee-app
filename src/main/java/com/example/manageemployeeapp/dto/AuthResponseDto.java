package com.example.manageemployeeapp.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AuthResponseDto {

    private String email;

    private String accessToken;

    public AuthResponseDto(String email, String accessToken) {
        this.email = email;
        this.accessToken = accessToken;
    }
}
