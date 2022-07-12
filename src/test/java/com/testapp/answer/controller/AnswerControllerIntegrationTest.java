package com.testapp.answer.controller;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.testapp.Application;
import com.testapp.answer.model.Answer;
import com.testapp.answer.repository.AnswerRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AnswerControllerIntegrationTest {
  @Autowired
  private AnswerRepository answerRepository;

  @Autowired
  private MockMvc mockMvc;

  @Test
  public void when_user_is_not_authenticated_it_should_redirect_to_login() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.get("/question/{id}/answers", 1))
        .andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/login"));
  }

  @Test
  @WithMockUser(username = "test", password = "test")
  public void when_question_is_invalid_it_should_return_not_found() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.get("/question/{id}/answers", 999999))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(
            MockMvcResultMatchers
                .xpath("//h1[contains(text(),'Question is not found')]")
                .exists());
  }

  @Test
  @WithMockUser(username = "test", password = "test")
  public void when_answer_is_created_it_should_be_in_the_list() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.post("/answer/create")
            .param("text", "Answer 1")
            .param("question", "1")
            .param("correct", "false"))
        .andExpect(MockMvcResultMatchers.redirectedUrl("/question/1/answers"))
        .andDo(
            result -> MockMvcResultMatchers
                .xpath("//table//tbody//tr[0]//td[1][contains(text(), 'Answer 1')]")
                .exists());
    List<Answer> answers = answerRepository.findByQuestionId(1);
    answerRepository.deleteAll(answers);
  }
}
