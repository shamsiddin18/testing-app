package com.testapp.question.controller;


import com.testapp.Application;
import com.testapp.question.model.Question;
import com.testapp.question.repository.QuestionRepository;
import com.testapp.subject.model.Subject;
import com.testapp.subject.repository.SubjectRepository;
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

import java.util.Optional;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class QuestionControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    SubjectRepository subjectRepository;


    @Test
    public void when_user_is_not_authenticated_view_list_page_should_redirect_to_login() throws Exception{
       Subject subject = new Subject();
       subject.setTitle("math36");
       subjectRepository.save(subject);
        mockMvc
                .perform(MockMvcRequestBuilders.get("/subject/{id}/question",subject.getId()))
                .andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/login"));
        subjectRepository.delete(subject);
    }

    @Test
    @WithMockUser(username = "test", password = "test")
    public void when_user_is_authenticated_the_view_list_page_should_be_displayed() throws Exception{
        Subject subject = new Subject();
        subject.setTitle("math48");
        subjectRepository.save(subject);
        mockMvc
                .perform(MockMvcRequestBuilders.get("/subject/{id}/questions", subject.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk());
        subjectRepository.delete(subject);
    }

    @Test
    @WithMockUser(username = "test", password = "test")
    public void when_subject_does_not_exists_the_view_list_page_should_return_404()throws Exception{
        mockMvc
                .perform(MockMvcRequestBuilders.get("/subject/{id}/questions", 999999))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        MockMvcResultMatchers
                                .xpath("//h1[contains(text(),'Subject is not found')]")
                                .exists());
    }

    @Test
    public void when_user_is_not_authenticated_the_create_page_should_redirect_to_login() throws Exception{
        mockMvc
                .perform(MockMvcRequestBuilders.get("/question/create"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/login"));

    }

    @Test
    @WithMockUser(username = "test", password = "test")
    public void when_user_is_authenticated_the_create_page_should_display() throws Exception{
        mockMvc
                .perform(MockMvcRequestBuilders.get("/question/create"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

//  @Test
    @WithMockUser(username = "test", password = "test")
    public void when_question_is_created_it_should_be_in_the_list() throws  Exception{
        mockMvc
                .perform(MockMvcRequestBuilders.post("/question/create")
                        .param("text", "question90")
                        .param("subject","math91"))/////////////ask//////////
                .andExpect(MockMvcResultMatchers.redirectedUrl("/subject/{id}/questions"))////////
                .andDo(
                        result -> MockMvcResultMatchers
                                .xpath("//table//tbody//tr[0]//td[1]//a[contains(text(), ' '")
                                .exists());
        Optional<Subject> subjects = subjectRepository.findFirstByTitle("math72");
        subjectRepository.delete(subjects.get());
//        Set<Question> questions = questionRepository.findBySubjectId(subjects.get().getId());
//        questionRepository.delete(questions);

    }

    @Test
    @WithMockUser(username = "test", password = "test")
    public void when_text_is_empty_create_form_should_display_validation_error() throws Exception{
        mockMvc
                .perform(MockMvcRequestBuilders.post("/question/add")
                        .param("text", " "))
                .andExpect(
                        result ->  MockMvcResultMatchers
                                .xpath("//span[contains(text(), 'Question can not be empty')]")
                                .exists());
    }

    @Test
    @WithMockUser(username = "test", password = "test")
    public void when_text_is_less_than_3_create_form_should_display_validation_error() throws Exception{
        mockMvc
                .perform(MockMvcRequestBuilders.post("/question/add")
                        .param("text", "r"))
                .andExpect(
                        result ->  MockMvcResultMatchers
                                .xpath("//span[contains(text(), 'size must be between 3 and 1000')]")
                                .exists());
    }

    @Test
    public void when_user_is_not_authenticated_it_should_redirect_to_login() throws  Exception{
        mockMvc
         .perform(MockMvcRequestBuilders.get("/question/{id}/edit",1))
                .andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithMockUser(username = "test", password = "test")
    public void when_user_authenticated_edit_page_it_should_be_display() throws Exception{
        Subject subject = new Subject();
        subject.setTitle("math143");
        subjectRepository.save(subject);

        Question question = new Question();
        question.setText("question143");
        question.setSubject(subject);
        questionRepository.save(question);
        mockMvc
                .perform(MockMvcRequestBuilders.get("/question/{id}/edit", question.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk());
        questionRepository.delete(question);
        subjectRepository.delete(subject);
    }

  @Test
  @WithMockUser(username = "test", password = "test")
  public void when_question_does_not_exists_the_edit_page_should_return_404()throws Exception{
        mockMvc
                .perform(MockMvcRequestBuilders.get("/question/{id}/edit", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(
                        result -> MockMvcResultMatchers
                                .xpath("//h1[contains(text(),'Question is not found')]")
                                .exists());

  }

  @Test
  @WithMockUser(username = "test", password = "test")
    public void  when_edit_form_empty_it_should_display_validation_errors() throws Exception{
      Subject subject = new Subject();
      subject.setTitle("math118");
      subjectRepository.save(subject);

      Question question = new Question();
      question.setText("question178");
      question.setSubject(subject);
      questionRepository.save(question);
      mockMvc
              .perform(MockMvcRequestBuilders.post("/question/{id}/edit", 1))
              .andExpect(
                      result ->  MockMvcResultMatchers
                              .xpath("//div[contains(text(), 'cannot be empty')]")
                              .exists());
      subjectRepository.delete(subject);
      questionRepository.delete(question);
  }

    @Test
    @WithMockUser(username = "test", password = "test")
    public void  when_edit_form_less_than_three_it_should_display_validation_errors() throws Exception{
        Subject subject = new Subject();
        subject.setTitle("math118");
        subjectRepository.save(subject);

        Question question = new Question();
        question.setText("question199");
        question.setSubject(subject);
        questionRepository.save(question);
        mockMvc
                .perform(MockMvcRequestBuilders.post("/question/{id}/edit", question.getId())
                        .param("text" , "rr"))
                .andExpect(
                        result ->  MockMvcResultMatchers
                                .xpath("//div[contains(text(), ' Question cannot be empty')]")
                                .exists());
        subjectRepository.delete(subject);
        questionRepository.delete(question);
    }

    @Test
    @WithMockUser(username = "test", password = "test")
    public void when_edit_form_submitted_it_should_show_in_list() throws Exception{
        Subject subject = new Subject();
        subject.setTitle("math133");
        subjectRepository.save(subject);

        Question question = new Question();
        question.setText("question222");
        question.setSubject(subject);
        questionRepository.save(question);
        mockMvc
                .perform(MockMvcRequestBuilders.post("/question/{id}/edit", question.getId())
                        .param("text", "math227"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/subject/"+question.getSubject().getId() +"/questions"))
                .andDo(
                        result -> MockMvcResultMatchers
                                .xpath("//table//tbody//tr[0]//td[1]//a[contains(text(), 'math146')]")
                                .exists()

                );
        subjectRepository.delete(subject);
        questionRepository.delete(question);
    }









}