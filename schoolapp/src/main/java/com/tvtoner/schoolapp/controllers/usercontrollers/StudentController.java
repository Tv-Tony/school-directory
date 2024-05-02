package com.tvtoner.schoolapp.controllers.usercontrollers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StudentController {


    @GetMapping("/studentHomePage")
    public String instructorHomePage(){
        return "users/student-home-page";
    }
}
