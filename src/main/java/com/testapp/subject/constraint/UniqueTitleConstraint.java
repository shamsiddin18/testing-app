package com.testapp.subject.constraint;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueTitleValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueTitleConstraint {
    String message() default "Login should be unique";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
