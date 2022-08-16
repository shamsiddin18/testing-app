package com.testapp.answer.repository;

import com.testapp.answer.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {

    List<Answer> findByQuestionId(Integer questionId);

    Answer findByText(String text);

}
