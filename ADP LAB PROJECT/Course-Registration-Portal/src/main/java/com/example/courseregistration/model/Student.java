package com.example.courseregistration.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;

@Entity
public class Student extends User {

	 private String firstName;
	 private String lastName;
	 private String email;
	 private String studentId;
	 private String major;
	 private int yearOfStudy;
	 
	 @ManyToMany(fetch = FetchType.EAGER)
	    @JoinTable(
	            name = "student_course_mapping",
	            joinColumns = @JoinColumn(name = "student_id"),
	            inverseJoinColumns = @JoinColumn(name = "course_id")
	    )
	 private Set<Course> courses = new HashSet<>();
	 
	public Student() {
	}
	
	public Student(String username, String password, UserRole role,  String firstName, String lastName, String email, String studentId, String major, int yearOfStudy) {
		super(username, password, role);
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.studentId = studentId;
		this.major = major;
		this.yearOfStudy = yearOfStudy;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getStudentId() {
		return studentId;
	}
	
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	
	public String getMajor() {
		return major;
	}
	
	public void setMajor(String major) {
		this.major = major;
	}
	
	public int getYearOfStudy() {
		return yearOfStudy;
	}
	
	public void setYearOfStudy(int yearOfStudy) {
		this.yearOfStudy = yearOfStudy;
	}

	public Set<Course> getCourses() {
		return courses;
	}

	public void setCourses(Set<Course> courses) {
		this.courses = courses;
	}
	 
	
	 
}
