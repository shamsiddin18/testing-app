package com.testapp.question.service;

import com.testapp.question.model.Question;
import com.testapp.question.repository.QuestionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class QuestionServiceTest {

  @Test
  public void when_save_return_object() {
    QuestionRepository questionRepository = Mockito.mock(QuestionRepository.class);
    Question question = new Question();
    Mockito.when(questionRepository.save(question)).thenReturn(question);
    QuestionService questionService = new QuestionService(questionRepository);
    Question result = questionService.save(question);
    assertEquals(result, question);
    Mockito.verify(questionRepository).save(result);
  }
}
