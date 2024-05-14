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
     *  This method gets the user through authentication and sends both the attributes to be processed in the template, we can
     *  get the instructors information to the banner with the authentication instance, and the user can display properties
     *  of the instructor because instructor has a foreign key reference to the user table. For example user.instructor.firstName()
     * @param theModel model to add attributes
     * @param authentication authentication to grab the user
     * @return to Instructor Home Page
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

    /**
     * shows current assignments in the class with the option to add assignments or delete them
     * @param courseID course id
     * @param theModel model to add attributes
     * @return course-assignments template
     */
    @GetMapping("/viewCourseAssignments")
    public String viewCourseAssignments(@RequestParam("id") long courseID, Model theModel){

        theModel.addAttribute("course", instructorService.getCourseById(courseID));

        return "course-assignments";

    }

    /**
     * This method checks if there is an assignment in the session. If not, it adds one, the method then adds a course object
     * to the model as well as a new question instance so the user can create a new question. The point of adding the session
     * to the user is because the method will have to go back and forth between multiple other methods that individually
     * add questions and answers, so that way over time we build up an assignment object in one piece, and we can save it
     * to the database in one go, instead of saving pieces of the assignment in the database every time one of the methods is called
     * @param courseId course id
     * @param session session attribute to store information for a longer lifecycle than sending through models
     * @param model model to add attribute
     * @return to create-assignment template
     */
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

        model.addAttribute("course", instructorService.getCourseById(courseId));
        model.addAttribute("question", new Question());

        return "create-assignment";
    }

    /**
     * This method processes the assignment, it gets the whole assignment in one piece from the session and saves it to
     * the db.
     * @param courseId course id
     * @param title assignment title
     * @param session session attribute with the new assignment object
     * @return to instructor home page
     */
    @PostMapping("/processAssignment")
    public String processAssignment(@RequestParam("courseId") long courseId,
                                    @RequestParam("title") String title,
                                    HttpSession session) {

        Assignment assignment = (Assignment) session.getAttribute("assignment");
        assignment.setTitle(title);

        Course course = instructorService.getCourseById(courseId);

        // Assign the course to the assignment
        assignment.setCourse(course);

        // For all questions, we set the assignment, embedded inside is another for loop, for each question we pull the
        // answers from the question and for all answers we set the question. This way establishing the foreign key reference
        // parameter in the db
        for (Question e : assignment.getQuestions()){
            e.setAssignment(assignment);
            for (Answer a : e.getAnswers()){
                a.setQuestion(e);
            }
        }

        instructorService.saveAssignment(assignment);

        session.removeAttribute("assignment");


        return "redirect:/instructorHomePage"; // Redirect to the instructor home page
    }

    /**
     * This method grabs the assigment from the session attribute, checks for null, and then adds the new question to it,
     * then it is placed back inside the session
     * @param courseId course id
     * @param question question in string form
     * @param session to access session attributes
     * @param title title of the assignment
     * @return /createAssignment endpoint with the courseId
     */
    @PostMapping("/processQuestion")
    public String processQuestion(@RequestParam("courseId") long courseId,
                                  @RequestParam("question") String question,
                                  HttpSession session,
                                  @RequestParam("assignmentTitle") String title) {

        Assignment assignment1 = (Assignment) session.getAttribute("assignment");
        if (assignment1 == null) {
            return "access-denied";
        }

        assignment1.addQuestion(new Question(question));

        assignment1.setTitle(title);

        System.out.println(assignment1.getTitle());

        session.setAttribute("assignment", assignment1);

        return "redirect:/createAssignment?courseId=" + courseId;
    }

    /**
     * In this method first we get the assignment object from the session attribute. Then using a for loop, we get the question
     * object by checking if the questionText matches the question text in any of the assignment.questions(). After
     *  this, we assign a new answer and populate the answer with the answer and boolean that describes if it is correct or
     * not and set it to the question of which is set to the assignment and then save the updated assignment attribute back into
     * the session attributes.
     * @param courseId course id
     * @param questionText the question in string form
     * @param answerText answer in string form
     * @param correct correctness of the answer in boolean form
     * @param session to access session attributes
     * @return /createAssignment endpoint with the courseId
     */
    @PostMapping("/processAnswer")
    public String processAnswer(@RequestParam("courseId") long courseId,
                                @RequestParam("questionText") String questionText,
                                @RequestParam("answer") String answerText,
                                @RequestParam("correct") boolean correct,
                                HttpSession session) {

        // Retrieve the assignment from the session
        Assignment assignment = (Assignment) session.getAttribute("assignment");

        // Check if the assignment exists in the session
        if (assignment == null) {
            return "access-denied";
        }

        // Find the question in the assignment by its text
        Question question = null;

        for (Question q : assignment.getQuestions()){
            if (q.getQuestion().equals(questionText)) {
                question = q;
                break; // Exit loop once the question is found
            }
        }

        // Check if the question exists
        if (question == null) {
            return "access-denied";
        }

        // Create a new answer with the submitted answer text and check if its correct
        Answer answer = new Answer(answerText, correct);

        // Add the answer to the question
        question.addAnswer(answer);

        // Update the assignment in the session
        session.setAttribute("assignment", assignment);

        // Redirect to the createAssignment page with the courseId parameter
        return "redirect:/createAssignment?courseId=" + courseId;
    }

    /**
     * method deletes the assignment by the assignment ID
     * @param assignmentId assignment ID
     * @param courseId course ID
     * @return returns to the assignments view for the specific course
     */
    @PostMapping("/deleteAssignment")
    public String deleteAssignment(@RequestParam("assignmentId") long assignmentId,
                                   @RequestParam("courseId") long courseId){

        Assignment tempAssignment = instructorService.getAssignmentById(assignmentId);

        instructorService.deleteAssignment(tempAssignment);

        return "redirect:/viewCourseAssignments?id=" + courseId;

    }

    /**
     * This method gets the course by the id, and in the template course.students() is called to show all the students
     * in the course
     * @param courseId course ID
     * @param theModel model to add attributes
     * @return return 'course-students' template
     */
    @GetMapping("/courseStudents")
    public String courseStudents(@RequestParam("id") long courseId,
                                 Model theModel){

        Course course = instructorService.getCourseById(courseId);

        theModel.addAttribute("course", course);

        return "course-students";


    }
}
