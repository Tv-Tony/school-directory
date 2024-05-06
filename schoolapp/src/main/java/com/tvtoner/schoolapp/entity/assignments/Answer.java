package com.tvtoner.schoolapp.entity.assignments;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "answer")
@Getter
@Setter
@ToString
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "answer_text")
    private String answer;

    @Column(name = "is_correct")
    private boolean questionValue;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "question_id")
    private Question question;

    //Constructors
    public Answer(){

    }

    public Answer(String answer, boolean questionValue){
        this.answer = answer;
        this.questionValue = questionValue;
    }




}
