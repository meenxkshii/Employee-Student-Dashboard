package com.cdac.studentportal.service;

import com.cdac.studentportal.model.Student;
import com.cdac.studentportal.repository.StudentRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    private final StudentRepo studentRepo;

    public StudentService(StudentRepo studentRepository) {
        this.studentRepo = studentRepository;
    }

 
    public List<Student> getAllStudents() {
        return studentRepo.findAll();
    }

  
    public Student registerStudent(String name, String course) {
        if (name == null || name.length() < 5) {
            throw new IllegalArgumentException("Name must be at least 5 characters long.");
        }

        Student student = new Student();
        student.setName(name);
        student.setCourse(course);
        student.generateCredentials(); 

        return studentRepo.save(student);
    }

   
    public void deleteStudent(Long id) {
        studentRepo.deleteById(id);
    }

    
    public Optional<Student> getStudentById(Long id) {
        return studentRepo.findById(id);
    }

   
    public void saveStudent(Student student) {
        studentRepo.save(student);
    }

    public Optional<Student> authenticateStudent(String username, String password) {
        return studentRepo.findByUsernameAndPassword(username, password);
    }

    
    public Student updateStudent(Long id, String newName, String newCourse) {
        Optional<Student> studentOptional = studentRepo.findById(id);

        if (!studentOptional.isPresent()) {
            throw new IllegalArgumentException("Student not found");
        }

        Student student = studentOptional.get();
        student.setName(newName);
        student.setCourse(newCourse);

        
        student.regenerateCredentials();

        return studentRepo.save(student);
    }
}
