package com.testapp.subject.controller;

import com.testapp.Application;
import com.testapp.subject.model.Subject;
import com.testapp.subject.repository.SubjectRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import java.util.Optional;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class SubjectControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SubjectRepository subjectRepository;

    @Test
    @WithAnonymousUser
    public void when_user_is_not_authenticated_the_list_page_should_be_redirected_to_login() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/subjects"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithMockUser
    public void when_user_is_authenticated_the_list_page_should_be_displayed() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/subjects"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithAnonymousUser
    public void when_user_is_not_authenticated_the_add_page_should_be_redirected_to_login() throws Exception{
        mockMvc
                .perform(MockMvcRequestBuilders.get("/subject/add"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithMockUser
    public void when_user_is_authenticated_the_adding_page_should_be_displayed() throws Exception{
        mockMvc
                .perform(MockMvcRequestBuilders.get("/subject/add"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser
    public void when_subject_is_created_it_should_be_in_the_list() throws Exception{
        mockMvc
                .perform(MockMvcRequestBuilders.post("/subject/add")
                        .param("title","math72"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/subjects"))
                .andDo(
                        result -> MockMvcResultMatchers
                                .xpath("//table//tbody//tr[0]//td[1]//a[contains(text(), 'math72')]")
                                .exists());
      Optional<Subject> subjects = subjectRepository.findFirstByTitle("math72");
        subjectRepository.delete(subjects.get());

    }
    @Test
    @WithMockUser
    public void when_title_is_empty_create_form_should_display_validation_error() throws Exception{
        mockMvc
                .perform(MockMvcRequestBuilders.post("/subject/add")
                        .param("title", " "))
                .andExpect(
                        result ->  MockMvcResultMatchers
                        .xpath("//div[contains(text(), 'subject  cannot be empty')]")
                        .exists());
    }

    @Test
    @WithMockUser
    public void when_title_already_exists_create_form_should_display_validation_error() throws Exception{
          Subject subject = new Subject();
          subject.setTitle("Dummy");
          subjectRepository.save(subject);
          mockMvc
                  .perform(MockMvcRequestBuilders.post("/subject/add")
                          .param("title", "Dummy"))
                  .andExpect(
                          result -> MockMvcResultMatchers
                                  .xpath("//div[contains(text(), 'subject should be unique')]")
                                  .exists());
          subjectRepository.delete(subject);
    }

    @Test
    @WithAnonymousUser
    public void when_user_is_not_authenticated_edit_page_it_should_redirect_to_login() throws  Exception{

        mockMvc
                .perform(MockMvcRequestBuilders.get("/subject/{id}/edit", 1))
                .andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/login"));

    }


    @Test
    @WithMockUser
    public void when_user_authenticated_edit_page_it_should_be_display() throws Exception{
        Subject subject = new Subject();
        subject.setTitle("math106");
        subjectRepository.save(subject);
        mockMvc
                .perform(MockMvcRequestBuilders.get("/subject/{id}/edit", subject.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk());
        subjectRepository.delete(subject);

    }

    @Test
    @WithMockUser
    public void when_edit_form_empty_it_should_display_validation_errors() throws Exception{
        Subject subject = new Subject();
        subject.setTitle("math118");
        subjectRepository.save(subject);
        mockMvc
                .perform(MockMvcRequestBuilders.post("/subject/{id}/edit", subject.getId()))
                .andExpect(
                        result ->  MockMvcResultMatchers
                        .xpath("//div[contains(text(), ' cannot be empty')]")
                        .exists());
        subjectRepository.delete(subject);
    }
    @Test
    @WithMockUser
    public void when_edit_form_invalid_it_should_display_validation_errors() throws Exception{
        mockMvc
                .perform(MockMvcRequestBuilders.post("/subject/{id}/edit", 1))
                .andExpect(
                        result ->  MockMvcResultMatchers
                                .xpath("//div[contains(text(), ' cannot be empty')]")
                                .exists());
    }

    @Test
    @WithMockUser
    public void when_subject_does_not_exists_the_edit_page_should_return_404()throws Exception{
        mockMvc
                .perform(MockMvcRequestBuilders.get("/subject/{id}/edit", 999999))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        MockMvcResultMatchers
                                .xpath("//h1[contains(text(),'Subject is not found')]")
                                .exists());
    }

    @Test
    @WithMockUser
    public void when_edit_form_submitted_it_should_show_in_list() throws Exception{
        Subject subject = new Subject();
        subject.setTitle("math133");
       subjectRepository.save(subject);
        mockMvc
                .perform(MockMvcRequestBuilders.post("/subject/{id}/edit", subject.getId())
                        .param("title", "math146"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/subjects"))
                .andDo(
                        result -> MockMvcResultMatchers
                                .xpath("//table//tbody//tr[0]//td[1]//a[contains(text(), 'math146')]")
                                .exists()

       );
        subjectRepository.delete(subject);
    }

}
