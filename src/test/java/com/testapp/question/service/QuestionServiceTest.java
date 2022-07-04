package com.testapp.question.service;

import com.testapp.question.model.Question;
import com.testapp.question.repository.QuestionRepository;
import com.testapp.subject.model.Subject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {
    @Mock
    private QuestionRepository questionRepository;
    private QuestionService underTest;
    private  AutoCloseable autoCloseable;

    @BeforeEach
    void setUp(){
       autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new QuestionService(questionRepository);
    }
    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void canSave() {
        String text ="go go";
        Subject subject = new Subject();
//      subject.setId(1);
        Question question = new Question();
        question.setText(text);
        question.setSubject(subject);
        underTest.save(question);

//      verify(questionRepository).save();
        boolean expected = underTest.equals(question);

        assertThat(expected).isTrue();

    }
}