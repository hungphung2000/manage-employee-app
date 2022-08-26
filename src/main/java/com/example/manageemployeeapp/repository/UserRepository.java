package com.example.manageemployeeapp.repository;

import com.example.manageemployeeapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByOrderByName();

    @Query(value = "SELECT * FROM users u WHERE u.username LIKE '%?1'", nativeQuery = true)
    List<User> findByNameLike(String name);

    Optional<User> findByEmail(String email);


}
