package com.tvtoner.schoolapp.controllers.usercontrollers;

import com.tvtoner.schoolapp.entity.Course;
import com.tvtoner.schoolapp.entity.assignments.Answer;
import com.tvtoner.schoolapp.entity.assignments.Assignment;
import com.tvtoner.schoolapp.entity.assignments.Question;
import com.tvtoner.schoolapp.security.entity.User;
import com.tvtoner.schoolapp.security.service.UserService;
import com.tvtoner.schoolapp.security.user.WebUser;
import com.tvtoner.schoolapp.service.InstructorService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class InstructorController {

    private final InstructorService instructorService;

    private final UserService userService;

    @Autowired
    public InstructorController(InstructorService instructorService, UserService userService) {
        this.instructorService = instructorService;
        this.userService = userService;
    }

    /**
     *
     * @param theModel
     * @param authentication
     * @return
     */
    @GetMapping("/instructorHomePage")
    public String instructorHomePage(Model theModel, Authentication authentication){

        String username = authentication.getName();

        User theUser = userService.findByUserName(username);

        System.out.println(theUser);

        theModel.addAttribute("authentication", authentication);
        theModel.addAttribute("user", theUser);

        return "users/instructor-home-page";
    }

    @GetMapping("/viewCourseAssignments")
    public String viewCourseAssignments(@RequestParam("id") long courseID, Model theModel){

        theModel.addAttribute("course", instructorService.getCourseById(courseID));

        return "course-assignments";

    }

    @PostMapping("/processAssignment")
    public String processAssignment(@RequestParam("courseId") long courseId,
                                    @RequestParam("title") String title,
                                    Model model,
                                    HttpSession session) {

        // Assuming you have a service method to save the assignment
        Assignment assignment = (Assignment) session.getAttribute("assignment");
        assignment.setTitle(title);

        // Retrieve the course by courseId (assuming you have a method to do this)
        Course course = instructorService.getCourseById(courseId);

        // Assign the course to the assignment
        assignment.setCourse(course);

        for (Question e : assignment.getQuestions()){
            e.setAssignment(assignment);
            for (Answer a : e.getAnswers()){
                a.setQuestion(e);
            }
        }

        instructorService.saveAssignment(assignment);

        session.removeAttribute("assignment");


        return "redirect:/instructorHomePage"; // Redirect to the instructor home page or any other page as needed
    }



    @GetMapping("/createAssignment")
    public String createAssignment(@RequestParam("courseId") long courseId, HttpSession session, Model model) {

        Assignment assignment = (Assignment) session.getAttribute("assignment");
        if (assignment == null) {
            assignment = new Assignment();
            session.setAttribute("assignment", assignment);
        }

        try {
            System.out.println(assignment);
            System.out.println(assignment.getQuestions());
            System.out.println(assignment.getQuestions().getFirst().getAnswers());
        }
        catch (Exception e){
            System.out.println("Assignment is null");
        }

        // Populate other model attributes as needed
        // For example:
        model.addAttribute("course", instructorService.getCourseById(courseId));
        model.addAttribute("question", new Question());

        return "create-assignment";
    }

    @PostMapping("/processQuestion")
    public String processQuestion(@RequestParam("courseId") long courseId,
                                  @RequestParam("question") String question,
                                  HttpSession session,
                                  Model model,
                                  @RequestParam("assignmentTitle") String title) {

        Assignment assignment = (Assignment) session.getAttribute("assignment");
        if (assignment == null) {
            // Handle error or redirect to an appropriate page
        }

        // Here you can handle the submitted question data and add it to the assignment
        Assignment assignment1 = (Assignment) session.getAttribute("assignment");
        assignment1.addQuestion(new Question(question));

        assignment1.setTitle(title);

        System.out.println(assignment1.getTitle());

        session.setAttribute("assignment", assignment1);

        return "redirect:/createAssignment?courseId=" + courseId;
    }

    @PostMapping("/processAnswer")
    public String processAnswer(@RequestParam("courseId") long courseId,
                                @RequestParam("questionText") String questionText,
                                @RequestParam("answer") String answerText,
                                @RequestParam("correct") boolean correct,
                                HttpSession session,
                                Model model) {

        // Retrieve the assignment from the session
        Assignment assignment = (Assignment) session.getAttribute("assignment");

        // Check if the assignment exists in the session
        if (assignment == null) {
            // Handle error or redirect to an appropriate page
        }

        // Find the question in the assignment by its text
        Question question = null; // Initialize to null

        for (Question q : assignment.getQuestions()){
            if (q.getQuestion().equals(questionText)) {
                question = q;
                break; // Exit loop once the question is found
            }
        }

        // Check if the question exists
        if (question == null) {
            // Handle error or redirect to an appropriate page
        }

        // Create a new answer with the submitted answer text and correctness
        Answer answer = new Answer(answerText, correct);

        // Add the answer to the question
        question.addAnswer(answer);

        // Update the assignment in the session
        session.setAttribute("assignment", assignment);

        // Redirect to the createAssignment page with the courseId parameter
        return "redirect:/createAssignment?courseId=" + courseId;
    }

    @PostMapping("/deleteAssignment")
    public String deleteAssignment(@RequestParam("assignmentId") long assignmentId,
                                   @RequestParam("courseId") long courseId){

        Assignment tempAssignment = instructorService.getAssignmentById(assignmentId);

        instructorService.deleteAssignment(tempAssignment);

        return "redirect:/viewCourseAssignments?id=" + courseId;

    }

    @GetMapping("/courseStudents")
    public String courseStudents(@RequestParam("id") long courseId,
                                 Model theModel){

        Course course = instructorService.getCourseById(courseId);

        theModel.addAttribute("course", course);

        return "course-students";


    }
}
