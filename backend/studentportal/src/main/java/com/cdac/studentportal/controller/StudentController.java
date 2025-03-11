package com.cdac.studentportal.controller;

import com.cdac.studentportal.model.Student;
import com.cdac.studentportal.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    
    @GetMapping("/students")
    public List<Student> getStudents() {
        return studentService.getAllStudents();
    }

    
    @PostMapping("/students/register")
    public ResponseEntity<?> registerStudent(@RequestBody Map<String, String> payload) {
        String name = payload.get("name");
        String course = payload.get("course");

        if (name == null || name.length() < 5) {
            return ResponseEntity.badRequest().body("Name must be at least 5 characters long.");
        }

        Student newStudent = studentService.registerStudent(name, course);
        return ResponseEntity.ok(newStudent);
    }

    
    @DeleteMapping("/students/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
        if (!studentService.getStudentById(id).isPresent()) {
            return ResponseEntity.status(404).body("Student not found");
        }
        studentService.deleteStudent(id);
        return ResponseEntity.ok().body("Student deleted successfully");
    }

   
    @PutMapping("/students/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String newName = payload.get("name");
        String newCourse = payload.get("course");

        if (newName == null || newName.length() < 5) {
            return ResponseEntity.badRequest().body("Name must be at least 5 characters long.");
        }

        try {
            
            Student updatedStudent = studentService.updateStudent(id, newName, newCourse);
            return ResponseEntity.ok(updatedStudent);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

   
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        if ("employee".equals(username) && "admin".equals(password)) {
            Map<String, String> response = new HashMap<>();
            response.put("role", "employee");
            return ResponseEntity.ok(response);
        }

        Optional<Student> student = studentService.authenticateStudent(username, password);
        if (student.isPresent()) {
            Map<String, String> response = new HashMap<>();
            response.put("role", "student");
            response.put("name", student.get().getName());
            response.put("course", student.get().getCourse());
            response.put("username", student.get().getUsername());
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(401).build();
    }
}
