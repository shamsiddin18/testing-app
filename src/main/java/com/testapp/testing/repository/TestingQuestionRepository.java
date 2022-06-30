package com.testapp.testing.repository;

import com.testapp.testing.model.TestingQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestingQuestionRepository extends JpaRepository<TestingQuestion, Integer> {
}
