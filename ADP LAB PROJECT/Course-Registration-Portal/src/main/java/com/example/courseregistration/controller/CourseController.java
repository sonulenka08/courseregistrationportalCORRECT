// CourseController.java
package com.example.courseregistration.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.courseregistration.model.Course;
import com.example.courseregistration.model.Professor;
import com.example.courseregistration.model.Student;
import com.example.courseregistration.model.User;
import com.example.courseregistration.service.CourseService;
import com.example.courseregistration.service.UserService;

@Controller
@RequestMapping("/course")
public class CourseController {
    private final CourseService courseService;
    private final UserService userService;
    private final String UPLOAD_DIRECTORY = "uploads/lesson-plans/";

    @Autowired
    public CourseController(CourseService courseService, UserService userService) {
        this.courseService = courseService;
        this.userService = userService;
        try {
            Files.createDirectories(Paths.get(UPLOAD_DIRECTORY));
        } catch (IOException e) {
            System.err.println("Error creating upload directory: " + e.getMessage());
        }
    }

    @GetMapping
    public String showCourses(@ModelAttribute("currentUser") User user, Model model) {
        if (user == null) {
            return "redirect:/login";
        }
        List<Course> courses = courseService.getAllCourses();
        Collections.sort(courses);
        model.addAttribute("role", user.getRole().name());
        model.addAttribute("courseList", courses);
        return "course";
    }

    @GetMapping("/add")
    public String showAddCoursesForm(@ModelAttribute("currentUser") User user, Model model) {
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("role", user.getRole().name());
        model.addAttribute("course", new Course());
        model.addAttribute("editMode", false);
        return "addCourse";
    }

    @PostMapping("/add")
    public String addCourse(@ModelAttribute("currentUser") User user, @ModelAttribute("course") Course course, @RequestParam("lessonPlan") MultipartFile lessonPlan, Model model, RedirectAttributes redirectAttributes) {
        if (user == null) {
            return "redirect:/login";
        }
        try {
            handleLessonPlanUpload(course, lessonPlan, model);
            course.setProfessor((Professor) user);
            courseService.saveCourse(course);
            Professor professor = (Professor) user;
            professor.getCourses().add(course);
            userService.saveUser(professor);
            redirectAttributes.addFlashAttribute("success", "Course added successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error adding course: " + e.getMessage());
        }
        return "redirect:/course";
    }

    @GetMapping("/search")
    public String searchCourses(@ModelAttribute("currentUser") User user, @RequestParam(name = "keyword", required = false) String keyword, Model model) {
        if (user == null) {
            return "redirect:/login";
        }
        List<Course> filteredCourses;
        if (keyword != null && !keyword.isEmpty()) {
            filteredCourses = courseService.getAllCourses().stream().filter(course -> course.getCourseName().toLowerCase().contains(keyword.toLowerCase()) || course.getCourseCode().toLowerCase().contains(keyword.toLowerCase())).collect(Collectors.toList());
        } else {
            filteredCourses = courseService.getAllCourses();
        }
        model.addAttribute("courseList", filteredCourses);
        model.addAttribute("role", user.getRole().name());
        return "course";
    }

    @PostMapping("/edit")
    public String showEditCourseForm(@ModelAttribute("currentUser") User user, @RequestParam("courseId") Long id, Model model) {
        if (user == null) {
            return "redirect:/login";
        }
        Course course = courseService.getCourseById(id);
        if (course != null) {
            model.addAttribute("course", course);
            model.addAttribute("editMode", true);
            return "addCourse";
        }
        return "redirect:/professor-dashboard";
    }

    @PostMapping("/editSave")
    public String editCourse(@ModelAttribute("currentUser") User user, @ModelAttribute Course course, @RequestParam("lessonPlan") MultipartFile lessonPlan, Model model, RedirectAttributes redirectAttributes) {
        if (user == null) {
            return "redirect:/login";
        }
        Course existingCourse = courseService.getCourseById(course.getId());
        if (existingCourse == null) {
            redirectAttributes.addFlashAttribute("error", "Course not found");
            return "redirect:/professor-dashboard";
        }
        try {
            course.setProfessor(existingCourse.getProfessor());
            handleLessonPlanUpload(course, lessonPlan, model);
            courseService.updateCourse(course);
            redirectAttributes.addFlashAttribute("success", "Course updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating course: " + e.getMessage());
        }
        return "redirect:/professor-dashboard";
    }

    @GetMapping("/details")
    public String showCourseRegisterForm(@ModelAttribute("currentUser") User user, @RequestParam("courseId") Long id, Model model, RedirectAttributes redirectAttributes) {
        if (user == null) {
            return "redirect:/login";
        }
        Course course = courseService.getCourseById(id);
        if (course == null) {
            redirectAttributes.addFlashAttribute("error", "Course not found");
            return "redirect:/course";
        }
        model.addAttribute("course", course);
        model.addAttribute("role", user.getRole().name());
        if (user instanceof Student) {
            Student student = (Student) user;
            boolean alreadyRegistered = course.getStudents().stream().anyMatch(s -> s.getId().equals(student.getId()));
            int totalRegistrations = student.getCourses().size();
            boolean maxRegistrationReached = totalRegistrations >= 2;
            int registrationCount = course.getStudents().size();
            int capacity = course.getCapacity();
            boolean hasAvailableSeats = (capacity - registrationCount) > 0;
            if (alreadyRegistered) {
                model.addAttribute("success", "You have registered for this course");
            } else if (maxRegistrationReached) {
                model.addAttribute("error", "You can only register for 2 courses. Withdraw from registered course to register for this");
            } else if (!hasAvailableSeats) {
                model.addAttribute("error", "No seats available. Sorry we are filled!");
            }
            model.addAttribute("registrationAllowed", !alreadyRegistered && !maxRegistrationReached && hasAvailableSeats);
            model.addAttribute("alreadyRegistered", alreadyRegistered);
        }
        return "register";
    }


