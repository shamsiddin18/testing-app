package com.testapp.testing.constraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AnswerValidator implements ConstraintValidator<AnswerConstraint,String> {
    @Override
    public void initialize(AnswerConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null || s.isEmpty()) {
            return true;
        }
      return false;
    }
}
