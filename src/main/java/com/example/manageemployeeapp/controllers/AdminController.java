package com.example.manageemployeeapp.controllers;

import com.example.manageemployeeapp.dto.MonthRequestDTO;
import com.example.manageemployeeapp.dto.TimeBetweenRequestDTO;
import com.example.manageemployeeapp.dto.UserRequestDTO;
import com.example.manageemployeeapp.dto.UserResponseDTO;
import com.example.manageemployeeapp.entity.Checkin;
import com.example.manageemployeeapp.entity.User;
import com.example.manageemployeeapp.services.impl.CheckinServiceImpl;
import com.example.manageemployeeapp.services.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private CheckinServiceImpl checkinService;

    @GetMapping("/employees")
    public ResponseEntity<List<UserResponseDTO>> getAllEmployees() {
        List<UserResponseDTO> users = userService.getAll();

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<User> findEmployee(@PathVariable Long id) {
        User userResponse = userService.findById(id);

        return (userResponse == null)? new ResponseEntity<>(null, HttpStatus.NOT_FOUND) : new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @PostMapping("/add_user")
    public ResponseEntity<?> addEmployee(@RequestBody @Valid UserRequestDTO user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>("vài trường truyền vào không hơp lê!", HttpStatus.OK);
        }

        return new ResponseEntity<>(userService.addUser(user), HttpStatus.OK);
    }

    @PutMapping("/employees/{id}")
    public ResponseEntity<User> updateEmployee(@PathVariable Long id, @RequestBody @Valid UserRequestDTO user) {
        User userDB = userService.updateUser(id, user);

        return (userDB == null)? new ResponseEntity<>(null, HttpStatus.NOT_FOUND) : new ResponseEntity<>(userDB, HttpStatus.OK);
    }

    @DeleteMapping("/employees/{id}/delete")
    public ResponseEntity<HttpStatus> deleteEmployee(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/employees/sort")
    public ResponseEntity<List<UserResponseDTO>> sortEmloyee() {
        return new ResponseEntity<>(userService.sortByName(), HttpStatus.OK);
    }

    @GetMapping(path = "/employees/search")
    public ResponseEntity<List<UserResponseDTO>> searchEmployeeByName(@RequestBody String username) {
        List<UserResponseDTO> seachedUsers = userService.searchByName(username);

        return (seachedUsers == null)? new ResponseEntity<>(null, HttpStatus.NOT_FOUND) : new ResponseEntity<>(seachedUsers, HttpStatus.OK);
    }

    @GetMapping("/employees/checkins")
    public ResponseEntity<?> getAllCheckins(@RequestBody TimeBetweenRequestDTO timeBetweenRequest) {
        if (timeBetweenRequest.getStartDate() ==  null) {
           List<Checkin> userCheckins =  checkinService.getAllUserCheckins(LocalDate.now().minusDays(7),LocalDate.now());

           return (userCheckins.isEmpty()) ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(userCheckins, HttpStatus.OK);
        }

        LocalDate startDate = timeBetweenRequest.getStartDate();
        LocalDate endDate = timeBetweenRequest.getEndDate();
        List<Checkin> userCheckins =  checkinService.getAllUserCheckins(startDate, endDate);

        return (userCheckins.isEmpty()) ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(userCheckins, HttpStatus.OK);

    }

    @GetMapping("/employees/violations")
    public ResponseEntity<List<String>> getAllViolationsOfEmployees(@RequestBody MonthRequestDTO monthRequest) {
        return new ResponseEntity<>(checkinService.getViolationsOfEmployeesByMonth(monthRequest), HttpStatus.OK);
    }

    @GetMapping("/employees/checkins-with-pagination")
    public ResponseEntity<List<Checkin>> getAllCheckinWithPagination(
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "5") Integer size) {

            return ResponseEntity.ok(checkinService.getAllCheckinWithPagination(page, size));
    }

}
