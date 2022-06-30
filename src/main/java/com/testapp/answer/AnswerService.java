package com.testapp.answer;

import com.testapp.answer.model.Answer;
import com.testapp.answer.repository.AnswerRepository;
import org.springframework.stereotype.Service;

@Service
public class AnswerService {
    private AnswerRepository answerRepository;

    public AnswerService(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    public Answer create(Answer answer) {
        return answerRepository.save(answer);
    }
}
