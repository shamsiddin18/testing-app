package com.testapp.subject.repository;

import com.testapp.subject.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject, Integer> {
    Optional<Subject> findByIdAndTitle(Integer id, String title);

   Optional<Subject> findFirstByTitle(String title);

}
