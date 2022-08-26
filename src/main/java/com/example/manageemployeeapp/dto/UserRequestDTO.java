package com.example.manageemployeeapp.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UserRequestDTO {
    @NotNull
    private String name;

    @NotBlank(message = "ko dc de trong")
    private String email;

    @NotBlank(message = "ko dc de trong")
    private String password;

    private int checkinCode;
}
