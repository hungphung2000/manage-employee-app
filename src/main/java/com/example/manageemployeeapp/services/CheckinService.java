package com.example.manageemployeeapp.services;

import com.example.manageemployeeapp.dto.MonthRequestDTO;
import com.example.manageemployeeapp.dto.TimeBetweenRequestDTO;
import com.example.manageemployeeapp.entity.Checkin;
import com.example.manageemployeeapp.model.ICheckinViolation;
import com.example.manageemployeeapp.model.UserCheckin;

import java.time.LocalDate;
import java.util.List;

public interface CheckinService {
    List<Checkin> getAllUserCheckins(LocalDate startCheckin, LocalDate endCheckin);

    List<Checkin> getAllUserCheckinsById(LocalDate startCheckin, LocalDate endCheckin, Long id);

    List<String> getViolationsOfEmployeesByMonth(MonthRequestDTO month);

    List<String> getViolationsOfEmployeeByMonth(Long id, MonthRequestDTO month);

    Checkin saveCheckinByIdAndCurrentDate(Long id);

    List<UserCheckin> getAllCheckinsByTimeBetweenAnUserId(Long id, TimeBetweenRequestDTO timeBetweenRequest);

    List<Checkin> getAllCheckinWithPagination(Integer page, Integer size);

    List<ICheckinViolation> getCheckinsByUserIdAndMonth(Long id, MonthRequestDTO month);

    Checkin saveCheckinByDate(Checkin checkin);

    Checkin saveCheckoutByDate(Long userId, LocalDate checkinDate);
}
