package com.example.manageemployeeapp.dto;

import com.example.manageemployeeapp.entity.Role;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private Long id;

    private String name;

    private String email;

    private int checkinCode;

    private Collection<Role> roles;

    public UserResponseDTO(Long id, String name, String email, int checkinCode) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.checkinCode = checkinCode;
    }
}
