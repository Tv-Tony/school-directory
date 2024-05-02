package com.tvtoner.schoolapp.security.security;

import com.tvtoner.schoolapp.security.entity.User;
import com.tvtoner.schoolapp.security.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private UserService userService;

    public CustomAuthenticationSuccessHandler(UserService theUserService) {
        userService = theUserService;
    }

//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
//            throws IOException, ServletException {
//
//        System.out.println("In customAuthenticationSuccessHandler");
//
//        String userName = authentication.getName();
//
//        System.out.println("userName=" + userName);
//
//        User theUser = userService.findByUserName(userName);
//
//        // now place in the session
//        HttpSession session = request.getSession();
//        session.setAttribute("user", theUser);
//
//        // forward to home page
//        response.sendRedirect(request.getContextPath() + "/");
//    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        System.out.println("In customAuthenticationSuccessHandler");

        // Retrieve the authenticated user's username
        String userName = authentication.getName();

        // Retrieve the user details from the userService
        User theUser = userService.findByUserName(userName);

        // Place the user details in the session
        HttpSession session = request.getSession();
        session.setAttribute("user", theUser);

        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals("ROLE_ADMIN")) {
                response.sendRedirect("/adminHomePage");
                return;
            } else if (authority.getAuthority().equals("ROLE_INSTRUCTOR")) {
                response.sendRedirect("/instructorHomePage");
                return;
            } else if (authority.getAuthority().equals("ROLE_STUDENT")) {
                response.sendRedirect("/studentHomePage");
                return;
            }
        }

        // If no specific role is found, redirect to a default page
        response.sendRedirect("/");
    }
}


