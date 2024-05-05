package com.tvtoner.schoolapp.entity;

import com.tvtoner.schoolapp.security.entity.User;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="student")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.DETACH, CascadeType.REFRESH,
                    CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "course_student",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> courses;

    public Student() {
        // Default constructor
    }

    public Student(User user) {
        this.user = user;
    }

    // Getter and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    // add singe course
    public void addCourse(Course theCourse){

        if (this.courses == null){
            this.courses = new ArrayList<>();
        }

        this.courses.add(theCourse);
    }

    // toString() Method


    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", user=" + user +
                '}';
    }
}
