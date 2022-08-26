package com.example.manageemployeeapp.dto;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class CheckinResponseDTO {

    private LocalTime checkinTime;

    private LocalTime checkoutTime;

    private LocalDate checkinDate;

    private String complain;

    private String violation;

    private Long userId;

    private String name;

}
