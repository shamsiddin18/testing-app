package com.testapp.user.service;


import com.testapp.user.model.UserModel;
import com.testapp.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService  implements UserDetailsService {
    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(UserModel userModel){
        userModel.setPassword(this.passwordEncoder.encode(userModel.getPassword()));
        userRepository.save(userModel);
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        UserModel user =this.userRepository.findFirstByLogin(username).orElse(null);
        if(user == null){
            throw new UsernameNotFoundException("User is not found");
        }

        return user;
    }
}
