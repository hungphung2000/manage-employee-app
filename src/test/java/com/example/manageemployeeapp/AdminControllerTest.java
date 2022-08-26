package com.example.manageemployeeapp;

import com.example.manageemployeeapp.controllers.AdminController;
import com.example.manageemployeeapp.dto.UserResponseDTO;
import com.example.manageemployeeapp.entity.Role;
import com.example.manageemployeeapp.enums.EnumRole;
import com.example.manageemployeeapp.services.impl.CheckinServiceImpl;
import com.example.manageemployeeapp.services.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
@ExtendWith(MockitoExtension.class)
public class AdminControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    UserServiceImpl userService;

    @MockBean
    CheckinServiceImpl checkinService;

    @Test
    @WithMockUser
    @AutoConfigureMockMvc(addFilters = false)
    public void testGetAllEmployees() throws Exception {
        List<UserResponseDTO> testList = new ArrayList<>();

        Long id = Long.valueOf(1);
        String name = "alo";
        String email = "alo@gmail.com";
        int checkinCode = 1234;
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(1L, EnumRole.EMPLOYEE));

        UserResponseDTO testUser = new UserResponseDTO(id, name, email, checkinCode, roles);

        testList.add(testUser);
        Mockito.when(userService.getAll()).thenReturn(testList);

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk());

    }
}
