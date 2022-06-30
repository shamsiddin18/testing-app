package com.testapp.testing.controller;

import com.testapp.answer.model.Answer;
import com.testapp.answer.repository.AnswerRepository;
import com.testapp.question.model.Question;
import com.testapp.question.repository.QuestionRepository;
import com.testapp.subject.model.Subject;
import com.testapp.subject.repository.SubjectRepository;
import com.testapp.testing.model.Testing;
import com.testapp.testing.model.TestingQuestion;
import com.testapp.testing.repository.TestingQuestionRepository;
import com.testapp.testing.repository.TestingRepository;
import com.testapp.user.model.UserModel;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.*;

@Controller
public class TestController {
    private final SubjectRepository subjectRepository;
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final TestingService testingService;
    private final TestingQuestionRepository testingQuestionRepository;
    private  final TestingRepository testingRepository;

    public TestController(
        SubjectRepository subjectRepository,
        AnswerRepository answerRepository,
        QuestionRepository questionRepository,
        TestingService testingService,
        TestingQuestionRepository testingQuestionRepository,
        TestingRepository testingRepository
    ){
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
        this.subjectRepository = subjectRepository;
        this.testingService = testingService;
        this.testingQuestionRepository=testingQuestionRepository;
        this.testingRepository=testingRepository;
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
            // TODO: try to use 1 query to get all questions
            if (question.getAnswers().size() <= 1) {
                continue;
            }

            testing.addQuestion(question);
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

        model.addAttribute("testing", testing);
        model.addAttribute("subject", testing.getSubject());
        model.addAttribute("questions", testing.getQuestions());

        return  "testing/test";
    }

    @PostMapping("/testing/{id}")
    public String check(Model model, @PathVariable Integer id, @RequestParam HashMap<String, String>results){
        Testing test= this.testingRepository.findById(id).orElse(null);
        if(test == null){
            return  "error";
        }

        Integer totalCorrect = 0;
        Integer totalIncorrect = test.getQuestions().size();
        HashMap<Integer, Answer> correctAnswers = new HashMap<>();
        HashMap<Integer, Answer> submittedAnswers = new HashMap<>();
        Set<Question> answered = new HashSet<>();
        Set<Question> notAnswered = new HashSet<>();
        Set<Question> questions = test.getQuestions();
        for (Question question : questions) {
            for (Answer answer : question.getAnswers()) {
                if (answer.isCorrect()) {
                    correctAnswers.put(question.getId(), answer);
                }
            }

            boolean found = false;
            for (Map.Entry<String, String> map : results.entrySet()) {
                String key = map.getKey();
                String val = map.getValue();
                Integer questionId = Integer.parseInt(key);
                if (!questionId.equals(question.getId())) {
                    continue;
                }

                found = true;
                answered.add(question);

                Integer answerId = Integer.parseInt(val);
                for (Answer answer : question.getAnswers()) {
                    if (answer.getId().equals(answerId)) {
                        submittedAnswers.put(answer.getId(), answer);
                        if (answer.isCorrect()) {
                            totalCorrect++;
                            totalIncorrect--;
                            continue;
                        }
                    }
                }
            }

            if (!found) {
                notAnswered.add(question);
            }
        }

//        Question question =this.questionRepository.findById(id).orElse(null);
//        Integer totalCorrect = 0;
//        Integer totalIncorrect = 0;
//        HashMap<String, Answer> correctAnswers = new HashMap<>();
//        HashMap<String, Answer> selectedAnswers = new HashMap<>();
//        HashMap<String, Question> questions = new HashMap<>();

//        for (Map.Entry<String, String> map : results.entrySet()){
//            Integer questionId = Integer.parseInt(map.getKey());
//            Integer answerId = Integer.parseInt(map.getValue());
//            List<Answer> answers = this.answerRepository.findByQuestionId(questionId);
//            for (Answer answer : answers) {
//                if (answer.isCorrect()) {
//                    correctAnswers.put(questionId.toString(), answer);
//                }
//
//                if (Objects.equals(answer.getId(), answerId)) {
//                    selectedAnswers.put(questionId.toString(), answer);
//                    if(answer.isCorrect()) {
//                        totalCorrect++;
//                        continue;
//                    }
//
//                    totalIncorrect++;
//                }
//            }
//        }

        model.addAttribute("totalCorrect", totalCorrect);
        model.addAttribute("totalIncorrect", totalIncorrect);
        model.addAttribute("answered", answered);
        model.addAttribute("notAnswered", notAnswered);
        model.addAttribute("correctAnswers", correctAnswers);
        model.addAttribute("selectedAnswers", submittedAnswers);
        model.addAttribute("testing", test);
        model.addAttribute("subject", test.getSubject());
        model.addAttribute("questions", test.getQuestions());

        return "testing/testing07";
    }

//    @GetMapping("/testing/view")
//    public String testingView(@PathVariable Integer id, Model model) {
//        Testing testing = testingService.find(id);
//        if (testing == null) {
//            return "error";
//        }
//
//        model.addAttribute("testing", testing);
//        model.addAttribute("subject", testing.getSubject());
//        model.addAttribute("questions", testing.getQuestions());
//
//        return  "testing/testing07";
//    }

}
