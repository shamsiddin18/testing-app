package com.testapp.question.controller;

import com.testapp.question.model.Question;
import com.testapp.question.repository.QuestionRepository;
import com.testapp.question.service.QuestionService;
import com.testapp.subject.model.Subject;
import com.testapp.subject.repository.SubjectRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import javax.validation.Valid;
import java.util.List;

@Controller
public class QuestionController {
    private final QuestionRepository questionRepository;
    private final SubjectRepository subjectRepository;
    private final QuestionService questionService;

    public QuestionController(
            QuestionRepository questionRepository,
            SubjectRepository subjectRepository,
            QuestionService questionService) {
        this.questionRepository = questionRepository;
        this.subjectRepository = subjectRepository;
        this.questionService = questionService;
    }

    @GetMapping("/subject/{id}/questions")
    public String viewList(Model model, @PathVariable Integer id) {
        Subject subject = this.subjectRepository.findById(id).orElse(null);
        if (subject == null) {
            return "404";
        }

        model.addAttribute("subject", subject);

        return "question/list";
    }

    @GetMapping("/question/create")
    public String showCreateForm(Question question, Model model) {
        model.addAttribute("subjects", this.getAllSubjects());

        return "question/create";
    }

    @PostMapping("/question/create")
    public String submitCreateForm(@Valid Question question, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("subjects", this.getAllSubjects());
            return "question/create";
        }

        questionService.save(question);

        return "redirect:/subject/" + question.getSubject().getId() + "/questions";
    }

    @GetMapping("/question/{id}/edit")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Question question = this.questionRepository.findById(id).orElse(null);
        if (question == null) {
            return "404";
        }

        model.addAttribute("question", question);
        model.addAttribute("subjects", this.getAllSubjects());

        return "question/edit";
    }

    @PostMapping("/question/{id}/edit")
    public String submitEditForm(@Valid Question question, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("question", question);
            model.addAttribute("subjects", this.getAllSubjects());
            return "question/edit";
        }

        this.questionService.save(question);

        return "redirect:/subject/" + question.getSubject().getId() + "/questions";
    }

    private List<Subject> getAllSubjects() {
        return this.subjectRepository.findAll();
    }
}
