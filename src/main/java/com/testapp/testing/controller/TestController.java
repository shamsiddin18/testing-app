package com.testapp.testing.controller;

import com.testapp.answer.model.Answer;
import com.testapp.answer.repository.AnswerRepository;
import com.testapp.question.model.Question;
import com.testapp.question.repository.QuestionRepository;
import com.testapp.subject.model.Subject;
import com.testapp.subject.repository.SubjectRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.*;

@Controller
public class TestController {
    private  final SubjectRepository subjectRepository;
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

    public TestController(
        SubjectRepository subjectRepository,
        AnswerRepository answerRepository,
        QuestionRepository questionRepository
    ){
        this.answerRepository=answerRepository;
        this.questionRepository=questionRepository;
        this.subjectRepository=subjectRepository;
    }

    @GetMapping("/testing")
    public String viewSubjectTestList(Model model){
        model.addAttribute("subjects", this.subjectRepository.findAll());

        return "testing/subject";
    }

    @GetMapping("/testing/subject/{id}")
    public String view(Model model, @PathVariable Integer id) {
        Subject subject = this.subjectRepository.findById(id).orElse(null);
        if (subject == null) {
            return "404";
        }
        Set<Question> questions = this.questionRepository.findBySubjectId(subject.getId());
        /*
          STEPS:
           - [ ] Insert single record into testing table with: user_id, subject_id, start_time (DateTime) and get testing.id
           - [ ] Insert multiple records into testing_question table with: testing_id, question_id, i.e.
                for (question : questions) {
                    insert into testing_question(testing.id, question.id)
                }
         */
        model.addAttribute("subject", subject);
        model.addAttribute("questions", questions);

        return  "testing/test";
    }

    @PostMapping("/testing")
    public String check(Model model,@RequestParam HashMap<String, String>results){
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
