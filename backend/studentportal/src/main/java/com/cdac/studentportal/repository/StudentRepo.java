

package com.cdac.studentportal.repository;

import com.cdac.studentportal.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StudentRepo extends JpaRepository<Student, Long> {
    Optional<Student> findByUsernameAndPassword(String username, String password);
}

