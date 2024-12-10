package com.example.courseregistration.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.courseregistration.model.User;
import com.example.courseregistration.model.UserRole;
import com.example.courseregistration.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/")
public class UserController{
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    public String showLogin() {
        return "redirect:/login";
    }
    
    @GetMapping("/login")
    public String showLoginForm(Model model) {
    	if(userService.getAllStudents().size() == 0) {
    		userService.loadStudentData();
    	}
    	if(userService.getAllProfessors().size() == 0){
    		userService.loadProfessorData();
    	}
    	model.addAttribute("type", "login");
        return "login";
    }
    
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        User user = userService.getUserByUsername(username);
	        if (user != null) {
	        	if(user.getPassword().equals(password)) {
		            request.getSession().setAttribute("user", user);
		            if(user.getRole() == UserRole.STUDENT) {
		            	return "redirect:/student-dashboard";
		            } else {
		            	return "redirect:/professor-dashboard";
		            }
	        	} else {
	        		redirectAttributes.addFlashAttribute("error", "Invalid credentials");
	                return "redirect:/login";
	        	}
	        } else {
	            redirectAttributes.addFlashAttribute("error", "User not found");
	            return "redirect:/login";
	        }
	    
    }

    
    @GetMapping("/logout")
    public String logoutUser(HttpServletRequest request) {
    	request.getSession().invalidate();
        return "redirect:/login";
    }
    
    @GetMapping("/student-dashboard")
    public String showUserDashboard(@ModelAttribute("currentUser") User user, Model model) {
    	if(user == null) {
    		return "redirect:/login";
    	}
    	model.addAttribute("student", user);
    	model.addAttribute("role", user.getRole().name());
    	return "studentDashboard";
    }
    
    @GetMapping("/professor-dashboard")
    public String showProfessorDashboard(@ModelAttribute("currentUser") User user, Model model) {
    	if(user == null) {
    		return "redirect:/login";
    	}
    	model.addAttribute("professor", user);
    	model.addAttribute("role", user.getRole().name());
    	return "professorDashboard";
    }
    
}

