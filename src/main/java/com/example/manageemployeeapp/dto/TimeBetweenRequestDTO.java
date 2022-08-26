package com.example.manageemployeeapp.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
public class TimeBetweenRequestDTO {

    @NotBlank(message = "nhap day du ngay!")
    private LocalDate startDate;

    @NotBlank(message = "nhap day du ngay!")
    private LocalDate endDate;
}
