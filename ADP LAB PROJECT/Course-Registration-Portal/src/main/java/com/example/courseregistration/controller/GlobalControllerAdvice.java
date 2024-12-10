package com.example.courseregistration.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.courseregistration.model.User;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute("currentUser")
    public User getCurrentUser(HttpServletRequest request) {
    	User user = (User) request.getSession().getAttribute("user");
    	if(user != null) {
    		return user;
    	} else {
    		return null;
    	}
    }

}