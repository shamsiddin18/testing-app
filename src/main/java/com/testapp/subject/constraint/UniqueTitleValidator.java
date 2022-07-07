package com.testapp.subject.constraint;

import com.testapp.subject.model.Subject;
import com.testapp.subject.repository.SubjectRepository;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public final class UniqueTitleValidator implements ConstraintValidator<UniqueTitleConstraint, String> {
    private final SubjectRepository subjectRepository;

    public UniqueTitleValidator(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    @Override
    public void initialize(UniqueTitleConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null || s.isEmpty()) {
            return true;
        }

        Subject ss = this.subjectRepository.findFirstByTitle(s).orElse(null);
        if (ss == null) {
            return true;
        }

        return false;
    }
}
