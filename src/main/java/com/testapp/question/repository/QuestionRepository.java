package com.testapp.question.repository;

import com.testapp.question.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.Set;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    Set<Question> findBySubjectId(Integer subjectId);
    Optional<Question> findByTextAndSubjectId(String text, Integer subjectId);
}
