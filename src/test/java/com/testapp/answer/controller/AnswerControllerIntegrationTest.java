package com.testapp.answer.controller;

import java.util.List;
import com.testapp.question.model.Question;
import com.testapp.question.repository.QuestionRepository;
import com.testapp.subject.model.Subject;
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
        private QuestionRepository questionRepository;

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
        public void when_question_has_not_answers_it_should_return_empty_list() throws Exception {
                Subject subject = new Subject();
                subject.setId(1);

                Question question = new Question();
                question.setText("Dummy question");
                question.setSubject(subject);
                questionRepository.save(question);

                mockMvc
                                .perform(MockMvcRequestBuilders.get("/question/{id}/answers", question.getId()))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(
                                                MockMvcResultMatchers
                                                                .xpath("//td[contains(text(),'Empty')]")
                                                                .exists());

                questionRepository.delete(question);
        }

         @Test
        @WithMockUser(username = "test", password = "test")
        public void when_answer_is_created_it_should_be_in_the_db() throws Exception {
                mockMvc
                                .perform(MockMvcRequestBuilders.post("/answer/create")
                                                .param("text", "Dummy answer")
                                                .param("question", "1")
                                                .param("correct", "false"))
                                .andExpect(MockMvcResultMatchers.redirectedUrl("/question/1/answers"));


                 Answer answer = answerRepository.findByText("Dummy answer");
                  answerRepository.delete(answer);
        }

        @Test
        public void when_user_is_not_authenticated_create_page_should_redirect_to_login() throws Exception {
                mockMvc
                                .perform(MockMvcRequestBuilders.get("/answer/create"))
                                .andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/login"));
        }

        @Test
        @WithMockUser(username = "test", password = "test")
        public void when_user_is_authenticated_create_page_should_be_displayed() throws Exception {
                mockMvc
                                .perform(MockMvcRequestBuilders.get("/answer/create"))
                                .andExpect(MockMvcResultMatchers.status().isOk());
        }

        @Test
        @WithMockUser(username = "test", password = "test")
        public void when_user_is_authenticated_edit_page_should_be_displayed() throws Exception {
                Question question = new Question();
                question.setId(1);

                Answer answer = new Answer();
                answer.setText("Dummy answer");
                answer.setQuestion(question);
                answerRepository.save(answer);

                mockMvc
                                .perform(MockMvcRequestBuilders.get("/answer/{id}/edit", answer.getId()))
                                .andExpect(MockMvcResultMatchers.status().isOk());

                answerRepository.delete(answer);
        }

        @Test
        public void when_user_is_not_authenticated_edit_page_should_redirect_to_login() throws Exception {
                Question question = new Question();
                question.setId(1);

                Answer answer = new Answer();
                answer.setText("Dummy answer");
                answer.setQuestion(question);
                answerRepository.save(answer);

                mockMvc
                                .perform(MockMvcRequestBuilders.get("/answer/{id}/edit", answer.getId()))
                                .andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/login"));

                answerRepository.delete(answer);
        }

        @Test
        @WithMockUser(username = "test", password = "test")
        public void when_edit_form_invalid_it_should_display_validation_errors() throws Exception {
                Question question = new Question();
                question.setId(1);

                Answer answer = new Answer();
                answer.setText("Dummy answer");
                answer.setQuestion(question);
                answerRepository.save(answer);

                mockMvc
                                .perform(MockMvcRequestBuilders.post("/answer/{id}/edit", answer.getId())
                                                .param("question", "1")
                                                .param("correct", "false"))
                                .andExpect(MockMvcResultMatchers
                                                .xpath("//span[contains(text(), 'must not be empty')]")
                                                .exists());
                answerRepository.delete(answer);
        }

        @Test
        @WithMockUser(username = "test", password = "test")
        public void when_edit_form_submitted_it_should_be_in_the_list() throws Exception {
                Question question = new Question();
                question.setId(1);

                Answer answer = new Answer();
                answer.setText("Dummy answer");
                answer.setQuestion(question);
                answerRepository.save(answer);

                mockMvc
                                .perform(MockMvcRequestBuilders.post("/answer/{id}/edit", answer.getId())
                                                .param("text", "Dummy answer edited")
                                                .param("question", "1")
                                                .param("correct", "false"))
                                .andExpect(MockMvcResultMatchers.redirectedUrl("/question/1/answers"));

                answerRepository.delete(answer);
        }

}
