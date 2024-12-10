package com.example.courseregistration.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.courseregistration.dao.CourseDAO;
import com.example.courseregistration.model.Course;

@Service
public class CourseService {
	private final CourseDAO courseDAO;

    public CourseService(CourseDAO courseDAO) {
        this.courseDAO = courseDAO;
    }
    
    public void saveCourse(Course course) {
    	courseDAO.saveCourse(course);
    }
    
	public List<Course> getAllCourses() {
		return courseDAO.getAllCourses();
	}
	
    public void updateCourse(Course course) {
    	courseDAO.updateCourse(course);
    }
    
    public void deleteCourse(Long id) {
    	courseDAO.deleteCourse(id);
    }
    
    public Course getCourseById(Long id) {
    	return courseDAO.getCourseById(id);
    }
}
