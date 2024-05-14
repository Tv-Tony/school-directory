package com.tvtoner.schoolapp.controllers.usercontrollers;

import com.tvtoner.schoolapp.entity.Course;
import com.tvtoner.schoolapp.entity.Student;
import com.tvtoner.schoolapp.entity.assignments.Answer;
import com.tvtoner.schoolapp.entity.assignments.Assignment;
import com.tvtoner.schoolapp.entity.assignments.Grade;
import com.tvtoner.schoolapp.entity.assignments.Question;
import com.tvtoner.schoolapp.security.entity.User;
import com.tvtoner.schoolapp.security.service.UserService;
import com.tvtoner.schoolapp.service.StudentService;
import com.tvtoner.schoolapp.service.helper.AssignmentGradeInfo;
import com.tvtoner.schoolapp.service.helper.CourseAverageGradeInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class StudentController {

    private final UserService userService;

    private final StudentService studentService;

    @Autowired
    public StudentController(UserService userService, StudentService studentService) {
        this.userService = userService;
        this.studentService = studentService;
    }

    /**
     * This method gets the student through the authentication and the courses of the student. Then we use a helper method
     * called CourseAverageGradeInfo, it is an array list. for every course we get the assignments, and inside is another loop
     * that calculates the average of all the assignments in the course if there is a grade associated with the assignment.
     * Then a new CourseAverageGradeInfo object is created and a course and average grade are added into the constructor of this
     * and it is added to the array list. We add this array list to the model so that we can show every course the student
     * has and the average grade of the class
     * @param theModel model to add attributes
     * @param authentication authentication to get the student
     * @return student home page template
     */
    @GetMapping("/studentHomePage")
    public String studentHomePage(Model theModel, Authentication authentication){

        String username = authentication.getName();

        User theUser = userService.findByUserName(username);

        // Get the student
        Student student = theUser.getStudent();

        // Get the courses for the student
        List<Course> courses = student.getCourses();

        // Create a list to store course information with average grades
        List<CourseAverageGradeInfo> courseAverageGradeInfos = new ArrayList<>();

        // Iterate over each course
        for (Course course : courses) {
            // Get the assignments for the course
            List<Assignment> assignments = course.getAssignments();

            // Calculate the average grade for all assignments in the course
            double totalGrade = 0;
            int assignmentCount = 0;
            for (Assignment assignment : assignments) {
                Grade grade = studentService.getGradeByAssignmentAndStudent(assignment.getId(), student.getId());
                if (grade != null) {
                    totalGrade += grade.getGrade();
                    assignmentCount++;
                }
            }
            double averageGrade = (assignmentCount > 0) ? totalGrade / assignmentCount : 0;

            // Create CourseAverageGradeInfo object to hold course and average grade information
            CourseAverageGradeInfo courseAverageGradeInfo = new CourseAverageGradeInfo(course, averageGrade);

            // Add to the list
            courseAverageGradeInfos.add(courseAverageGradeInfo);
        }

        theModel.addAttribute("user", theUser);
        theModel.addAttribute("courseAverageGradeInfos", courseAverageGradeInfos);

        return "users/student-home-page";
    }

    /**
     * Retrieves a list of available courses for a student to add. Filters courses and only display courses student
     * is not already enrolled in
     *
     * @param theModel the model to add attributes
     * @param session the HTTP session to retrieve user information
     * @return the view name for the page displaying available courses
     */
    @GetMapping("/addCourses")
    public String allCourses(Model theModel, HttpSession session){

        User user = (User) session.getAttribute("user");

        List<Course> studentCourses = user.getStudent().getCourses();

        List<Course> allCourses = studentService.getAllCourses();

        // Filter out the courses that the student is already enrolled in
        List<Course> availableCourses = allCourses.stream()
                .filter(course -> studentCourses.stream().noneMatch(sc -> sc.getTitle().equals(course.getTitle())))
                .collect(Collectors.toList());

        theModel.addAttribute("courses", availableCourses);

        return "add-courses-page";
    }

    /**
     * This method adds a selected course to the student's enrolled courses. Gets student data from session unlike
     * previous methods were the data is sent through the model. Also, the user service is cached, so we have to add it back into
     * the attribute to refresh and save the user.student() with the correct changes
     *
     * @param session the HTTP session to retrieve user information
     * @param courseId the ID of the course to be added
     * @return a redirect to the student home page after adding the course
     */
    @PostMapping("/addCourseButton")
    public String addCourse(HttpSession session,
                            @RequestParam("courseId") long courseId){

        User user = (User) session.getAttribute("user");

        long studentId = user.getStudent().getId();

        studentService.addAndSaveCourse(studentId,courseId);

        //Refresh the user because changes are cached
        user = userService.findByUserName(user.getUserName());
        session.setAttribute("user", user);


        return "redirect:/studentHomePage";
    }

    /**
     * Removes a selected course from the student's enrolled courses.
     *
     * @param session the HTTP session to retrieve user information
     * @param courseId the ID of the course to be removed
     * @return a redirect to the student home page after removing the course
     */
    @PostMapping("/removeCourseFromStudent")
    public String removeCourseFromStudent(HttpSession session,
                                          @RequestParam("courseId") long courseId){

        User user = (User) session.getAttribute("user");

        long studentId = user.getStudent().getId();

        studentService.removeStudentCourse(studentId, courseId);

        //Refresh the user because changes are cached
        user = userService.findByUserName(user.getUserName());
        session.setAttribute("user", user);

        return "redirect:/studentHomePage";
    }

    /**
     * Displays the assignments for a specific course. A helper method is used here to display the grade of an assignment
     * if it exists
     *
     * @param courseId the ID of the course to display assignments for
     * @param session the HTTP session to retrieve user information
     * @param theModel the model to add attributes
     * @return the view name for the page displaying course assignments
     */
    @GetMapping("/showAssignments")
    public String courseAssignments(@RequestParam("courseId") long courseId,
                                    HttpSession session,
                                    Model theModel) {

        // Get the course
        Course course = studentService.getCourseById(courseId);

        // Get the student
        User user = (User) session.getAttribute("user");
        Student student = user.getStudent();

        // Get the assignments for the course
        List<Assignment> assignments = course.getAssignments();

        // Create a list to store assignment information with grades
        List<AssignmentGradeInfo> assignmentGradeInfos = new ArrayList<>();

        // Iterate over each assignment
        for (Assignment assignment : assignments) {
            // Get the grade for the assignment and student
            Grade grade = studentService.getGradeByAssignmentAndStudent(assignment.getId(), student.getId());

            // Create AssignmentGradeInfo object to hold assignment and grade information
            AssignmentGradeInfo assignmentGradeInfo = new AssignmentGradeInfo(assignment, grade);

            // Add to the list
            assignmentGradeInfos.add(assignmentGradeInfo);
        }

        // Add the course and assignment information to the model
        theModel.addAttribute("course", course);
        theModel.addAttribute("assignmentGradeInfos", assignmentGradeInfos);

        return "show-assignments";
    }

    /**
     * Displays an assignment for a student to complete in a quiz like format.
     *
     * @param assignmentId the ID of the assignment to be completed
     * @param theModel model to add attributes
     * @return the view name for the page displaying the assignment
     */
    @GetMapping("/doAssignment")
    public String takeTest(@RequestParam("assignmentId") long assignmentId,
                           Model theModel){

        theModel.addAttribute("assignment", studentService.getAssignmentById(assignmentId));

        return "test-page";
    }

    /**
     * Processes the score for an assignment submitted by a student. Answers chosen by student are in html servlet.
     * For every question, we get the answer, and check if it is correct. We calculate the score of the assignment and get
     * grade and save it.
     *
     * @param assignmentId the ID of the assignment to score
     * @param request the HTTP servlet request to retrieve submitted answers
     * @param session the HTTP session to retrieve user information
     * @return a redirect to the page displaying course assignments after scoring
     */
    @GetMapping("/processAssignmentScore")
    public String processAssignmentScore(@RequestParam("assignmentId") long assignmentId,
                                         HttpServletRequest request,
                                         HttpSession session) {

        Assignment assignment = studentService.getAssignmentById(assignmentId);

        // Get the courseId from the assignment
        long courseId = assignment.getCourse().getId();

        // Get the size of the test
        double testSize = assignment.getQuestions().size();
        double testScore = 0;

        // Iterate over each question in the assignment
        for (Question question : assignment.getQuestions()) {
            // Get the selected answer for this question from the request parameters
            String selectedAnswerId = request.getParameter("question-" + question.getId());

            // Find the corresponding answer object
            Answer selectedAnswer = question.getAnswers().stream()
                    .filter(answer -> String.valueOf(answer.getId()).equals(selectedAnswerId))
                    .findFirst()
                    .orElse(null);

            // Check if the selected answer is correct
            if (selectedAnswer != null && selectedAnswer.isQuestionValue()) {
                testScore += 1;
            }
        }

        // Calculate the final score
        double finalScore = (testScore / testSize) * 100;

        // Get the student
        User user = (User) session.getAttribute("user");
        Student theStudent = user.getStudent();

        // Save the grade
        studentService.saveTheGrade(assignment, theStudent, finalScore);

        // Redirect to the courseAssignments page with the courseId
        return "redirect:/showAssignments?courseId=" + courseId;
    }


}
