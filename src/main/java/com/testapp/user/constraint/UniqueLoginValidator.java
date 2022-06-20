package com.testapp.user.constraint;

import com.testapp.user.model.UserModel;
import com.testapp.user.repository.UserRepository;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueLoginValidator implements ConstraintValidator<UniqueLoginConstraint, String> {
    private final UserRepository repository;

    public UniqueLoginValidator(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public void initialize(UniqueLoginConstraint constraint) {}

    @Override
    public boolean isValid(String field, ConstraintValidatorContext context) {
        if (field == null || field.isEmpty()) {
            return true;
        }

        UserModel user = this.repository.findFirstByLogin(field).orElse(null);
        if (user == null) {
            return true;
        }



        return false;
    }
}
