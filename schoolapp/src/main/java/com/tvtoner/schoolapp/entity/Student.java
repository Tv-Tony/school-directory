package com.tvtoner.schoolapp.entity;

import com.tvtoner.schoolapp.entity.assignments.Grade;
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

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {CascadeType.DETACH, CascadeType.REFRESH,
                    CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "course_student",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> courses;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Grade> grades;



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

    public List<Grade> getGrades() {
        return grades;
    }

    public void setGrades(List<Grade> grades) {
        this.grades = grades;
    }

    // Add Grade
    public void addGrade(Grade theGrade){

        if (this.grades == null){
            this.grades = new ArrayList<>();
        }

        this.grades.add(theGrade);
    }

    // add singe course
    public void addCourse(Course theCourse){

        if (this.courses == null){
            this.courses = new ArrayList<>();
        }

        this.courses.add(theCourse);
    }

    // remove a course
    public void removeCourseById(long courseId) {
        // Find the course with the given ID in the courses set and remove it
        courses.removeIf(course -> course.getId() == courseId);
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
