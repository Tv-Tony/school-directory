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
import org.springframework.web.bind.annotation.*;

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

    /**
     * This method uses the admin service adn sends attributes to be displayed on the admin
     * homepage
     * @param theModel model to add attributes
     * @param authentication authentication to check if its working correctly
     * @return thymeleaf template for admin home page
     */
    @GetMapping("/adminHomePage")
    public String adminHomePage(Model theModel, Authentication authentication){


        System.out.println(authentication.toString());

        // get the list of students
        List<Student> students = adminService.getAllStudents();

        // get the list of instructors
        List<Instructor> instructors = adminService.getAllInstructors();

        // get the list of courses
        List<Course> courses = adminService.getAllCourses();


        // add them to the model to access in the html file
        theModel.addAttribute("students", students);
        theModel.addAttribute("instructors", instructors);
        theModel.addAttribute("courses", courses);

        return "users/admin-home-page";
    }

    /**
     * This method puts a student object in the update html template, thymeleaf automatically calls
     * getters for the fields, so they are already pre-populated making it easier to update
     * @param studentId Get the parameter to access the student
     * @param theModel model to add attributes
     * @return thymeleaf template for updating the student
     */
    @PostMapping("/updateStudent")
    public String updateStudent(@RequestParam("id") long studentId, Model theModel){

        // retrieve student,
        Student tempStudent = adminService.getStudentById(studentId);

        // add the student to the model
        theModel.addAttribute("student", tempStudent);

        return "update/update-student";

    }

    /**
     * This method grabs the student object that had the updates from the setters of thymeleaf
     * then grabs a user, we make an existing student object that holds the same information as the
     * given model attribute, and we set the users name and email and then save the student, since the
     * id is of an existing student it is updated
     * @param student receive the student attribute from template
     * @param theBindingResult holds the result of binding and any validation errors
     * @param theModel model to add attributes
     * @return sends user to admin homepage
     */
    @PostMapping("/processStudentUpdateForm")
    public String processUpdateStudent(@Valid @ModelAttribute("student") Student student,
                                       BindingResult theBindingResult,
                                       Model theModel){

        //Grab the existing student with the same id
        Student existingStudent = adminService.getStudentById(student.getId());

        // grab the user
        User existingUser = existingStudent.getUser();

        // set the values
        existingUser.setFirstName(student.getUser().getFirstName());
        existingUser.setLastName(student.getUser().getLastName());
        existingUser.setEmail(student.getUser().getEmail());

        // check binding results for errors
        if (theBindingResult.hasErrors()){
            return "update/update-student";
        }

        // update the student
       userService.updateStudent(existingStudent);

        System.out.println("Student Successfully Updated");

        return "redirect:/adminHomePage";
    }

    /**
     * Takes the student id from the template and gets the student object and deletes it
     * @param studentId student id to access student
     * @return redirect to admin home page
     */
    @PostMapping("/deleteStudent")
    public String deleteStudent(@RequestParam("id") long studentId){

        userService.deleteUser(adminService.getStudentById(studentId).getUser());

        System.out.println("Successfully deleted the student");

        return "redirect:/adminHomePage";
    }

    /**
     * get the instructor id and retrieve the instructor and add the object in the model for the
     * thymeleaf template to process
     * @param instructorId id of the instructor
     * @param theModel model to add attributes
     * @return update instructor template
     */
    @PostMapping("/updateInstructor")
    public String updateInstructor(@RequestParam("id") long instructorId, Model theModel){

        // retrieve instructor
        Instructor tempInstructor = adminService.getInstructorById(instructorId);

        // add the attribute
        theModel.addAttribute("instructor", tempInstructor);

        return "update/update-instructor";

    }

    /**
     * This method takes the Instructor object with the updated fields from html and processes them,
     * so they can be saved into the db
     * @param instructor instructor object with updated fields from thymeleaf setter
     * @param theBindingResult checks for validation errors
     * @param theModel model to add attributes
     * @return redirect to the admin home page
     */
    @PostMapping("/processInstructorUpdateForm")
    public String processUpdateStudent(@Valid @ModelAttribute("instructor") Instructor instructor,
                                       BindingResult theBindingResult,
                                       Model theModel){

        //Grab the existing instructor with the same id
        Instructor existingInstructor = adminService.getInstructorById(instructor.getId());

        // grab the user
        User existingUser = existingInstructor.getUser();

        // set the fields
        existingUser.setFirstName(instructor.getUser().getFirstName());
        existingUser.setLastName(instructor.getUser().getLastName());
        existingUser.setEmail(instructor.getUser().getEmail());

        // check for validation errors
        if (theBindingResult.hasErrors()){
            return "update/update-instructor";
        }

        // update the instructor
        userService.updateInstructor(existingInstructor);

        System.out.println("Instructor Successfully Updated");

        return "redirect:/adminHomePage";
    }

    /**
     * Method uses the userService and instructor id to get the user
     * @param instructorId instructor id parameter from template
     * @return redirect the admin home page
     */
    @PostMapping("/deleteInstructor")
    public String deleteInstructor(@RequestParam("id") long instructorId){

        // try catch to delete the user, cascade will also delete the instructor
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

    /**
     * This method gets the id of the course, so we can put in the model as well as the
     * list of all the teachers, so we can give html access to all of these
     * @param id Course id given by template
     * @param theModel model to add attributes
     * @return model with instructors list and course to be able to access in the template
     */
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

    /**
     * Updates the title of the course
     * @param courseId id of current course
     * @param course Course model attribute
     * @param theBindingResult has result of binding and used for validation purposes
     * @param theModel model to add attributes
     * @return to admin home page
     */
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

    /**
     * Method just directs the admin user to the course create form
     * @param theModel model to add attributes
     * @return create course form
     */
    @GetMapping("/showCreateSCourseForm")
    public String createCourse(Model theModel){

        theModel.addAttribute("course", new Course());

        return "register/create-course";
    }

    /**
     * this method processes the create course form and saves to db
     * @param title new course title
     * @return to admin home page
     */
    @PostMapping("/processCourseCreate")
    public String course(@RequestParam("courseTitle") String title){

        System.out.println("In courseCreateProcess");
        Course course = new Course();

        course.setId(0);
        course.setTitle(title);
        adminService.updateCourse(course);

       return "redirect:/adminHomePage";
    }

    /**
     * this method requests the course id from the previous page and sends it to the template from where we can call
     * course.students() to get the students of this specific course
     * @param courseId course id
     * @param theModel model to add attributes
     * @return view students from the admin home page
     */
    @GetMapping("/viewCourseStudents")
    public String viewCourseStudents(@RequestParam("id") long courseId,
                                     Model theModel){


        theModel.addAttribute("course", adminService.getCourseById(courseId));

        return "course-students-admin-page";
    }


}
