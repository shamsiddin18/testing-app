package com.testapp.testing.controller;

import com.testapp.Application;
import com.testapp.question.model.Question;
import com.testapp.question.repository.QuestionRepository;
import com.testapp.subject.model.Subject;
import com.testapp.testing.model.Testing;
import com.testapp.testing.model.TestingQuestion;
import com.testapp.testing.repository.TestingQuestionRepository;
import com.testapp.testing.repository.TestingRepository;
import com.testapp.testing.service.TestingService;
import com.testapp.user.model.UserModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Date;
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


    @Test
    public void when_user_is_not_authenticated_subject_list_page_should_redirect_to_login() throws Exception{
        mockMvc
                .perform(MockMvcRequestBuilders.get("/testing"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithMockUser(username = "test",password = "test")
    public void when_user_is_authenticated_subject_list_page_should_display() throws Exception{
        mockMvc
                .perform(MockMvcRequestBuilders.get("/testing"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void when_user_is_not_authenticated_start_testing_page_should_redirect_to_login() throws Exception{
        mockMvc
                .perform(MockMvcRequestBuilders.get("/testing/subject/{id}",1))
                .andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/login"));
    }

//    @Test
    @WithMockUser(username = "test",password = "test")
    public void when_user_is_authenticated_start_testing_page_should_be_display() throws Exception{
        mockMvc
               .perform(MockMvcRequestBuilders.get("/testing/subject/{id}",1))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = "test",password = "test")
    public void when_subject_null_start_testing_page_should_return_404() throws Exception{
        mockMvc
                .perform(MockMvcRequestBuilders.get("/testing/subject/{id}", 99))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        MockMvcResultMatchers
                                .xpath("//h1[contains(text(),'Subject is not found')]")
                                .exists());
    }

    @Test
    @WithMockUser(username = "test", password = "test")
    public void when_start_testing_page_should_display() throws Exception{
        Subject subject = new Subject();
        subject.setId(1);
        Set<Question> questions = this.questionRepository.findBySubjectId(subject.getId());
        UserModel user = new UserModel();
        user.setId(1);
        Testing testing = new Testing();
        testing.setUser(user);
        testing.setSubject(subject);
        testing.setCreatedAt(new Date());
        for (Question question : questions) {
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
                .perform(MockMvcRequestBuilders.get("/testing/subject/{id}",subject.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk());


    }








}
