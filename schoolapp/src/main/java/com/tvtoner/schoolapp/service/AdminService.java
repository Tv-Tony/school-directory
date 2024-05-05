package com.tvtoner.schoolapp.service;

import com.tvtoner.schoolapp.entity.Course;
import com.tvtoner.schoolapp.entity.Instructor;
import com.tvtoner.schoolapp.entity.Student;
import org.springframework.stereotype.Service;

import java.util.List;


public interface AdminService {

    List<Instructor> getAllInstructors();

    List<Student> getAllStudents();

    Student getStudentById(long studentId);

    void updateStudent(Student theStudent);

    void deleteStudent(long studentId);

    Instructor getInstructorById(long instructorId);

    void updateInstructor(Instructor theInstructor);

    void deleteInstructor(long instructorId);

    List<Course> getAllCourses();

    Course getCourseById(long theId);

    void deleteCourse(long courseId);

    void updateCourse(Course theCourse);

    void addCourse(String title);

}
