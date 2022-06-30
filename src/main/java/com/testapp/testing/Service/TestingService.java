package com.testapp.testing.Service;

import com.testapp.testing.model.Testing;
import com.testapp.testing.repository.TestingRepository;
import org.springframework.stereotype.Service;

@Service
public class TestingService {
    private TestingRepository testRepository;

    public TestingService(TestingRepository testRepository) {
        this.testRepository = testRepository;
    }

    public void save(Testing model) {
        testRepository.save(model);
    }

    public Testing find(Integer id) {
        return testRepository.findById(id).orElse(null);
    }
}
