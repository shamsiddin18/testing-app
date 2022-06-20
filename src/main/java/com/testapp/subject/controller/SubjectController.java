package com.testapp.subject.controller;

import com.testapp.answer.model.Answer;
import com.testapp.answer.repository.AnswerRepository;
import com.testapp.question.model.Question;
import com.testapp.question.repository.QuestionRepository;
import com.testapp.subject.SubjectService;
import com.testapp.subject.model.Subject;
import com.testapp.subject.repository.SubjectRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@Controller
public class SubjectController {
    private final SubjectRepository repository;
    private final SubjectService subjectService;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    public SubjectController(
        SubjectRepository repository,
        SubjectService subjectService,
        QuestionRepository questionRepository,
        AnswerRepository answerRepository
    ) {
        this.repository = repository;
        this.subjectService = subjectService;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
    }

    @GetMapping("/subjects")
    public String viewList(Model model){
        List<Subject> subjects = this.repository.findAll();
        System.out.println("subjects: "+subjects);
        model.addAttribute("subjects", subjects);
        return "subject/list";
    }

    @GetMapping("/subject/add")
    public String showCreateForm(Model model) {
        model.addAttribute("subject", new Subject());
        return "subject/create";
    }

    @PostMapping("/subject/add")
    public String submitCreateForm(@Valid Subject subject, BindingResult result){
        if (result.hasErrors()) {
            return "subject/create";
        }

        subjectService.creatSubject(subject);

        return "redirect:/subjects";
    }

    @GetMapping("/subject/{id}/edit")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Subject subject = this.repository.findById(id).orElse(null);
        if (subject == null) {
            return "redirect:user/error";
        }

        model.addAttribute("subject", subject);
        return "subject/edit";
    }

    @PostMapping ("/subject/{id}/edit")
    public String submitEditForm(@Valid Subject subject,BindingResult result,Model model) {
        if (result.hasErrors()) {
            model.addAttribute("subject" , subject);
            return "subject/edit";
        }

        this.subjectService.creatSubject(subject);
        return "redirect:/subjects";
    }

}
