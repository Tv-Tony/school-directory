package com.tvtoner.schoolapp.security.service;

import com.tvtoner.schoolapp.entity.Instructor;
import com.tvtoner.schoolapp.entity.Student;
import com.tvtoner.schoolapp.security.entity.User;
import com.tvtoner.schoolapp.security.user.WebUser;
import org.aspectj.apache.bcel.generic.Instruction;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User findByUserName(String userName);

    void deleteUser(User user);

    void saveStudent(WebUser webUser);

    void updateStudent(Student theStudent);

    void saveInstructor(WebUser webUser);

    void updateInstructor(Instructor theInstructor);

}
