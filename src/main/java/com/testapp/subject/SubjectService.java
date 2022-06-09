package com.testapp.subject;

import com.testapp.subject.model.Subject;
import com.testapp.subject.repository.SubjectRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SubjectService implements UserDetailsService {
    private SubjectRepository subjectRepository;

    public SubjectService(SubjectRepository subjectRepository){
        this.subjectRepository=subjectRepository;
    }

    public Subject creatSubject(Subject subject){
//        if(title==null){
//            return  null;
//        }
//
//        if(subjectRepository.findFirstByTitle(title).isPresent()){
//            return  null;
//        }
//
//        Subject subject = new Subject();
//        subject.setTitle(title);
        return subjectRepository.save(subject);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       Subject user =this.subjectRepository.findFirstByTitle(username).orElse(null);
        if(user == null){
            throw new UsernameNotFoundException("User is not found");
        }

        return user;
    }







}
