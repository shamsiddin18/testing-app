package com.testapp.testing.model;

import com.testapp.question.model.Question;
import com.testapp.subject.model.Subject;
import com.testapp.user.model.UserModel;
import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "testing")
public class Testing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, unique = false)
    private UserModel user;

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false, unique = false)
    private Subject subject;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @OneToMany(cascade = { CascadeType.ALL })
    @OrderBy
    @JoinTable(name = "testing_question", joinColumns = {
            @JoinColumn(name = "testing_id", referencedColumnName = "id") }, inverseJoinColumns = {
                    @JoinColumn(name = "question_id", referencedColumnName = "id") })
    private Set<Question> questions;

    @Column(name="ended_at", nullable = true)
    private Date endedAt;

    @Column(name="total_score", nullable = true)
    private Integer score;

    public Testing() {
        this.questions = new HashSet<Question>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Set<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
    }

    public void addQuestion(Question question) {
        this.questions.add(question);
    }

    public Date getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(Date endedAt) {
        this.endedAt = endedAt;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
