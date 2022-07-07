package com.testapp.user.constraint;

import com.testapp.user.model.UserModel;
import com.testapp.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class UniqueLoginValidatorTest {
    @Test
    public void when_filed_is_empty_it_should_be_valid() {
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        ConstraintValidatorContext constraintValidator = Mockito.mock(ConstraintValidatorContext.class);
        UniqueLoginValidator uniqueLoginValidator = new UniqueLoginValidator(userRepository);
        assertTrue(uniqueLoginValidator.isValid("", constraintValidator));
    }

    @Test
    public void when_repository_return_null_it_should_be_valid() {
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        ConstraintValidatorContext constraintValidator = Mockito.mock(ConstraintValidatorContext.class);
        UserModel userModel = new UserModel();
        userModel.setLogin("shams");

        Mockito.when(userRepository.findFirstByLogin("shams")).thenReturn(Optional.empty());
        UniqueLoginValidator uniqueLoginValidator = new UniqueLoginValidator(userRepository);
        boolean result = uniqueLoginValidator.isValid(userModel.getLogin(), constraintValidator);
        assertTrue(result);
        Mockito.verify(userRepository).findFirstByLogin("shams");
    }

    @Test
    public void when_repository_return_not_null_it_should_be_false() {
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        ConstraintValidatorContext constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        UserModel userModel = new UserModel();
        userModel.setLogin("shams");

        Mockito.when(userRepository.findFirstByLogin("shams")).thenReturn(Optional.of(userModel));
        UniqueLoginValidator uniqueLoginValidator = new UniqueLoginValidator(userRepository);
        boolean result = uniqueLoginValidator.isValid(userModel.getLogin(), constraintValidatorContext);
        assertFalse(result);
        Mockito.verify(userRepository).findFirstByLogin("shams");
    }
}
