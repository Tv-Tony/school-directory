package com.tvtoner.schoolapp.entity.assignments;

import com.tvtoner.schoolapp.entity.Course;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "assignment")
@Getter
@Setter
@ToString
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "title")
    private String title;

    @ManyToOne()
    @JoinColumn(name = "course_id")
    private Course course;

    @OneToMany(mappedBy = "assignment", cascade = CascadeType.ALL)
    private List<Question> questions;

    //Constructos


    public Assignment() {
    }

    public Assignment(String title) {
        this.title = title;
    }

    //Add a single assignment
    public void addQuestion(Question theQuestion){

        if (questions == null){
            questions = new ArrayList<>();
        }

        questions.add(theQuestion);
    }

    //get a question by its id
    public Question getQuestionById(long id){

        try{
            for (Question e : questions){

                if (e.getId() == id){
                    return e;
                }
            }
        }
        catch (Exception e){
            System.out.println("Error finding question" + e);
            return null;
        }
        return null;
    }
}
