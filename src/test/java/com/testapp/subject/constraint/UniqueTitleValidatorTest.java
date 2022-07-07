package com.testapp.subject.constraint;

import com.testapp.subject.model.Subject;
import com.testapp.subject.repository.SubjectRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class UniqueTitleValidatorTest {

    @Test
    public void when_title_is_empty_it_should_be_valid() {
        SubjectRepository subjectRepository = Mockito.mock(SubjectRepository.class);
        ConstraintValidatorContext constraintValidator = Mockito.mock(ConstraintValidatorContext.class);
        UniqueTitleValidator uniqueTitleValidator = new UniqueTitleValidator(subjectRepository);
        assertTrue(uniqueTitleValidator.isValid("", constraintValidator));
    }

    @Test
    public void when_repository_return_null_it_should_be_valid(){
        SubjectRepository subjectRepository = Mockito.mock(SubjectRepository.class);
        ConstraintValidatorContext constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        Subject subject = new Subject();
        subject.setTitle("math");
        Mockito.when(subjectRepository.findFirstByTitle("math")).thenReturn(Optional.empty());
        UniqueTitleValidator uniqueTitleValidator = new UniqueTitleValidator(subjectRepository);
        assertTrue(uniqueTitleValidator.isValid(subject.getTitle(), constraintValidatorContext));
        Mockito.verify(subjectRepository).findFirstByTitle("math");
    }

    @Test
    public void when_repository_return_not_null_it_should_be_invalid(){
        SubjectRepository subjectRepository = Mockito.mock(SubjectRepository.class);
        ConstraintValidatorContext constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        Subject subject = new Subject();
        subject.setTitle("math");
        Mockito.when(subjectRepository.findFirstByTitle("math")).thenReturn(Optional.of(subject));
        UniqueTitleValidator uniqueTitleValidator = new UniqueTitleValidator(subjectRepository);
        assertFalse(uniqueTitleValidator.isValid(subject.getTitle(), constraintValidatorContext));
        Mockito.verify(subjectRepository).findFirstByTitle("math");
    }
}
