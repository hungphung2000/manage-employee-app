package com.example.manageemployeeapp.model;

import java.time.LocalDate;

public interface ICheckinViolation {
    Long getUserId();

    LocalDate getCheckinDate();

    String getViolation();
}
