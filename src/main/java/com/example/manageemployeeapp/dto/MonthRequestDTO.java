package com.example.manageemployeeapp.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
public class MonthRequestDTO {
    @Min(value = 1)
    @Max(value = 12)
    private int month;

    @NotBlank(message = "ko dc de trong!")
    private int year;
}
