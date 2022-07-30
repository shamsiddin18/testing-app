package com.testapp.testing.repository;

import com.testapp.testing.model.Testing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestingRepository extends JpaRepository<Testing, Integer> {

    List<Testing> findAllByUserId(Integer id);
}
