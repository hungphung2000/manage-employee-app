package com.example.manageemployeeapp.services.impl;

import com.example.manageemployeeapp.dto.UserRequestDTO;
import com.example.manageemployeeapp.dto.UserResponseDTO;
import com.example.manageemployeeapp.entity.Role;
import com.example.manageemployeeapp.entity.User;
import com.example.manageemployeeapp.repository.RoleRepository;
import com.example.manageemployeeapp.repository.UserRepository;
import com.example.manageemployeeapp.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    public static final int TOTAL_ADMIN = 2;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    AsyncMailService asyncMailService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    @Cacheable("users")
    @Async
    public User addUser(UserRequestDTO user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName("user");
        }

        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            return null;
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            return null;
        }

        User savedUser = mapper.map(user, User.class);
        int checkinCode = ThreadLocalRandom.current().nextInt(1000, 10000);
        savedUser.setCheckinCode(checkinCode);

        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findById(Long.valueOf(2)).get());
        savedUser.setRoles(roles);

        asyncMailService.sendMail(user);

        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        savedUser.setPassword(encodedPassword);

        return userRepository.save(savedUser);
    }

    @Override
    @CachePut("users")
    public User updateUser(Long id, UserRequestDTO user) {
        if (id < TOTAL_ADMIN) {
            return null;
        }

        User userDB = userRepository.findById(id).orElse(null);

        if (userDB == null) {
            return null;
        }

        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());

        userDB.setEmail(user.getEmail());
        userDB.setName(user.getName());
        userDB.setPassword(encodedPassword);

        return userRepository.save(userDB);
    }

    @Override
    @CacheEvict("users")
    public void deleteUser(Long id) {
        if (id > TOTAL_ADMIN) {
            userRepository.deleteById(id);
        }
    }

    @Override
    @Cacheable("users")
    public User findById(Long id) {

        return userRepository.findById(id).orElse(null);
    }

    @Override
    @Cacheable("users_dto")
    public List<UserResponseDTO> getAll() {
        System.out.println("hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh: " );

        return userRepository
                .findAll()
                .stream()
                .map(savedUser -> mapper.map(savedUser, UserResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable("users_dto")
    public List<UserResponseDTO> sortByName() {

        return userRepository
                .findByOrderByName()
                .stream()
                .map(user -> mapper.map(user, UserResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable("users_dto")
    public List<UserResponseDTO> searchByName(String username) {
        return userRepository
                .findAll()
                .stream()
                .filter(user -> user.getName().contains(username))
                .map(user -> mapper.map(user, UserResponseDTO.class))
                .collect(Collectors.toList());
    }
}




