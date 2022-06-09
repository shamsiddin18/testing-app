package com.testapp.question.repository;

import com.testapp.question.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    List<Question> findBySubjectId(Integer subject_id);
    Optional <Question> findByTextAndSubjectId(String text, Integer subject_id);

}
