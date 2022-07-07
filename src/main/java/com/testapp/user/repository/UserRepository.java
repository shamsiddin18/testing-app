package com.testapp.user.repository;

import com.testapp.user.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, Integer> {
    Optional<UserModel> findFirstByLogin(String login);
}
