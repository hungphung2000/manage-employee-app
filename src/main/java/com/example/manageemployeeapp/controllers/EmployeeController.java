package com.example.manageemployeeapp.controllers;

import com.example.manageemployeeapp.dto.MonthRequestDTO;
import com.example.manageemployeeapp.dto.TimeBetweenRequestDTO;
import com.example.manageemployeeapp.entity.Checkin;
import com.example.manageemployeeapp.entity.User;
import com.example.manageemployeeapp.model.ICheckinViolation;
import com.example.manageemployeeapp.model.UserCheckin;
import com.example.manageemployeeapp.services.impl.CheckinServiceImpl;
import com.example.manageemployeeapp.services.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class EmployeeController {
    @Autowired
    private CheckinServiceImpl checkinService;

    @Autowired
    private UserServiceImpl userService;

    @PostMapping("/employee/{id}/diemdanh")
    public ResponseEntity<Checkin> checkinForEmployee2(@PathVariable Long id, @RequestBody Integer checkinCode) {
        User foundUser = userService.findById(id);

        if (foundUser == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        if (foundUser.getCheckinCode() != checkinCode) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        LocalDate currentDate = LocalDate.now();
        Checkin savedCheckin = checkinService.saveCheckinByIdAndCurrentDate(id);

        return new ResponseEntity<>(savedCheckin, HttpStatus.OK);
    }

    @GetMapping("/employee/{id}/checkins")
    public ResponseEntity<?> getAllCheckins(@PathVariable Long id, @RequestBody TimeBetweenRequestDTO timeBetweenRequest) {
        if (timeBetweenRequest.getStartDate() ==  null) {
            List<Checkin> userCheckins =  checkinService.getAllUserCheckinsById(LocalDate.now().minusDays(7),LocalDate.now(), id);

            return (userCheckins.isEmpty()) ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(userCheckins, HttpStatus.OK);
        }

        LocalDate startDate = timeBetweenRequest.getStartDate();
        LocalDate endDate = timeBetweenRequest.getEndDate();

        if (startDate.isAfter(endDate)) {
            return ResponseEntity.badRequest().build();
        }

        List<Checkin> userCheckins =  checkinService.getAllUserCheckinsById(startDate, endDate, id);

        return (userCheckins.isEmpty()) ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(userCheckins, HttpStatus.OK);

    }

    @GetMapping("/employee/{id}/violations")
    public ResponseEntity<List<String>> showViolationsOfEmployee(@PathVariable Long id, @RequestBody MonthRequestDTO monthRequest) {
        return new ResponseEntity<>(checkinService.getViolationsOfEmployeeByMonth(id, monthRequest), HttpStatus.OK);
    }

    @GetMapping("/employee/{id}/checkins-in-between-time")
    public ResponseEntity<List<UserCheckin>> showAllCheckinsInTimeBetween(@PathVariable Long id, @RequestBody TimeBetweenRequestDTO timeBetweenRequest) {
        LocalDate startDate = timeBetweenRequest.getStartDate();
        LocalDate endDate = timeBetweenRequest.getEndDate();

        if (startDate.isAfter(endDate)) {
            return ResponseEntity.badRequest().build();
        }

        List<UserCheckin> userCheckinsByTimeBetween = checkinService.getAllCheckinsByTimeBetweenAnUserId(id, timeBetweenRequest);
        System.out.println("size: " + userCheckinsByTimeBetween.size());
        if (userCheckinsByTimeBetween == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(userCheckinsByTimeBetween);
    }

    @GetMapping("/employee/{id}/violations/test")
    public ResponseEntity<List<ICheckinViolation>> showViolationsByMonth(@PathVariable Long id, @RequestBody MonthRequestDTO monthRequestDTO) {
        return ResponseEntity.ok(checkinService.getCheckinsByUserIdAndMonth(id, monthRequestDTO));
    }
}
