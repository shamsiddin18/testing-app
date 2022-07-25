package com.testapp.testing.controller;

import com.testapp.Application;
import com.testapp.answer.model.Answer;
import com.testapp.question.model.Question;
import com.testapp.question.repository.QuestionRepository;
import com.testapp.subject.model.Subject;
import com.testapp.subject.repository.SubjectRepository;
import com.testapp.testing.model.Testing;
import com.testapp.testing.model.TestingQuestion;
import com.testapp.testing.repository.TestingQuestionRepository;
import com.testapp.testing.repository.TestingRepository;
import com.testapp.testing.service.TestingService;
import com.testapp.user.model.UserModel;
import com.testapp.user.repository.UserRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TestControllerIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    TestingRepository testingRepository;

    @Autowired
    TestingQuestionRepository testingQuestionRepository;

    @Autowired
    TestingService testingService;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    SubjectRepository subjectRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    public void when_user_is_not_authenticated_subject_list_page_should_redirect_to_login() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/testing"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithMockUser(username = "test", password = "test")
    public void when_user_is_authenticated_subject_list_page_should_display() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/testing"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void when_user_is_not_authenticated_start_testing_page_should_redirect_to_login() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/testing/subject/{id}", 1))
                .andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithMockUser(username = "test", password = "test")
    public void when_subject_null_start_testing_page_should_return_404() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/testing/subject/{id}", 99999))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        MockMvcResultMatchers
                                .xpath("//h1[contains(text(),'Subject is not found')]")
                                .exists());
    }

    @Test
    @WithMockUser(username = "test", password = "test")
    public void when_user_is_authenticated_start_testing_page_should_be_display() throws Exception {
        Subject subject = this.findSubjectById(1);

        String redirectedUrl = mockMvc
                .perform(MockMvcRequestBuilders.get("/testing/subject/{id}", subject.getId()))
                .andExpect(MockMvcResultMatchers.redirectedUrlPattern("/testing/{id}"))
                .andReturn()
                .getResponse()
                .getRedirectedUrl();

        Integer testingId = Integer.parseInt(redirectedUrl.substring(redirectedUrl.lastIndexOf("/") + 1));
        this.testingRepository.deleteById(testingId);
    }

    @Test
    public void when_user_is_not_authenticated_testing_page_should_redirect_to_login() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/testing/{id}", 9999))
                .andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithMockUser(username = "test", password = "test")
    public void when_testing_does_not_exists_the_testing_page_should_return_404() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/testing/{id}", 999999))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        MockMvcResultMatchers
                                .xpath("//h1[contains(text(),'Testing is not found')]")
                                .exists());
    }

    @Test
    @WithMockUser(username = "test", password = "test")
    public void when_user_is_authenticated_testing_page_should_display() throws Exception {
        Subject subject = this.findSubjectById(1);
        UserModel user = this.findUserById(1);

        Testing testing = new Testing();
        testing.setSubject(subject);
        testing.setUser(user);
        testing.setCreatedAt(new Date());
        testing.setEndedAt(new Date());
        this.testingService.save(testing);

        mockMvc
                .perform(MockMvcRequestBuilders.get("/testing/{id}", testing.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        this.testingRepository.delete(testing);
    }

    // @Test
    @WithMockUser(username = "test", password = "test")
    public void when_testing_submitted_it_should_display() throws Exception {
        Subject subject = this.findSubjectById(1);
        UserModel user = this.findUserById(1);

        Testing testing = new Testing();
        testing.setSubject(subject);
        testing.setUser(user);
        testing.setCreatedAt(new Date());
        testing.setEndedAt(new Date());
        for (Question question : subject.getQuestions()) {
            if (question.getAnswers().size() == 0) {
                continue;
            }
            TestingQuestion testingQuestion = new TestingQuestion();
            testingQuestion.setTesting(testing);
            testingQuestion.setQuestion(question);
            testing.addTestingQuestion(testingQuestion);
        }
        this.testingService.save(testing);

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/testing/{id}", testing.getId())
                        .param("results", "1=1"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/testing/result"));

    }

    private Subject findSubjectById(Integer id) throws Exception {
        Subject subject = this.subjectRepository.findById(id).orElse(null);
        if (subject == null) {
            throw new Exception("Subject is not found with given id: " + id);
        }

        return subject;
    }

    private UserModel findUserById(Integer id) throws Exception {
        UserModel user = this.userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new Exception("User is not found with given id: " + id);
        }

        return user;
    }
}
