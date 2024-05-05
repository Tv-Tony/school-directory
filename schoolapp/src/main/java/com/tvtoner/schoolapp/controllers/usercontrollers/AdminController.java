package com.tvtoner.schoolapp.controllers.usercontrollers;

import com.tvtoner.schoolapp.entity.Course;
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

        List<Course> courses = adminService.getAllCourses();


        // add them to the model to access in the html file
        theModel.addAttribute("students", students);
        theModel.addAttribute("instructors", instructors);
        theModel.addAttribute("courses", courses);

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

    @PostMapping("/deleteCourse")
    public String deleteCourse(@RequestParam("id") long courseId){

        try {
            adminService.deleteCourse(courseId);
            System.out.println("Successfully deleted the Course");
        } catch (Exception e) {
            System.out.println("Error deleting Course: " + e.getMessage());
        }


        return "redirect:/adminHomePage";
    }

    @GetMapping("/instructorSelection")
    public String instructorSelection(@RequestParam("id") long id, Model theModel){

        theModel.addAttribute("instructors", adminService.getAllInstructors());
        theModel.addAttribute("course", adminService.getCourseById(id));

        return "instructor-selection";
    }

    @PostMapping("/addInstructor")
    public String addInstructorToCourse(@RequestParam("id") long instructorId, @RequestParam("course") long courseId){
        Instructor theInstructor = adminService.getInstructorById(instructorId);

        System.out.println(theInstructor);

        Course theCourse = adminService.getCourseById(courseId);

        System.out.println(theCourse);

        theCourse.setInstructor(theInstructor);

        adminService.updateCourse(theCourse);

        System.out.println("Instructor set");

        return "redirect:/instructorSelection?id=" + courseId;
    }

    @PostMapping("/saveCourseChanges")
    public String saveCourseChanges(@RequestParam("id") long courseId,
                                    @Valid @ModelAttribute("course") Course course,
                                    BindingResult theBindingResult,
                                    Model theModel) {

        // Print received course ID
        System.out.println("Received Course ID: " + courseId);
        System.out.println(course.getTitle());
        // Retrieve existing course by ID
        Course existingCourse = adminService.getCourseById(courseId);

        // Update course details
        existingCourse.setTitle(course.getTitle());

        // Check for binding errors
        if (theBindingResult.hasErrors()) {
            return "redirect:/adminHomePage";
        }

        // Update the existing course
        adminService.updateCourse(existingCourse);

        System.out.println("Course Successfully Updated");

        return "redirect:/adminHomePage";
    }

    @GetMapping("/showCreateSCourseForm")
    public String createCourse(Model theModel){

        theModel.addAttribute("course", new Course());

        return "register/create-course";
    }

    @PostMapping("/processCourseCreate")
    public String course(@RequestParam("courseTitle") String title){

        System.out.println("In courseCreateProcess");
        Course course = new Course();

        course.setId(0);
        course.setTitle(title);
        adminService.updateCourse(course);

       return "redirect:/adminHomePage";

    }


}
