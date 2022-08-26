package com.example.manageemployeeapp.config;

import com.example.manageemployeeapp.enums.EnumRole;
import com.example.manageemployeeapp.jwt.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    LoginSuccessHandler loginSuccessHandler;

    @Autowired
    JwtTokenFilter jwtTokenFilter;

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.exceptionHandling()
                .authenticationEntryPoint(
                        (request, response, ex) -> {
                            response.sendError(
                                    HttpServletResponse.SC_UNAUTHORIZED,
                                    ex.getMessage()
                            );
                        }
                );

        http.authorizeHttpRequests()
                .antMatchers("/admin").hasAuthority(EnumRole.ADMIN.name())
                .antMatchers("/admin/**").hasAuthority(EnumRole.ADMIN.name())
                .antMatchers("/api").hasAnyAuthority(EnumRole.EMPLOYEE.name(), EnumRole.ADMIN.name())
                .antMatchers("/api/**").hasAnyAuthority(EnumRole.EMPLOYEE.name(), EnumRole.ADMIN.name())
                .anyRequest().authenticated()
                .and()
                .formLogin()
                    .successHandler(loginSuccessHandler)
                    .permitAll()
                .and().httpBasic()
                .and()
                .logout().permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/403");

        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
