package com.example.courseregistration.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;

@Entity
@Table(name = "courses")
public class Course implements Comparable<Course> {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String courseName;
    private String courseCode;
    
    @Column(name="course_description")
    private String courseDesciption;
    
    private int capacity;
    
    @ManyToOne
    @JoinColumn(name = "professor_id") 
    private Professor professor;
    
    @ManyToMany(mappedBy = "courses", fetch = FetchType.EAGER)
    private Set<Student> students = new HashSet<>();
    
    @Column(name = "lesson_plan_path")
    private String lessonPlanPath; // Add field for the file path
    
	public Course() {
		
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getCourseName() {
		return courseName;
	}
	
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	
	public String getCourseCode() {
		return courseCode;
	}
	
	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	public String getCourseDesciption() {
		return courseDesciption;
	}

	public void setCourseDesciption(String courseDesciption) {
		this.courseDesciption = courseDesciption;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public Professor getProfessor() {
		return professor;
	}

	public void setProfessor(Professor professor) {
		this.professor = professor;
	}

	public Set<Student> getStudents() {
		return students;
	}

	public void setStudents(Set<Student> students) {
		this.students = students;
	}
	
	 // Getter and Setter for lessonPlanPath
    public String getLessonPlanPath() {
        return lessonPlanPath;
    }

    public void setLessonPlanPath(String lessonPlanPath) {
        this.lessonPlanPath = lessonPlanPath;
    }

	@Override
    public int compareTo(Course otherCourse) {
        return this.getCourseName().toLowerCase().compareTo(otherCourse.getCourseName().toLowerCase());
    }
	
}
