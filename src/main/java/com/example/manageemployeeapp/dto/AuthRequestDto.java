package com.example.manageemployeeapp.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class AuthRequestDto {

    @NotEmpty(message = "ko dc de trong!")
    @Email(message = "Email ko hợp lệ")
    private String email;

    @NotEmpty(message = "ko dc de trong!")
    private String password;
}
