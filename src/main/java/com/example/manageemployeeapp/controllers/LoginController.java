package com.example.manageemployeeapp.controllers;

import com.example.manageemployeeapp.config.CustomUserDetails;
import com.example.manageemployeeapp.dto.AuthRequestDto;
import com.example.manageemployeeapp.dto.AuthResponseDto;
import com.example.manageemployeeapp.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    JwtTokenProvider jwtUtil;

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody AuthRequestDto request) {
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
            String accessToken = jwtUtil.generateToken(user);
            AuthResponseDto response = new AuthResponseDto(user.getUsername(), accessToken);

            return ResponseEntity.ok().body(response);

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
