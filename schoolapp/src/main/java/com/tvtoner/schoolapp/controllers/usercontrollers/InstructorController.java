package com.tvtoner.schoolapp.controllers.usercontrollers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InstructorController {

    @GetMapping("/instructorHomePage")
    public String instructorHomePage(){
        return "users/instructor-home-page";
    }
}
