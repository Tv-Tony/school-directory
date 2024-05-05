package com.tvtoner.schoolapp.service;

import com.tvtoner.schoolapp.dao.CourseRepository;
import com.tvtoner.schoolapp.dao.InstructorRepository;
import com.tvtoner.schoolapp.dao.StudentRepository;
import com.tvtoner.schoolapp.entity.Course;
import com.tvtoner.schoolapp.entity.Instructor;
import com.tvtoner.schoolapp.entity.Student;
import com.tvtoner.schoolapp.security.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService{

    private final StudentRepository studentRepo;

    private final InstructorRepository instructorRepo;

    private final CourseRepository courseRepo;

    @Autowired
    public AdminServiceImpl(StudentRepository studentRepo, InstructorRepository instructorRepo, CourseRepository courseRepo) {
        this.studentRepo = studentRepo;
        this.instructorRepo = instructorRepo;
        this.courseRepo = courseRepo;
    }

    @Override
    public List<Instructor> getAllInstructors() {

        return instructorRepo.findAll();
    }

    @Override
    public List<Student> getAllStudents() {

        return studentRepo.findAll();
    }

    @Override
    public Student getStudentById(long studentId) {

        Optional<Student> tempStudent = studentRepo.findById(studentId);

        if (tempStudent.isPresent()){
            return tempStudent.get();
        }
        else {
            throw new RuntimeException("Error finding Student");
        }
    }

    @Override
    public void updateStudent(Student theStudent) {

        try{
            studentRepo.save(theStudent);
        }
        catch (Exception e){
            System.out.println("Error updating Student " +theStudent.getId() + "\n" + e);
        }
    }

    @Override
    public void deleteStudent(long studentId) {

        // retrieve the student
        Student tempStudent = getStudentById(studentId);

        User user = tempStudent.getUser();

        // delete the student
        try {
            studentRepo.delete(tempStudent);
        }
        catch (Exception e){
            System.out.println("Unable to delete Student " +tempStudent.getId() +"\n" + e);
        }
    }

    @Override
    public Instructor getInstructorById(long instructorId) {

        Optional<Instructor> tempInstructor = instructorRepo.findById(instructorId);

        if (tempInstructor.isPresent()){
            return tempInstructor.get();
        }
        else {
            throw new RuntimeException("Error finding Instructor");
        }
    }

    @Override
    public void updateInstructor(Instructor theInstructor) {

        try {
            instructorRepo.save(theInstructor);
        }
        catch (Exception e){
            System.out.println("Unable to update Instructor " +theInstructor.getId() +"\n" + e);
        }
    }

    @Override
    public void deleteInstructor(long instructorId) {

        // retrieve the instructor
        Instructor tempInstructor = getInstructorById(instructorId);

        // delete the instructor
        try{
            instructorRepo.delete(tempInstructor);
        }
        catch (Exception e){
            System.out.println("Unable to delete Instructor " + tempInstructor.getId() +"\n" + e);
        }


    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepo.findAll();
    }

    @Override
    public Course getCourseById(long theId) {

        Optional<Course> tempCourse = courseRepo.findById(theId);

        if (tempCourse.isPresent()){
            return tempCourse.get();
        }
        else {
            throw new RuntimeException("Error finding course");
        }
    }

    @Override
    public void deleteCourse(long courseId) {

        Course tempCourse = getCourseById(courseId);

        courseRepo.delete(tempCourse);

        System.out.println("Course Deleted");

    }

    @Override
    public void updateCourse(Course theCourse) {
        courseRepo.save(theCourse);
    }

    @Override
    public void addCourse(String title) {
        Course newCourse = new Course();
        newCourse.setTitle(title);
        courseRepo.save(newCourse);
    }
}
