package com.example.manageemployeeapp.repository;

import com.example.manageemployeeapp.entity.Checkin;
import com.example.manageemployeeapp.model.ICheckinViolation;
import com.example.manageemployeeapp.model.UserCheckin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface CheckinRepository extends JpaRepository<Checkin, Integer> {

    @Query(value = "SELECT c FROM Checkin c WHERE c.checkinDate = :checkin AND c.user.id = :id")
    List<Checkin> findCheckInByDateAndUserId(@Param("checkin") LocalDate date, @Param("id") Long id);

    @Query(value = "DELETE FROM checkins c WHERE c.user_id = ?1 and c.checkin_date = ?2", nativeQuery = true)
    void deleteByUserIdAndDate(Long id, LocalDate date);

    @Query(value = "SELECT c.violation FROM checkins c WHERE MONTH(c.checkin_date) = :month AND YEAR(c.checkin_date) = :year", nativeQuery = true)
    List<String> findViolationsByMonth(@Param("month") int month, @Param("year") int year);

    @Query(value = "SELECT c.violation FROM checkins c WHERE MONTH(c.checkin_date) = :month AND YEAR(c.checkin_date) = :year AND c.user_id = :user_id", nativeQuery = true)
    List<String> findViolationsByMonthAndUserId(@Param("month") int month, @Param("year") int year, @Param("user_id") Long id);

    @Query("SELECT c FROM Checkin c WHERE c.checkinDate BETWEEN :start AND :end")
    List<Checkin> findAllCheckinsByTimeBetween(@Param("start") LocalDate startCheckinDate, @Param("end") LocalDate endCheckinDate);

    @Query(value = "SELECT COUNT(*) FROM checkins c WHERE c.user_id = ?1 AND c.checkin_date = ?2", nativeQuery = true)
    int countByUserIdAndCurrentDate(Long id, LocalDate currentDate);

    //truy van resultset voi constructor
    @Query(value = "SELECT new com.example.manageemployeeapp.model.UserCheckin( " +
            "u.name as username, u.email as email, c.complain as complain, c.violation as violation) " +
            "FROM User as u INNER JOIN Checkin c on u.id = c.user.id " +
            "WHERE u.id = :id AND c.checkinDate > :start_date " +
            "AND c.checkinDate < :end_date")
    List<UserCheckin> findAllCheckinsByTimeBetweenAndUserId(@Param("id") Long id, @Param("start_date") LocalDate startDate, @Param("end_date") LocalDate endDate);

    //pagination
    Page<Checkin> findAll(Pageable pageable);

    // truy van voi projection
    @Query(value = "select u.user_id as userId, c.checkin_date as checkinDate, c.violation as violation from users as u inner join checkins as c on u.user_id = c.user_id where u.user_id = :id and month(c.checkin_date) = :month and year(c.checkin_date) = :year", nativeQuery = true)
    List<ICheckinViolation> findAllViolationByIdAndMonth(@Param("id") Long id, @Param("month") int month, @Param("year") int year);

}
