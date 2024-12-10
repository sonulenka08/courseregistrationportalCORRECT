package com.example.courseregistration.dao;

import java.util.List;

import com.example.courseregistration.model.Course;

public interface CourseDAO {
	public void saveCourse(Course course);
	public List<Course> getAllCourses();
    public void updateCourse(Course course);
    public void deleteCourse(Long id);
    public Course getCourseById(Long id);
}
