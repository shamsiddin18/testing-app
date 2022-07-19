package com.testapp.user.controller;

import com.testapp.Application;
import com.testapp.user.model.UserModel;
import com.testapp.user.repository.UserRepository;
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

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Test
    public void when_user_is_not_authenticated_home_page_redirect_to_login() throws Exception{
        mockMvc
                .perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithMockUser(username="test", password = "test")
    public void when_user_is_authenticated_home_page_should_be_displayed() throws Exception{
        mockMvc
                .perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = "test", password = "test")
    public void when_user_is_authenticated_register_page_should_redirect_to_home_page() throws Exception{
        mockMvc
               .perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = "test", password = "test")
    public void when_user_is_authenticated_register_page_should_display() throws Exception{
        mockMvc
                .perform(MockMvcRequestBuilders.get("/register"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

   @Test
   @WithMockUser(username = "test", password = "test")
   public void when_register_form_required_are_empty_it_should_display_validation_error() throws Exception{
       mockMvc
               .perform(MockMvcRequestBuilders.post("/register"))
               .andExpect(
                       result -> MockMvcResultMatchers
                               .xpath("//div[contains(text(), 'Username cannot be empty')]")
                               .exists());
    }

    @Test
    @WithMockUser(username = "test", password = "test")
    public void when_username_box_is_empty_it_should_display_validation_error() throws Exception{
        mockMvc
                .perform(MockMvcRequestBuilders.post("/register")
                        .param("email","email@gmail.com")
                        .param("password", "1234"))
                .andExpect(
                        result -> MockMvcResultMatchers
                                .xpath("//div[contains(text(), 'Username cannot be empty')]")
                                .exists());
    }

    @Test
    @WithMockUser(username = "test", password = "test")
    public void when_username_less_then_4_it_should_display_validation_error() throws Exception{
        mockMvc
                .perform(MockMvcRequestBuilders.post("/register")
                        .param("login", "tt")
                        .param("email","email@gmail.com")
                        .param("password", "1234"))
                .andExpect(
                        result -> MockMvcResultMatchers
                                .xpath("//div[contains(text(), 'size must be between 4 and 64')]")
                                .exists());
    }

    @Test
    @WithMockUser(username = "test", password = "test")
    public void when_username_more_then_64_it_should_display_validation_error() throws Exception{
        mockMvc
                .perform(MockMvcRequestBuilders.post("/register")
                        .param("login", "Ищем и ждём опытного и решительного Devops-инженера. Если вы тигр" +
                                ", организатор по жизни, победитель по натуре , который сможет не только решать" +
                                " поставленные задачи, но и обогатит нашу команду своими знаниями, компетенциями и " +
                                "опытом, то вам к нам.Ищем и ждём опытного и решительного Devops-инженера. Если вы тигр," +
                                " организатор по жизни, победитель по натуре , который сможет не только решать " +
                                "поставленные задачи, но и обогатит нашу команду своими знаниями, компетенциями " +
                                "и опытом, то вам к нам.")
                        .param("email","email@gmail.com")
                        .param("password", "1234"))
                .andExpect(
                        result -> MockMvcResultMatchers
                                .xpath("//div[contains(text(), 'size must be between 4 and 64')]")
                                .exists());
    }
    @Test
    @WithMockUser(username = "test", password = "test")
    public void when_email_box_is_empty_it_should_display_validation_error() throws Exception{
        mockMvc
                .perform(MockMvcRequestBuilders.post("/register")
                        .param("login", "tiger")
                        .param("password", "1234"))
                .andExpect(
                        result -> MockMvcResultMatchers
                                .xpath("//div[contains(text(), 'Email cannot be empty')]")
                                .exists());
    }
    @Test
    @WithMockUser(username = "test", password = "test")
    public void when_email_without_icon_it_should_display_validation_error() throws Exception{
        mockMvc
                .perform(MockMvcRequestBuilders.post("/register")
                        .param("login", "tiger")
                        .param("password", "1234"))
                .andExpect(
                        result -> MockMvcResultMatchers
                                .xpath("//div[contains(text(), 'Email should be valid')]")
                                .exists());
    }

    @Test
    @WithMockUser(username = "test", password = "test")
    public void when_password_box_is_empty_it_should_display_validation_error() throws Exception{
        mockMvc
                .perform(MockMvcRequestBuilders.post("/register")
                        .param("login", "tiger")
                        .param("email", "email@gmail.com"))
                .andExpect(
                        result -> MockMvcResultMatchers
                                .xpath("//div[contains(text(), 'Password cannot be empty')]")
                                .exists());
    }
    @Test
    @WithMockUser(username = "test", password = "test")
    public void when_password_less_then_4_it_should_display_validation_error() throws Exception{
        mockMvc
                .perform(MockMvcRequestBuilders.post("/register")
                        .param("login", "tiger")
                        .param("email","email@gmail.com")
                        .param("password", "12"))
                .andExpect(
                        result -> MockMvcResultMatchers
                                .xpath("//div[contains(text(), 'size must be between 4 and 64')]")
                                .exists());
    }

    @Test
    @WithMockUser(username = "test", password = "test")
    public void when_password_more_then_64_it_should_display_validation_error() throws Exception{
        mockMvc
                .perform(MockMvcRequestBuilders.post("/register")
                     .param("login", "tiger")
                     .param("email", "email@gmail.com")
                     .param("password", "Ищем и ждём опытного и решительного Devops-инженера. Если вы тигр" +
                            ", организатор по жизни, победитель по натуре , который сможет не только решать" +
                            " поставленные задачи, но и обогатит нашу команду своими знаниями, компетенциями и " +
                            "опытом, то вам к нам.Ищем и ждём опытного и решительного Devops-инженера. Если вы тигр," +
                            " организатор по жизни, победитель по натуре , который сможет не только решать " +
                            "поставленные задачи, но и обогатит нашу команду своими знаниями, компетенциями " +
                            "и опытом, то вам к нам."))
                .andExpect(
                        result -> MockMvcResultMatchers
                                .xpath("//div[contains(text(), 'size must be between 4 and 64')]")
                                .exists());
    }

    @Test
    @WithMockUser(username = "test", password = "test")
    public void when_register_submitted_it_should_redirect_to_login() throws Exception{
        mockMvc
                .perform(MockMvcRequestBuilders.post("/register")
                        .param("login", "tiger")
                        .param("email","email@gmail.com")
                        .param("password", "1234"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/login"));
        UserModel userModel = this.userRepository.findFirstByLogin("tiger").orElse(null);
        if (userModel != null) {
            userRepository.delete(userModel);
        }

    }







}
