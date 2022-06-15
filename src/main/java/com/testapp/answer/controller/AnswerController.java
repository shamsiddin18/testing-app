package com.testapp.answer.controller;

import com.testapp.answer.AnswerService;
import com.testapp.answer.model.Answer;
import com.testapp.answer.repository.AnswerRepository;
import com.testapp.question.model.Question;
import com.testapp.question.repository.QuestionRepository;
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
public class AnswerController {
    private final AnswerRepository repository;
    private final AnswerService answerService;
    private final QuestionRepository questionRepository;

    public AnswerController(
        AnswerRepository repository,
        AnswerService answerService,
        QuestionRepository questionRepository
    ) {
        this.repository = repository;
        this.answerService = answerService;
        this.questionRepository = questionRepository;
    }
    private List <Question> getAllQuestion(){
        return  this.questionRepository.findAll();
    }

    @GetMapping("/question/{id}/answers")
    public String viewList(@PathVariable Integer id, Model model) {
        Question question = this.questionRepository.findById(id).orElse(null);
        if (question == null) {
            return "404";
        }

        model.addAttribute("question", question);

        return "answer/list";
    }

    @GetMapping("/answer/create")
    public String showCreateForm( Answer answer, Model model){
        model.addAttribute("questions", this.getAllQuestion());
        return "answer/create";
    }

    @PostMapping("/answer/create")
    public String submitCreateForm(@Valid Answer answer, BindingResult result, Model model){
        if (result.hasErrors()) {
            model.addAttribute("questions", this.getAllQuestion());
            return "answer/create";
        }
        answerService.create(answer);

        // @TODO: Please redirect to question answer list
        return "redirect:/subjects";
    }

    @GetMapping("/answer/{id}/edit")
    public String showEditForm(@PathVariable Integer id, Model model){
        Answer answer = this.repository.findById(id).orElse(null);
        if (answer == null) {
            return "redirect:user/error";
        }

        model.addAttribute("answer", answer);
        model.addAttribute("questions", this.getAllQuestion());

        return "answer/edit";
    }

    @PostMapping("/answer/{id}/edit")
    public String submitEditForm(@Valid Answer answer, BindingResult result, Model model){
        if (result.hasErrors()) {
            model.addAttribute("answer" , answer);
            model.addAttribute("questions", this.getAllQuestion());
            return "answer/edit";
        }

        this.answerService.create(answer);

        return "redirect:/question/"+answer.getQuestion().getId()+"/answers";
    }
}
