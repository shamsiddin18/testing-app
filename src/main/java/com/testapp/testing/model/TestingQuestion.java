package com.testapp.testing.model;

import com.testapp.answer.model.Answer;
import com.testapp.question.model.Question;

import javax.persistence.*;

@Entity
@Table(name = "testing_question")
public class TestingQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne
    @JoinColumn(name = "testing_id", referencedColumnName = "id", nullable = false)
    private Testing testing;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @ManyToOne
    @JoinColumn(name = "answer_id")
    private Answer answer;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Testing getTesting() {
        return testing;
    }

    public void setTesting(Testing test) {
        this.testing = test;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }
}
