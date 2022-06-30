package com.testapp.subject;

import com.testapp.subject.model.Subject;
import com.testapp.subject.repository.SubjectRepository;
import org.springframework.stereotype.Service;

@Service
public class SubjectService {
    private SubjectRepository subjectRepository;

    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public Subject creatSubject(Subject subject) {
        return subjectRepository.save(subject);
    }
}
