package com.example.courseregistration.service;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.courseregistration.dao.UserDAO;
import com.example.courseregistration.model.Professor;
import com.example.courseregistration.model.Student;
import com.example.courseregistration.model.User;

@Service
public class UserService {
	
	private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
	
    public void saveUser(User user) {
		userDAO.saveUser(user);
	}
    
    public void updateUser(User user) {
    	userDAO.updateUser(user);
    }
    
    public User getUserById(Long id) {
    	return userDAO.getUserById(id);
    }
    
    public User getUserByUsername(String username) {
    	return userDAO.getUserByUsername(username);
    }
    
    public List<User> getAllStudents() {
        List<User> allUsers = userDAO.getAllUsers();

        return allUsers.stream()
                .filter(user -> "STUDENT".equals(user.getRole().name()))
                .collect(Collectors.toList());
    }

    public List<User> getAllProfessors() {
        List<User> allUsers = userDAO.getAllUsers();

        return allUsers.stream()
                .filter(user -> "PROFESSOR".equals(user.getRole().name()))
                .collect(Collectors.toList());
    }
    
    public void loadStudentData() {
    	FileService<Student> fileUtil = new FileService<>(Student.class.getSimpleName());
		List<String> STUDENT_DATA = fileUtil.readData("src/main/resources/STUDENT_DATA.txt");
		
		for(String item: STUDENT_DATA) {
			StudentFactoryLazySingleton studentFactory = StudentFactoryLazySingleton.getInstance();
			userDAO.saveUser(studentFactory.createUser(item));
		}
    }
    
    public void loadProfessorData() {
    	FileService<Professor> fileUtil = new FileService<>(Professor.class.getSimpleName());
		List<String> PROFESSOR_DATA = fileUtil.readData("src/main/resources/PROFESSOR_DATA.txt");
		
		for(String item: PROFESSOR_DATA) {
			ProfessorFactoryLazySingleton professorFactory = ProfessorFactoryLazySingleton.getInstance();
			userDAO.saveUser(professorFactory.createUser(item));
		}
    }
}