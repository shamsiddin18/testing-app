package com.testapp.testing.model;


import com.testapp.subject.model.Subject;
import com.testapp.user.model.UserModel;

import javax.persistence.*;
import java.util.TimeZone;

@Entity
@Table(name="testing")
public class Testing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel user;

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    private TimeZone time;

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

    public TimeZone getTime() {
        return time;
    }

    public void setTime(TimeZone time) {
        this.time = time;
    }
}
