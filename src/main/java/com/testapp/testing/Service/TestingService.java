package com.testapp.testing.Service;

import com.testapp.answer.model.Answer;
import com.testapp.testing.model.Testing;
import com.testapp.testing.model.TestingQuestion;
import com.testapp.testing.repository.TestingQuestionRepository;
import com.testapp.testing.repository.TestingRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Service
public class TestingService {
    private TestingRepository testRepository;

    private TestingQuestionRepository testingQuestionRepository;


    public TestingService(TestingRepository testRepository,
       TestingQuestionRepository testingQuestionRepository) {
        this.testRepository = testRepository;
        this.testingQuestionRepository=testingQuestionRepository;
    }

    public void save(Testing model) {
        testRepository.save(model);
    }

    public Testing find(Integer id) {
        return testRepository.findById(id).orElse(null);
    }

    public void saveAnswer(TestingQuestion model){
        testingQuestionRepository.save(model);
    }
}
