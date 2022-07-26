package com.testapp.testing.controller;

import com.testapp.answer.model.Answer;
import com.testapp.answer.repository.AnswerRepository;
import com.testapp.question.model.Question;
import com.testapp.question.repository.QuestionRepository;
import com.testapp.subject.model.Subject;
import com.testapp.subject.repository.SubjectRepository;
import com.testapp.testing.model.Testing;
import com.testapp.testing.model.TestingQuestion;
import com.testapp.testing.service.TestingService;
import com.testapp.user.model.UserModel;
import com.testapp.user.repository.UserRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Controller
public final class TestController {
    private final SubjectRepository subjectRepository;
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final TestingService testingService;

    public TestController(
            SubjectRepository subjectRepository,
            AnswerRepository answerRepository,
            QuestionRepository questionRepository,
            UserRepository userRepository,
            TestingService testingService) {
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
        this.subjectRepository = subjectRepository;
        this.userRepository = userRepository;
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
            model.addAttribute("error", "Subject is not found");
            return "error/404";
        }
        Set<Question> questions = this.questionRepository.findBySubjectId(subject.getId());
        /*
         * STEPS:
         * - Insert single record into testing table with: user_id, subject_id,
         * created_at (DateTime)
         * - Insert multiple records into testing_question table with: testing_id,
         * question_id
         */

        UserDetails user = (UserDetails) auth.getPrincipal();
        UserModel userModel = this.userRepository.findFirstByLogin(user.getUsername()).orElse(null);
        if (userModel == null) {
            return "redirect:/login";
        }

        Testing testing = new Testing();
        testing.setUser(userModel);
        testing.setSubject(subject);
        testing.setCreatedAt(new Date());
        for (Question question : questions) {
            if (question.getAnswers().size() == 0) {
                continue;
            }
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
            model.addAttribute("error", "Testing is not found");
            return "error/404";
        }

        model.addAttribute("testing", testing);

        if (testing.getEndedAt() != null) {
            HashMap<Integer, Answer> submittedAnswers = new HashMap<>();
            HashMap<Integer, Question> submittedQuestions = new HashMap<>();
            for (TestingQuestion testingQuestion : testing.getTestingQuestions()) {
                submittedQuestions.put(testingQuestion.getQuestion().getId(), testingQuestion.getQuestion());

                for (Answer answer : testingQuestion.getQuestion().getAnswers()) {
                    submittedAnswers.put(answer.getId(), answer);
                }
            }

            model.addAttribute("submittedAnswers", submittedAnswers);
            model.addAttribute("submittedQuestions", submittedQuestions);
            return "testing/result";
        }

        return "testing/test";
    }

    @PostMapping("/testing/{id}")
    public String check(Model model, @PathVariable Integer id,
            @RequestParam HashMap<String, String> results) {
        Testing testing = this.testingService.find(id);
        if (testing == null) {
            model.addAttribute("error", "Testing is not found");
            return "error/404";
        }

        Integer totalCorrect = 0;
        Integer totalIncorrect = testing.getTestingQuestions().size();
        HashMap<Integer, Answer> submittedAnswers = new HashMap<>();
        HashMap<Integer, Question> submittedQuestions = new HashMap<>();
        Set<TestingQuestion> testingQuestions = testing.getTestingQuestions();
        for (TestingQuestion testingQuestion : testingQuestions) {
            for (Map.Entry<String, String> map : results.entrySet()) {
                String key = map.getKey();
                String val = map.getValue();
                Integer questionId = Integer.parseInt(key);
                if (!questionId.equals(testingQuestion.getQuestion().getId())) {
                    continue;
                }

                Integer answerId = Integer.parseInt(val);
                for (Answer answer : testingQuestion.getQuestion().getAnswers()) {
                    if (answer.getId().equals(answerId)) {
                        submittedAnswers.put(answer.getId(), answer);
                        submittedQuestions.put(questionId, testingQuestion.getQuestion());
                        testingQuestion.setAnswer(answer);
                        if (answer.isCorrect()) {
                            totalCorrect++;
                            totalIncorrect--;
                            continue;
                        }
                    }
                }
            }
        }

        testing.setEndedAt(new Date());
        testing.setScore(totalCorrect);
        this.testingService.save(testing);

        model.addAttribute("submittedAnswers", submittedAnswers);
        model.addAttribute("submittedQuestions", submittedQuestions);
        model.addAttribute("testing", testing);

        return "testing/result";
    }

}