    @PostMapping("/details") // New POST mapping
    public String showCourseDetailsPost(@ModelAttribute("currentUser") User user, @RequestParam("courseId") Long id, Model model, RedirectAttributes redirectAttributes) {
        if (user == null) {
            return "redirect:/login";
        }
        Course course = courseService.getCourseById(id);
        if (course == null) {
            redirectAttributes.addFlashAttribute("error", "Course not found");
            return "redirect:/course"; 
        }
        model.addAttribute("course", course);
        model.addAttribute("role", user.getRole().name());
        if (user instanceof Student) {
            Student student = (Student) user;
            boolean alreadyRegistered = course.getStudents().contains(student);
            model.addAttribute("alreadyRegistered", alreadyRegistered);
            if (alreadyRegistered) {
                model.addAttribute("success", "You have registered for this course");
            } 
        }
        return "register"; 
    }




    @PostMapping("/register")
    public String registerCourse(@ModelAttribute("currentUser") User user, @RequestParam("courseId") Long id, Model model, RedirectAttributes redirectAttributes) {
        if (user == null) {
            return "redirect:/login";
        }
        Course course = courseService.getCourseById(id);
        if (course == null) {
            redirectAttributes.addFlashAttribute("error", "Course not found");
            return "redirect:/course";
        }
        int registrationCount = course.getStudents().size();
        int capacity = course.getCapacity();
        int availability = capacity - registrationCount;
        if (availability == 0) {
            redirectAttributes.addFlashAttribute("error", "No seats available. Sorry we are filled!");
        } else {
            try {
                Student student = (Student) user;
                student.getCourses().add(course);
                userService.updateUser(student);
                course.getStudents().add(student);
                courseService.updateCourse(course);
                redirectAttributes.addFlashAttribute("success", "Successfully registered for the course");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", "Error registering for course: " + e.getMessage());
            }
        }
        return "redirect:/course/details?courseId=" + id;
    }

    @PostMapping("/withdraw")
    public String withdrawCourse(@ModelAttribute("currentUser") User user, @RequestParam("courseId") Long id, RedirectAttributes redirectAttributes) {
        if (user == null) {
            return "redirect:/login";
        }
        try {
            Course course = courseService.getCourseById(id);
            Student student = (Student) user;
            Course removeCourse = student.getCourses().stream().filter(c -> c.getId().equals(course.getId())).findFirst().orElse(null);
            student.getCourses().remove(removeCourse);
            userService.updateUser(student);
            Student removeStudent = course.getStudents().stream().filter(s -> s.getStudentId().equals(student.getStudentId())).findFirst().orElse(null);
            course.getStudents().remove(removeStudent);
            courseService.updateCourse(course);
            redirectAttributes.addFlashAttribute("success", "Successfully withdrawn from the course");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error withdrawing from course: " + e.getMessage());
        }
        return "redirect:/student-dashboard";
    }

    @PostMapping("/upload-lesson-plan")
    public String uploadLessonPlan(@ModelAttribute("currentUser") User user, @RequestParam("courseId") Long courseId, @RequestParam("lessonPlan") MultipartFile file, RedirectAttributes redirectAttributes) {
        if (user == null) {
            return "redirect:/login";
        }
        try {
            Course course = courseService.getCourseById(courseId);
            if (course == null) {
                redirectAttributes.addFlashAttribute("error", "Course not found");
                return "redirect:/professor-dashboard";
            }
            String fileName = courseId + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIRECTORY, fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            course.setLessonPlanPath("/course/view-lesson-plan/" + courseId);
            courseService.saveCourse(course);
            redirectAttributes.addFlashAttribute("success", "Lesson plan uploaded successfully");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Failed to upload lesson plan: " + e.getMessage());
        }
        return "redirect:/professor-dashboard";
    }

    @GetMapping("/view-lesson-plan/{courseId}")
    public void viewLessonPlan(@ModelAttribute("currentUser") User user, @PathVariable Long courseId, HttpServletResponse response) throws IOException {
        if (user == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        try {
            Course course = courseService.getCourseById(courseId);
            if (course == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Course not found");
                return;
            }
            Path directoryPath = Paths.get(UPLOAD_DIRECTORY);
            List<Path> matchingFiles = Files.list(directoryPath).filter(path -> path.getFileName().toString().startsWith(courseId + "_")).collect(Collectors.toList());
            if (matchingFiles.isEmpty()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Lesson plan not found");
                return;
            }
            Path lessonPlanPath = matchingFiles.stream().max(Path::compareTo).get();
            String contentType = Files.probeContentType(lessonPlanPath);
            response.setContentType(contentType != null ? contentType : "application/octet-stream");
            Files.copy(lessonPlanPath, response.getOutputStream());
        } catch (IOException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error accessing lesson plan");
        }
    }

    private void handleLessonPlanUpload(Course course, MultipartFile lessonPlan, Model model) {
        if (!lessonPlan.isEmpty()) {
            try {
                String fileName = course.getId() + "_" + System.currentTimeMillis() + "_" + lessonPlan.getOriginalFilename();
                Path filePath = Paths.get(UPLOAD_DIRECTORY, fileName);
                Files.copy(lessonPlan.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                course.setLessonPlanPath("/course/view-lesson-plan/" + course.getId());
            } catch (IOException e) {
                model.addAttribute("errorMessage", "Error uploading lesson plan: " + e.getMessage());
            }
        }
    }
}