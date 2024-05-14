package com.tvtoner.schoolapp.security.service;

import com.tvtoner.schoolapp.dao.InstructorRepository;
import com.tvtoner.schoolapp.dao.StudentRepository;
import com.tvtoner.schoolapp.entity.Instructor;
import com.tvtoner.schoolapp.entity.Student;
import com.tvtoner.schoolapp.security.dao.RoleDao;
import com.tvtoner.schoolapp.security.dao.UserDao;
import com.tvtoner.schoolapp.security.entity.Role;
import com.tvtoner.schoolapp.security.entity.User;
import com.tvtoner.schoolapp.security.user.WebUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    private final RoleDao roleDao;

    private final StudentRepository studentRepo;

    private final InstructorRepository instructorRepo;

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserDao userDao, RoleDao roleDao, StudentRepository studentRepo, InstructorRepository instructorRepo, BCryptPasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.studentRepo = studentRepo;
        this.instructorRepo = instructorRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User findByUserName(String userName) {
        // check the database if the user already exists
        return userDao.findByUserName(userName);
    }

    @Override
    public void deleteUser(User user) {
        try{
            userDao.deleteUser(user);
        }
        catch (Exception e){
            System.out.println("Could not delete the user " + e);
        }
    }

    @Override
    public void saveStudent(WebUser webUser) {
        User user = new User();

        // assign user details to the user object
        user.setUserName(webUser.getUserName());
        user.setPassword(passwordEncoder.encode(webUser.getPassword()));
        user.setFirstName(webUser.getFirstName());
        user.setLastName(webUser.getLastName());
        user.setEmail(webUser.getEmail());
        user.setEnabled(true);

        // give user default role of "employee"
        user.setRole(roleDao.findRoleByName("ROLE_STUDENT"));

        System.out.println("Saved as a student");

        // save user in the database
//        userDao.save(user);

        // save student into the database which in turn will save user
        studentRepo.save(new Student(user));
    }


    @Override
    public void updateStudent(Student theStudent) {
        studentRepo.save(theStudent);
        System.out.println("Student has been saved");
    }

    @Override
    public void saveInstructor(WebUser webUser) {
        User user = new User();

        // assign user details to the user object
        user.setUserName(webUser.getUserName());
        user.setPassword(passwordEncoder.encode(webUser.getPassword()));
        user.setFirstName(webUser.getFirstName());
        user.setLastName(webUser.getLastName());
        user.setEmail(webUser.getEmail());
        user.setEnabled(true);

        // give user default role of "employee"
        user.setRole(roleDao.findRoleByName("ROLE_INSTRUCTOR"));

        System.out.println("Saved as a instructor");
        // save user in the database
//        userDao.save(user);

        //save instructor and user in database
        instructorRepo.save(new Instructor(user));
    }

    @Override
    public void updateInstructor(Instructor theInstructor) {
        instructorRepo.save(theInstructor);
        System.out.println("Instructor Saved");
    }

    @Override
    public void updateUser(User theUser) {
        userDao.save(theUser);
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userDao.findByUserName(userName);

        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }

        Collection<SimpleGrantedAuthority> authorities = mapRolesToAuthorities(Collections.singleton(user.getRole()));

        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(),
                authorities);
    }

    private Collection<SimpleGrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

        for (Role tempRole : roles) {
            SimpleGrantedAuthority tempAuthority = new SimpleGrantedAuthority(tempRole.getName());
            authorities.add(tempAuthority);
        }

        return authorities;
    }
}

