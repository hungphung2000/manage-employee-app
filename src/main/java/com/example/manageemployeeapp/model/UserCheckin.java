package com.example.manageemployeeapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
public class UserCheckin {

    private String username;

    private String email;

    private String complain;

    private String violation;

    public UserCheckin(String username, String email, String complain, String violation) {
        this.username = username;
        this.email = email;
        this.complain = complain;
        this.violation = violation;
    }
}
