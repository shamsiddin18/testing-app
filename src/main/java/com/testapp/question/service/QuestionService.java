package com.testapp.question.service;

import com.testapp.question.model.Question;
import com.testapp.question.repository.QuestionRepository;
import org.springframework.stereotype.Service;

@Service
public class QuestionService {
    private QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public Question save(Question question) {
        return questionRepository.save(question);
    }
}
