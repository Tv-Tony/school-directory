package com.tvtoner.schoolapp.security.security;

import com.tvtoner.schoolapp.security.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class DemoSecurityConfig {

    //bcrypt bean definition
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //authenticationProvider bean definition
    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserService userService) {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService); //set the custom user details service
        auth.setPasswordEncoder(passwordEncoder()); //set the password encoder - bcrypt
        return auth;
    }

    /**
     * Configures the security filter chain with access rules for different roles and authentication settings.
     *
     * @param http The HttpSecurity object to configure security settings
     * @param customAuthenticationSuccessHandler The custom authentication success handler
     * @return The configured SecurityFilterChain
     * @throws Exception If an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationSuccessHandler customAuthenticationSuccessHandler) throws Exception {

        http.authorizeRequests(configurer ->
                        configurer
                                .requestMatchers("/adminHomePage").hasRole("ADMIN")
                                .requestMatchers("/processStudentUpdateForm").hasRole("ADMIN")
                                .requestMatchers("/deleteStudent").hasRole("ADMIN")
                                .requestMatchers("/updateInstructor").hasRole("ADMIN")
                                .requestMatchers("/processInstructorUpdateForm").hasRole("ADMIN")
                                .requestMatchers("/deleteInstructor").hasRole("ADMIN")
                                .requestMatchers("/deleteCourse").hasRole("ADMIN")
                                .requestMatchers("/instructorSelection").hasRole("ADMIN")
                                .requestMatchers("/addInstructor").hasRole("ADMIN")
                                .requestMatchers("/saveCourseChanges").hasRole("ADMIN")
                                .requestMatchers("/showCreateSCourseForm").hasRole("ADMIN")
                                .requestMatchers("/processCourseCreate").hasRole("ADMIN")
                                .requestMatchers("/viewCourseStudents").hasRole("ADMIN")

                                .requestMatchers("/instructorHomePage").hasRole("INSTRUCTOR")
                                .requestMatchers("/viewCourseAssignments").hasRole("INSTRUCTOR")
                                .requestMatchers("/processAssignment").hasRole("INSTRUCTOR")
                                .requestMatchers("/createAssignment").hasRole("INSTRUCTOR")
                                .requestMatchers("/processQuestion").hasRole("INSTRUCTOR")
                                .requestMatchers("/processAnswer").hasRole("INSTRUCTOR")
                                .requestMatchers("/deleteAssignment").hasRole("INSTRUCTOR")
                                .requestMatchers("/courseStudents").hasRole("INSTRUCTOR")

                                .requestMatchers("/studentHomePage").hasRole("STUDENT")
                                .requestMatchers("/addCourses").hasRole("STUDENT")
                                .requestMatchers("/addCourseButton").hasRole("STUDENT")
                                .requestMatchers("/removeCourseFromStudent").hasRole("STUDENT")
                                .requestMatchers("/showAssignments").hasRole("STUDENT")
                                .requestMatchers("/doAssignment").hasRole("STUDENT")

                                .requestMatchers("/register/**").permitAll()
                                .requestMatchers("/").permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin(form ->
                        form
                                .loginPage("/loginPage")
                                .loginProcessingUrl("/authenticateTheUser")
                                .successHandler(customAuthenticationSuccessHandler)
                                .permitAll()
                )
                .logout(logout -> logout.permitAll())
                .exceptionHandling(configurer ->
                        configurer.accessDeniedPage("/access-denied")
                );

        return http.build();
    }

}
