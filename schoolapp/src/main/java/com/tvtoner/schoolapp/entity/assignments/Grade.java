package com.tvtoner.schoolapp.entity.assignments;

import com.tvtoner.schoolapp.entity.Student;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "grade")
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "grade")
    private double grade;

    @ManyToOne
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    //Constructor
    public Grade(){

    }

    public Grade(Double grade, Assignment assignment, Student student) {
        this.grade = grade;
        this.assignment = assignment;
        this.student = student;
    }
}
