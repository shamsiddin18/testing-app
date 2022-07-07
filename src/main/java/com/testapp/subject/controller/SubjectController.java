package com.testapp.subject.controller;

import com.testapp.subject.SubjectService;
import com.testapp.subject.model.Subject;
import com.testapp.subject.repository.SubjectRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import javax.validation.Valid;

@Controller
public final class SubjectController {
    private final SubjectRepository repository;
    private final SubjectService subjectService;

    public SubjectController(
            SubjectRepository repository,
            SubjectService subjectService) {
        this.repository = repository;
        this.subjectService = subjectService;
    }

    @GetMapping("/subjects")
    public String viewList(Model model) {
        List<Subject> subjects = this.repository.findAll();
        System.out.println("subjects: " + subjects);
        model.addAttribute("subjects", subjects);
        return "subject/list";
    }

    @GetMapping("/subject/add")
    public String showCreateForm(Model model) {
        model.addAttribute("subject", new Subject());
        return "subject/create";
    }

    @PostMapping("/subject/add")
    public String submitCreateForm(@Valid Subject subject, BindingResult result) {
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

    @PostMapping("/subject/{id}/edit")
    public String submitEditForm(@Valid Subject subject, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("subject", subject);
            return "subject/edit";
        }

        this.subjectService.creatSubject(subject);
        return "redirect:/subjects";
    }
}
