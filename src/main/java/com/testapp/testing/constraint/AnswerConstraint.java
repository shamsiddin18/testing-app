package com.testapp.testing.constraint;

import com.testapp.subject.constraint.UniqueTitleValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AnswerValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AnswerConstraint {
    String message() default "Answer should not be empty";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
