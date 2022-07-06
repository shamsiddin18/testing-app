package com.testapp.user.service;

import com.testapp.user.model.UserModel;
import com.testapp.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

class UserServiceTest {
    @Test
    public void when_service_saves_user_successfully() {
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        UserModel userModel = new UserModel();
        userModel.setPassword("foo");

        Mockito.when(passwordEncoder.encode("foo")).thenReturn("encoded-password");
        Mockito.when(userRepository.save(userModel)).thenReturn(userModel);
        UserService userService = new UserService(userRepository, passwordEncoder);
        UserModel result = userService.registerUser(userModel);
        assertEquals(result, userModel);
        Mockito.verify(userRepository).save(result);
        Mockito.verify(passwordEncoder).encode("foo");
    }

    @Test
    public void when_user_is_not_found_it_should_throw_exception() {
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);

        UserModel userModel = new UserModel();
        userModel.setLogin("foo");

        Mockito.when(userRepository.findFirstByLogin("foo")).thenReturn(Optional.empty());
        UserService userService = new UserService(userRepository, passwordEncoder);
        assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername("foo");
        });
        Mockito.verify(userRepository).findFirstByLogin("foo");
    }

    @Test
    public void when_user_is_found_it_should_return_user() {
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);

        UserModel userModel = new UserModel();
        userModel.setLogin("foo");

        Mockito.when(userRepository.findFirstByLogin("foo")).thenReturn(Optional.of(userModel));
        UserService userService = new UserService(userRepository, passwordEncoder);
        UserDetails result = userService.loadUserByUsername("foo");

        assertEquals(result, userModel);
        Mockito.verify(userRepository).findFirstByLogin("foo");
    }
}
