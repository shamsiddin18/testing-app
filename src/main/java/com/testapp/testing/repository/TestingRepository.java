package com.testapp.testing.repository;

import com.testapp.testing.model.Testing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface TestingRepository extends JpaRepository<Testing, Integer> {
   Set<Testing> findByQuestions(String questions);
}
