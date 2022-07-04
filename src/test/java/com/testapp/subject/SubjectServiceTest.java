package com.testapp.subject;

import com.testapp.question.repository.QuestionRepository;
import com.testapp.subject.model.Subject;
import com.testapp.subject.repository.SubjectRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class SubjectServiceTest {

    @Test
    public void create_subject(){
        SubjectRepository subjectRepository = Mockito.mock(SubjectRepository.class);
        Subject subject = new Subject();
        Mockito.when(subjectRepository.save(subject)).thenReturn(subject);
        SubjectService subjectService = new SubjectService(subjectRepository);
        Subject result = subjectService.creatSubject(subject);
        assertEquals(result,subject);
        Mockito.verify(subjectRepository).save(result);
    }
}