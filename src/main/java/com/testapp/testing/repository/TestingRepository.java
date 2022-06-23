package com.testapp.testing.repository;

import com.testapp.testing.model.Testing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestingRepository extends JpaRepository<Testing,Integer> {

}
