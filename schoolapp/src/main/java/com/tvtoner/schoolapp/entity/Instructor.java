package com.tvtoner.schoolapp.entity;

import com.tvtoner.schoolapp.security.entity.User;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "instructor")
public class Instructor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "instructor")
    private List<Course> courses;

    //Constructors
    public Instructor(){

    }

    public Instructor(User user){
        this.user = user;
    }

    // Getter and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    // add a single course
    public void addCourse(Course theCourse){

        if (this.courses == null){
            this.courses = new ArrayList<>();
        }

        this.courses.add(theCourse);
    }

    // toString() method

    @Override
    public String toString() {
        return "Instructor{" +
                "id=" + id +
                ", user=" + user +
                '}';
    }
}
