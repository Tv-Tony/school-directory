package com.tvtoner.schoolapp.entity;

import com.tvtoner.schoolapp.security.entity.User;
import jakarta.persistence.*;

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

    // toString() method

    @Override
    public String toString() {
        return "Instructor{" +
                "id=" + id +
                ", user=" + user +
                '}';
    }
}
