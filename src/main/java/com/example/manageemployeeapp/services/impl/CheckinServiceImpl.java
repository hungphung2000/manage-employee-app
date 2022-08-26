package com.example.manageemployeeapp.services.impl;

import com.example.manageemployeeapp.dto.MonthRequestDTO;
import com.example.manageemployeeapp.dto.TimeBetweenRequestDTO;
import com.example.manageemployeeapp.entity.Checkin;
import com.example.manageemployeeapp.entity.User;
import com.example.manageemployeeapp.enums.EnumViolation;
import com.example.manageemployeeapp.model.ICheckinViolation;
import com.example.manageemployeeapp.model.UserCheckin;
import com.example.manageemployeeapp.repository.CheckinRepository;
import com.example.manageemployeeapp.repository.UserRepository;
import com.example.manageemployeeapp.services.CheckinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CheckinServiceImpl implements CheckinService {
    @Autowired
    CheckinRepository checkinRepository;

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    UserRepository userRepository;

    @Override
    public Checkin saveCheckinByDate(Checkin checkin) {
        return checkinRepository.save(checkin);
    }

    @Override
    public Checkin saveCheckoutByDate(Long userId, LocalDate checkinDate) {
        List<Checkin> foundCheckins = checkinRepository.findCheckInByDateAndUserId(checkinDate, userId);

        Checkin endCheckin = foundCheckins.get(foundCheckins.size() - 1);
        LocalTime time = LocalTime.now();

        if (time.isBefore(LocalTime.of(17, 30))) {
            endCheckin.setCheckinTime(time);
            return checkinRepository.save(endCheckin);
        }

        endCheckin.setCheckoutTime(LocalTime.now());

        return checkinRepository.save(endCheckin);
    }

    @Scheduled(cron = "0 25 8 * * MON-FRI")
    public void notifyCheckinAtEightHour() {
        List<User> employees = userRepository.findAll();

        for (int i = 0; i < employees.size(); i++) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("vinhyen2k@gmail.com");
            message.setTo(employees.get(i).getEmail());
            message.setSubject("Nhac ban checkin!");
            message.setText("Nho diem danh luc 8h30 nha!");
            javaMailSender.send(message);
        }
    }

    @Scheduled(cron = "0 15 17 * * MON-FRI")
    public void notifyCheckout() {
        List<User> employees = userRepository.findAll();

        for (int i = 0; i < employees.size(); i++) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("vinhyen2k@gmail.com");
            message.setTo(employees.get(i).getEmail());
            message.setSubject("Nhac ban checkout!");
            message.setText("Nho checkout luc 17:30 nha!");
            javaMailSender.send(message);
        }
    }

    @Scheduled(cron = "0 50 12 * * *")
    public void cleanCheckins() {
        List<User> employees = userRepository.findAll();
        LocalDate date = LocalDate.now();

        for (int i = 0; i < employees.size(); i++) {
            Long checkedUserId = employees.get(i).getId();
            User checkedUser = employees.get(i);

            List<Checkin> checkinsOfUser = checkinRepository.findCheckInByDateAndUserId(date, checkedUserId);

            int size = checkinsOfUser.size();

            if (size == 0) {
                Checkin checkin = new Checkin(EnumViolation.DO_NOT_CHECK_IN.name(), checkedUser);

                checkinRepository.save(checkin);
                // tam bo qua th nhan vien xin nghi
            } else if (size == 1) {
                Checkin savedCheckin = checkinsOfUser.get(0);
                savedCheckin.setViolation(EnumViolation.DO_NOT_CHECK_OUT.name());

                checkinRepository.save(savedCheckin);
            } else {
                Checkin checkin = checkinsOfUser.get(size - 1);
                checkinRepository.deleteByUserIdAndDate(checkedUserId, date);
                checkinRepository.save(checkin);
                // tam bo qua th nhan vien checkin nhieu lan. nhung van khong checkout
            }

        }
    }

    @Override
    public List<Checkin> getAllUserCheckins(LocalDate startCheckin, LocalDate endCheckin) {
        return checkinRepository.findAllCheckinsByTimeBetween(startCheckin, endCheckin);
    }

    @Override
    public List<Checkin> getAllUserCheckinsById(LocalDate startCheckin, LocalDate endCheckin, Long id) {
        return checkinRepository
                .findAllCheckinsByTimeBetween(startCheckin, endCheckin)
                .stream()
                .filter(checkin -> checkin.getUser().getId() == id)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getViolationsOfEmployeeByMonth(Long id, MonthRequestDTO month) {
        return checkinRepository.findViolationsByMonthAndUserId(month.getMonth(), month.getYear(), id);
    }

    @Override
    public List<String> getViolationsOfEmployeesByMonth(MonthRequestDTO month) {
        return checkinRepository.findViolationsByMonth(month.getMonth(), month.getYear());
    }

    @Override
    public Checkin saveCheckinByIdAndCurrentDate(Long id) {
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        int numberCheckins = checkinRepository.countByUserIdAndCurrentDate(id, currentDate);
        System.out.println(numberCheckins);
        if (numberCheckins == 0) {

            User user = userRepository.findById(id).orElse(null);
            Checkin checkin = new Checkin();

            if (currentTime.isAfter(LocalTime.of(8, 30))) {
                checkin.setViolation(EnumViolation.LATE_CHECK_IN.name());
            }

            checkin.setCheckinTime(currentTime);
            checkin.setCheckinDate(currentDate);
            checkin.setUser(user);

            return checkinRepository.save(checkin);
        }

        List<Checkin> foundCheckins = checkinRepository.findCheckInByDateAndUserId(currentDate, id);

        Checkin endCheckin = foundCheckins.get(foundCheckins.size() - 1);
        endCheckin.setCheckoutTime(LocalTime.now());

        return checkinRepository.save(endCheckin);
    }



    @Override
    public List<UserCheckin> getAllCheckinsByTimeBetweenAnUserId(Long id, TimeBetweenRequestDTO timeBetweenRequest) {

        LocalDate startDate = timeBetweenRequest.getStartDate();
        LocalDate endDate = timeBetweenRequest.getEndDate();

        //return checkinRepository.findAllCheckinsByTimeBetweenAndUserId(id, startDate, endDate);

        return checkinRepository.findAllCheckinsByTimeBetweenAndUserId(id, startDate, endDate);
    }

    @Override
    public List<Checkin> getAllCheckinWithPagination(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Checkin> pagedResult = checkinRepository.findAll(pageable);

        return pagedResult.toList();
    }

    @Override
    public List<ICheckinViolation> getCheckinsByUserIdAndMonth(Long id, MonthRequestDTO month) {

        int monthOfViolations = month.getMonth();
        int yearOfViolations = month.getYear();

        return checkinRepository.findAllViolationByIdAndMonth(id, monthOfViolations, yearOfViolations);
    }
}
