package com.example.courseregistration.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;

@Entity
public class Professor extends User {
	 private String firstName;
	 private String lastName;
	 private String email;
	 private String employeeId;
	 private String department;
	 
	 @ManyToMany(fetch = FetchType.EAGER)
	    @JoinTable(
	            name = "professor_course_mapping",
	            joinColumns = @JoinColumn(name = "professor_id"),
	            inverseJoinColumns = @JoinColumn(name = "course_id")
	    )
	 private Set<Course> courses = new HashSet<>();
	 
	 public Professor() {
	 }
	 
	public Professor(String username, String password, UserRole role, String firstName, String lastName, String email, String employeeId, String department) {
		super(username, password, role);
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.employeeId = employeeId;
		this.department = department;
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
	
	public String getEmployeeId() {
		return employeeId;
	}
	
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	
	public String getDepartment() {
		return department;
	}
	
	public void setDepartment(String department) {
		this.department = department;
	}

	public Set<Course> getCourses() {
		return courses;
	}

	public void setCourses(Set<Course> courses) {
		this.courses = courses;
	} 
	
	
}
