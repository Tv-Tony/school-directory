package com.tvtoner.schoolapp.controllers;

import com.tvtoner.schoolapp.security.entity.User;
import com.tvtoner.schoolapp.security.service.UserService;
import com.tvtoner.schoolapp.security.user.WebUser;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@Controller
@RequestMapping("/register")
public class StudentRegistrationController {

    private Logger logger = Logger.getLogger(getClass().getName());

    private UserService userService;

    @Autowired
    public StudentRegistrationController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Initializes a binder for web data, trimming strings to remove leading and trailing whitespace.
     *
     * @param dataBinder The web data binder to initialize
     */
    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {

        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);

        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    /**
     * Displays the student registration form.
     *
     * @param theModel The model to add attributes
     * @return The view for the student registration form
     */
    @GetMapping("/showStudentRegistrationForm")
    public String showMyLoginPage(Model theModel) {

        theModel.addAttribute("webUser", new WebUser());

        return "register/student-registration/registration-form";
    }

    /**
     * Processes the submitted student registration form and saves the new student object to db
     *
     * @param theWebUser The submitted web user object
     * @param theBindingResult The result of the binding/validation process
     * @param session The HTTP session
     * @param theModel The model to add attributes
     * @param authentication The authentication object representing the current user's authentication
     * @return The view to redirect based on the authentication status
     */
    @PostMapping("/processStudentRegistrationForm")
    public String processStudentRegistrationForm(
            @Valid @ModelAttribute("webUser") WebUser theWebUser,
            BindingResult theBindingResult,
            HttpSession session, Model theModel, Authentication authentication) {

        String userName = theWebUser.getUserName();
        logger.info("Processing registration form for: " + userName);

        // form validation
        if (theBindingResult.hasErrors()){
            return "register/student-registration/registration-form";
        }

        // check the database if user already exists
        User existing = userService.findByUserName(userName);
        if (existing != null){
            theModel.addAttribute("webUser", new WebUser());
            theModel.addAttribute("registrationError", "User name already exists.");

            logger.warning("User name already exists.");
            return "register/student-registration/registration-form";
        }

        // create user account and store in the databse
        userService.saveStudent(theWebUser);

        logger.info("Successfully created user: " + userName);

//        // place user in the web http session for later use
//        session.setAttribute("user", theWebUser);

        // Check if the authenticated user has admin authority
        if (authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/adminHomePage";
        } else {
            return "register/registration-confirmation";
        }
    }
}
