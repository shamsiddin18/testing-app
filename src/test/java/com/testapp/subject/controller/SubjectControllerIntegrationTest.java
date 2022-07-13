package com.testapp.subject.controller;

import com.testapp.Application;
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

import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class SubjectControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    SubjectRepository subjectRepository;

    @Test
    public void when_user_is_not_authenticated_it_should_redirect_to_login() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/subjects"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithMockUser(username = "test", password = "test")
    public void when_user_is_authenticated_it_should_be_display() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/subjects"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void when_user_is_not_authenticated_in_add_page_it_should_redirect_to_login() throws Exception{
        mockMvc
                .perform(MockMvcRequestBuilders.get("/subject/add"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithMockUser(username = "test", password = "test")
    public void when_user_is_authenticated_in_add_page_it_should_be_display() throws Exception{
        mockMvc
                .perform(MockMvcRequestBuilders.get("/subject/add"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = "test", password = "test")
    public void when_subject_created_it_should_show_in_the_list() throws Exception{
        mockMvc
                .perform(MockMvcRequestBuilders.post("/subject/add")
                        .param("title","math12"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/subjects"))
                .andDo(
                        result -> MockMvcResultMatchers
                                .xpath("//table//tbody//tr[0]//td[1]//a[contains(title(), 'math12')]")
                                .exists());
        List<Subject> subject = subjectRepository.findAll();
        subjectRepository.deleteAll(subject);
    }
    @Test
    @WithMockUser(username = "test", password = "test")
    public void when_create_form_invalid_it_should_display_validation_errors() throws Exception{
        mockMvc
                .perform(MockMvcRequestBuilders.post("/subject/add")
                        .param("title", "tttt") )
                .andExpect(MockMvcResultMatchers
                        .xpath("//div[contains(text(), 'must not be empty')]")
                        .exists());
    }

    @Test
    public void when_user_is_not_authenticated_edit_page_it_should_redirect_to_login() throws  Exception{
        Subject subject = new Subject();
        subject.setTitle("math22");
        mockMvc
                .perform(MockMvcRequestBuilders.get("/subject/{id}/edit", subject.getId()))
                .andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/login"));
    }


    @Test
    @WithMockUser(username = "test", password = "test")
    public void when_user_authenticated_edit_page_it_should_be_display() throws Exception{
        Subject subject = new Subject();
        subject.setTitle("math12");
        mockMvc
                .perform(MockMvcRequestBuilders.get("/subject/{id}/edit", subject.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    @WithMockUser(username = "test", password = "test")
    public void when_edit_form_invalid_it_should_display_validation_errors() throws Exception{
        Subject subject = new Subject();
        subject.setTitle("math12");
        subjectRepository.save(subject);
        mockMvc
                .perform(MockMvcRequestBuilders.post("/subject/{id}/edit", subject.getId()))
                .andExpect(MockMvcResultMatchers
                        .xpath("//span[contains(text(), 'must not be empty')]")
                        .exists());
        subjectRepository.delete(subject);
    }

    @Test
    @WithMockUser(username = "test", password = "test")
    public void when_edit_form_submitted_it_should_show_in_list() throws Exception{
        Subject subject = new Subject();
        subject.setTitle("math12");
        subjectRepository.save(subject);
        mockMvc
                .perform(MockMvcRequestBuilders.post("/subject/{id}/edit", subject.getId())
                        .param("title", "math22"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/subjects"))
                .andDo(
                        result -> MockMvcResultMatchers
                                .xpath("//table//tbody//tr[0]//td[1]//a[contains(title(), 'math22')]")
                                .exists()

       );
        subjectRepository.delete(subject);
    }

}
