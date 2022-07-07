package com.testapp.testing.model;

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
    @OrderBy
    @JoinColumn(name = "subject_id", nullable = false, unique = false)
    private Subject subject;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @OneToMany(mappedBy = "testing", cascade = {CascadeType.ALL})
    @OrderBy
    private Set<TestingQuestion> testingQuestions;

    @Column(name = "ended_at")
    private Date endedAt;

    @Column(name="total_score")
    private Integer score;

    public Testing() {
        this.testingQuestions = new HashSet<>();
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

    public void addTestingQuestion(TestingQuestion testingQuestion) {
        this.testingQuestions.add(testingQuestion);
    }

    public Set<TestingQuestion> getTestingQuestions() {
        return testingQuestions;
    }

    public void setTestingQuestion(Set<TestingQuestion> testingQuestions) {
        this.testingQuestions = testingQuestions;
    }

    public void setTestingQuestions(Set<TestingQuestion> testingQuestions) {
        this.testingQuestions = testingQuestions;
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
