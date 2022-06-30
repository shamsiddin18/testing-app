package com.testapp.user.constraint;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueLoginValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueLoginConstraint {
    String message() default "Login should be unique";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
