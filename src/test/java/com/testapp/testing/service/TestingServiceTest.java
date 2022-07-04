package com.testapp.testing.service;

import com.testapp.testing.model.Testing;
import com.testapp.testing.repository.TestingRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import java.util.Optional;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestingServiceTest {
  @Test
  public void when_repository_return_empty_it_should_return_null() {
    TestingRepository repository = Mockito.mock(TestingRepository.class);
    Mockito.when(repository.findById(2)).thenReturn(Optional.empty());

    TestingService service = new TestingService(repository);

    assertNull(service.find(2));
    Mockito.verify(repository).findById(2);
  }

  @Test
  public void when_repository_return_result_it_should_return_result() {
    TestingRepository repository = Mockito.mock(TestingRepository.class);
    Testing testing = new Testing();
    testing.setId(2);

    Mockito.when(repository.findById(2)).thenReturn(Optional.of(testing));
    TestingService service = new TestingService(repository);

    Testing result = service.find(2);
    assertNotNull(result);
    assertEquals(2, result.getId());
    Mockito.verify(repository).findById(2);
  }
}
