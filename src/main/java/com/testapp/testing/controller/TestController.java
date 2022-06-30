package com.testapp.testing.controller;

import com.testapp.answer.model.Answer;
import com.testapp.answer.repository.AnswerRepository;
import com.testapp.question.model.Question;
import com.testapp.question.repository.QuestionRepository;
import com.testapp.subject.model.Subject;
import com.testapp.subject.repository.SubjectRepository;
import com.testapp.testing.Service.TestingService;
import com.testapp.testing.model.Testing;
import com.testapp.testing.model.TestingQuestion;
import com.testapp.user.model.UserModel;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
public class TestController {
    private final SubjectRepository subjectRepository;
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final TestingService testingService;

    public TestController(
            SubjectRepository subjectRepository,
            AnswerRepository answerRepository,
            QuestionRepository questionRepository,
            TestingService testingService) {
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
        this.subjectRepository = subjectRepository;
        this.testingService = testingService;
    }

    @GetMapping("/testing")
    public String viewSubjectTestList(Model model) {
        model.addAttribute("subjects", this.subjectRepository.findAll());

        return "testing/subject";
    }

    @GetMapping("/testing/subject/{id}")
    public String startTesting(@PathVariable Integer id, Authentication auth, Model model) {
        Subject subject = this.subjectRepository.findById(id).orElse(null);
        if (subject == null) {
            return "404";
        }
        Set<Question> questions = this.questionRepository.findBySubjectId(subject.getId());
        /*
         * STEPS:
         * - Insert single record into testing table with: user_id, subject_id,
         * created_at (DateTime)
         * - Insert multiple records into testing_question table with: testing_id,
         * question_id
         */
        Testing testing = new Testing();
        testing.setUser((UserModel) auth.getPrincipal());
        testing.setSubject(subject);
        testing.setCreatedAt(new Date());
        for (Question question : questions) {
            TestingQuestion testingQuestion = new TestingQuestion();
            testingQuestion.setTesting(testing);
            testingQuestion.setQuestion(question);
            testing.addTestingQuestion(testingQuestion);
        }

        this.testingService.save(testing);

        return "redirect:/testing/" + testing.getId();
    }

    @GetMapping("/testing/{id}")
    public String testing(@PathVariable Integer id, Model model) {
        Testing testing = testingService.find(id);
        if (testing == null) {
            return "error";
        }

        model.addAttribute("subject", testing.getSubject());
        // model.addAttribute("questions", testing.getQuestions());

        return "testing/test";
    }

    @PostMapping("/testing")
    public String check(Model model, @RequestParam HashMap<String, String> results) {
        Integer totalCorrect = 0;
        Integer totalIncorrect = 0;
        HashMap<String, Answer> correctAnswers = new HashMap<>();
        HashMap<String, Answer> selectedAnswers = new HashMap<>();
        HashMap<String, Question> questions = new HashMap<>();
        for (Map.Entry<String, String> map : results.entrySet()) {
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
                    if (answer.isCorrect()) {
                        totalCorrect++;
                        continue;
                    }

                    totalIncorrect++;
                }
            }

            Answer answer = this.answerRepository.findById(answerId).orElse(null);
        }

        model.addAttribute("totalCorrect", totalCorrect);
        model.addAttribute("totalIncorrect", totalIncorrect);
        model.addAttribute("questions", questions);
        model.addAttribute("correctAnswers", correctAnswers);
        model.addAttribute("selectedAnswers", selectedAnswers);

        return "testing/result";
    }

}
