package com.tvtoner.schoolapp.controllers.usercontrollers;

import com.tvtoner.schoolapp.entity.Instructor;
import com.tvtoner.schoolapp.entity.Student;
import com.tvtoner.schoolapp.security.entity.User;
import com.tvtoner.schoolapp.security.service.UserService;
import com.tvtoner.schoolapp.security.user.WebUser;
import com.tvtoner.schoolapp.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AdminController {

    private final AdminService adminService;

    private final UserService userService;

    @Autowired
    public AdminController(AdminService adminService, UserService userService) {
        this.adminService = adminService;
        this.userService = userService;
    }

    @GetMapping("/adminHomePage")
    public String adminHomePage(Model theModel, Authentication authentication){


        System.out.println(authentication.toString());

        // get the list of students
        List<Student> students = adminService.getAllStudents();

        // get the list of instructors
        List<Instructor> instructors = adminService.getAllInstructors();


        // add them to the model to access in the html file
        theModel.addAttribute("students", students);
        theModel.addAttribute("instructors", instructors);

        return "users/admin-home-page";
    }

    @PostMapping("/updateStudent")
    public String updateStudent(@RequestParam("id") long studentId, Model theModel){

        // retrieve student,
        Student tempStudent = adminService.getStudentById(studentId);

        theModel.addAttribute("student", tempStudent);

        return "update/update-student";

    }

    @PostMapping("/processStudentUpdateForm")
    public String processUpdateStudent(@Valid @ModelAttribute("student") Student student,
                                       BindingResult theBindingResult,
                                       Model theModel){

        //Grab the existing student with the same id
        Student existingStudent = adminService.getStudentById(student.getId());

        // grab the user
        User existingUser = existingStudent.getUser();

        existingUser.setFirstName(student.getUser().getFirstName());
        existingUser.setLastName(student.getUser().getLastName());
        existingUser.setEmail(student.getUser().getEmail());


        if (theBindingResult.hasErrors()){
            return "update/update-student";
        }

       userService.updateStudent(existingStudent);

        System.out.println("Student Successfully Updated");

        return "redirect:/adminHomePage";
    }

    @PostMapping("/deleteStudent")
    public String deleteStudent(@RequestParam("id") long studentId){

        userService.deleteUser(adminService.getStudentById(studentId).getUser());

        System.out.println("Successfully deleted the student");

        return "redirect:/adminHomePage";
    }

    @PostMapping("/updateInstructor")
    public String updateInstructor(@RequestParam("id") long instructorId, Model theModel){

        // retrieve student,
        Instructor tempInstructor = adminService.getInstructorById(instructorId);

        theModel.addAttribute("instructor", tempInstructor);

        return "update/update-instructor";

    }

    @PostMapping("/processInstructorUpdateForm")
    public String processUpdateStudent(@Valid @ModelAttribute("instructor") Instructor instructor,
                                       BindingResult theBindingResult,
                                       Model theModel){

        //Grab the existing instructor with the same id
        Instructor existingInstructor = adminService.getInstructorById(instructor.getId());

        // grab the user
        User existingUser = existingInstructor.getUser();

        existingUser.setFirstName(instructor.getUser().getFirstName());
        existingUser.setLastName(instructor.getUser().getLastName());
        existingUser.setEmail(instructor.getUser().getEmail());


        if (theBindingResult.hasErrors()){
            return "update/update-instructor";
        }

        userService.updateInstructor(existingInstructor);

        System.out.println("Instructor Successfully Updated");

        return "redirect:/adminHomePage";
    }

    @PostMapping("/deleteInstructor")
    public String deleteInstructor(@RequestParam("id") long instructorId){
//        System.out.println("Delete Instructor Method");
//        userService.deleteUser(adminService.getInstructorById(instructorId).getUser());
//
//        System.out.println("Successfully deleted the Instructor");

        try {
            userService.deleteUser(adminService.getInstructorById(instructorId).getUser());
            System.out.println("Successfully deleted the Instructor");
        } catch (Exception e) {
            System.out.println("Error deleting instructor: " + e.getMessage());
        }


        return "redirect:/adminHomePage";
    }
}
