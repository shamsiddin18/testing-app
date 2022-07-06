package com.testapp.user.service;

import com.testapp.user.model.UserModel;
import com.testapp.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Null;
import org.springframework.context.ApplicationContextException;
import org.springframework.lang.Nullable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
   private PasswordEncoder passwordEncoder;
    @Test
    public void testRegisterUser() {
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        PasswordEncoder passwordEncoder =  Mockito.mock(PasswordEncoder.class);
        UserModel userModel = new UserModel();
        Mockito.when(passwordEncoder.encode(userModel.getPassword())).thenReturn(userModel.getPassword());
        Mockito.when(userRepository.save(userModel)).thenReturn(userModel);
        UserService userService = new UserService(userRepository,passwordEncoder);
        UserModel result = userService.registerUser(userModel);
        assertEquals(result,userModel);
        Mockito.verify(userRepository).save(result);
    }

    @Test
    public void testLoadUserByUsername(){
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        PasswordEncoder passwordEncoder =  Mockito.mock(PasswordEncoder.class);
        UserModel userModel = new UserModel();
        Mockito.when(userRepository.findFirstByLogin(userModel.getUsername())).thenReturn(null);
        UserService userService = new UserService(userRepository,passwordEncoder);
        ApplicationContextException exception = Assertions.assertThrows(ApplicationContextException.class, () -> {
          Optional user= (userRepository.findFirstByLogin(userModel.getUsername()));
        });
//        String expectedMessage = null;
//        String actualMessage = exception.getMessage();
//        assertTrue(actualMessage.contains(expectedMessage));
        Assertions.assertEquals(null, exception.getMessage());
        
        UserModel result = (UserModel) userService.loadUserByUsername(userModel.getUsername());
        assertEquals(result,userModel);
        Mockito.verify(userRepository).save(result);

    }



}