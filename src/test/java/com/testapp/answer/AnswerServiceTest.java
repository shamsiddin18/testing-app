package com.testapp.answer;

import com.testapp.answer.model.Answer;
import com.testapp.answer.repository.AnswerRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class AnswerServiceTest {

    @Test
   public void save_test_answer(){
        AnswerRepository answerRepository = Mockito.mock(AnswerRepository.class);
        Answer answer= new Answer();
        Mockito.when(answerRepository.save(answer)).thenReturn(answer);
        AnswerService answerService = new AnswerService(answerRepository);
        Answer result = answerService.create(answer);
<<<<<<< HEAD
        assertEquals(result, answer);
=======
        assertEquals(result,answer);
>>>>>>> 1120746 (testing answer/question/subject/testing)
        Mockito.verify(answerRepository).save(answer);
    }
}