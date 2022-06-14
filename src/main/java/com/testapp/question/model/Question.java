package com.testapp.question.model;

import com.testapp.answer.model.Answer;
import com.testapp.subject.model.Subject;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name="question")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer id;
    @NotEmpty(message = "Question can not be empty")
    @Size(min=3,max=1000)
    private String text;

//    @NotNull
//    @Min(1)
//    private Integer subjectId;

    @ManyToOne
    @JoinColumn(name = "subject_id" ,nullable = false)
    private Subject subject;


    @OneToMany(mappedBy="question")
    private Set<Answer> answers;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }



    public Set<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<Answer> answers) {
        this.answers = answers;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }
}
