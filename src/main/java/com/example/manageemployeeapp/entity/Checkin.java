package com.example.manageemployeeapp.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "checkins")
@Builder
public class Checkin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Basic
    @Column(name = "checkin_time")
    private LocalTime checkinTime;

    @Basic
    @Column(name = "checkout_time")
    private LocalTime checkoutTime;
    @Basic
    @Column(name = "checkin_date")
    private LocalDate checkinDate;

    @Column(name = "complain")
    private String complain;

    @Column(name = "violation")
    private String violation;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Checkin(LocalTime checkinTime, LocalDate checkinDate, User user) {
        this.checkinTime = checkinTime;
        this.checkinDate = checkinDate;
        this.user = user;

    }

    public Checkin(String violation, User user) {
        this.violation = violation;
        this.user = user;
    }
}
