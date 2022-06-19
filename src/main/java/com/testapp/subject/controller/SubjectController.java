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
    @GetMapping("/subject")
    public String viewSubjectTestList(Model model){
        List<Subject> subjects = this.repository.findAll();
        System.out.println("subjects: "+subjects);
        model.addAttribute("subjects", subjects);
        return "testing/subjects";
    }

    @GetMapping("/subject/view/{id}")
    public String view(Model model, @PathVariable Integer id) {
        Subject subject = this.repository.findById(id).orElse(null);
        if (subject == null) {
            return "404";
        }
        model.addAttribute("subject", subject);
        List<Question> questions = this.questionRepository.findBySubjectId(subject.getId());
        HashMap<Integer, List<Answer>> answersMap = new HashMap<Integer, List<Answer>>();
        for (Question question: questions) {
            answersMap.put(question.getId(), this.answerRepository.findByQuestionId(question.getId()));
        }
        model.addAttribute("questions", questions);
        model.addAttribute("answers", answersMap);

        return  "testing/test";
    }

    @PostMapping("/testing")
    public String check(Model model, @RequestParam HashMap<String, String>results){
        Integer totalCorrect = 0;
        Integer totalIncorrect = 0;
        HashMap<String, Answer> correctAnswers = new HashMap<>();
        HashMap<String, Answer> selectedAnswers = new HashMap<>();
        HashMap<String, Question> questions = new HashMap<>();
        for (Map.Entry<String, String> map : results.entrySet()){
            Integer questionId = Integer.parseInt(map.getKey());
            Question question = this.questionRepository.findById(questionId).orElse(null);
            if (question == null) {
                totalIncorrect++;
                continue;
            }
            questions.put(questionId.toString(), question);
            Integer answerId = Integer.parseInt(map.getValue());
            List<Answer> answers = this.answerRepository.findByQuestionId(questionId);

            for (Answer answer : answers) {
                if (answer.isCorrect()) {
                    correctAnswers.put(questionId.toString(), answer);
                }

                if (Objects.equals(answer.getId(), answerId)) {
                    selectedAnswers.put(questionId.toString(), answer);
                    if(answer.isCorrect()) {
                        totalCorrect++;
                        continue;
                    }

                    totalIncorrect++;
                }
            }

            Answer answer= this.answerRepository.findById(answerId).orElse(null);
        }
        model.addAttribute("totalCorrect", totalCorrect);
        model.addAttribute("totalIncorrect", totalIncorrect);
        model.addAttribute("questions", questions);
        model.addAttribute("correctAnswers", correctAnswers);
        model.addAttribute("selectedAnswers", selectedAnswers);

        return "testing/result";
    }


}
