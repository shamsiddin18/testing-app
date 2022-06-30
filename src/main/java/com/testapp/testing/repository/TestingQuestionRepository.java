package com.testapp.testing.repository;

import com.testapp.testing.model.TestingQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TestingQuestionRepository extends JpaRepository<TestingQuestion, Integer> {

    Optional <TestingQuestion> findByAnswerId();
}
