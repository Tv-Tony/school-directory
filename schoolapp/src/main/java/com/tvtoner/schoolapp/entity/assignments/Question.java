package com.tvtoner.schoolapp.entity.assignments;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "question")
@Getter
@Setter
@ToString
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "question_text")
    private String question;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<Answer> answers;


    @ManyToOne()
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;
    //Constructors

    public Question() {
    }

    public Question(String question) {
        this.question = question;
    }

    //add a single answer
    public void addAnswer(Answer theAnswer){

        if (answers == null){
            answers = new ArrayList<>();
        }

        answers.add(theAnswer);
    }
}
