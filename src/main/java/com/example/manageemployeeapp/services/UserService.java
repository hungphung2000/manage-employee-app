package com.example.manageemployeeapp.services;

import com.example.manageemployeeapp.dto.UserRequestDTO;
import com.example.manageemployeeapp.dto.UserResponseDTO;
import com.example.manageemployeeapp.entity.User;
import java.util.List;

public interface UserService {

    User addUser(UserRequestDTO user);

    User updateUser(Long id, UserRequestDTO user);

    void deleteUser(Long id);

    User findById(Long id);

    List<UserResponseDTO> getAll() throws InterruptedException;

    List<UserResponseDTO> sortByName();

    List<UserResponseDTO> searchByName(String username);


}
