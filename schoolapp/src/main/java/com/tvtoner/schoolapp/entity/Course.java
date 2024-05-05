package com.tvtoner.schoolapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor  // Lombok's annotation for generating a no-argument constructor
@Entity
@ToString(of = {"id", "title"}) // Lombok's annotation to customize toString() method
@Table(name = "course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long id;

    @Column(name = "title")
    private String title;

    // Many courses can have one instructor
    @ManyToOne()
    @JoinColumn(name = "instructor_id")
    private Instructor instructor;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.DETACH, CascadeType.REFRESH,
                    CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "course_student",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<Student> students;

    // Constructor with Instructor
    public Course(String title, Instructor instructor) {
        this.title = title;
        this.instructor = instructor;
    }

    //add single student
    public void addStudent(Student theStudent){

        if (this.students == null){
            this.students = new ArrayList<>();
        }

        this.students.add(theStudent);
    }


}
