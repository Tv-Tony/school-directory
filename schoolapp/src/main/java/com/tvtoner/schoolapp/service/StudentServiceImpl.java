package com.tvtoner.schoolapp.service;

import com.tvtoner.schoolapp.dao.StudentRepository;
import com.tvtoner.schoolapp.dao.assignments.AssignmentRepository;
import com.tvtoner.schoolapp.dao.assignments.GradeRepository;
import com.tvtoner.schoolapp.entity.Course;
import com.tvtoner.schoolapp.entity.Student;
import com.tvtoner.schoolapp.entity.assignments.Assignment;
import com.tvtoner.schoolapp.entity.assignments.Grade;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tvtoner.schoolapp.dao.CourseRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService{

    private CourseRepository courseRepo;

    private StudentRepository studentRepo;

    private GradeRepository gradeRepo;

    private AssignmentRepository assignmentRepo;

    private EntityManager entityManager;


    @Autowired
    public StudentServiceImpl(CourseRepository courseRepo, StudentRepository studentRepo, GradeRepository gradeRepo, AssignmentRepository assignmentRepo, EntityManager entityManager) {
        this.courseRepo = courseRepo;
        this.studentRepo = studentRepo;
        this.gradeRepo = gradeRepo;
        this.assignmentRepo = assignmentRepo;
        this.entityManager = entityManager;
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepo.findAll();
    }

    @Override
    public void addAndSaveCourse(long studentId, long courseId) {

        Optional<Student> optionalStudent = studentRepo.findById(studentId);
        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();

            Optional<Course> optionalCourse = courseRepo.findById(courseId);
            if (optionalCourse.isPresent()) {
                Course course = optionalCourse.get();

                student.addCourse(course);
                studentRepo.save(student);
            } else {
                throw new IllegalArgumentException("Course not found for ID: " + courseId);
            }
        } else {
            throw new IllegalArgumentException("Student not found for ID: " + studentId);
        }
    }


    @Override
    public List<Course> getStudentCourses(long studentId) {
        Student tempStudent = getStudentById(studentId);

        return tempStudent.getCourses();
    }

    @Override
    @Transactional
    public void removeStudentCourse(long studentId, long courseId) {

        Student tempStudent = getStudentById(studentId);

        tempStudent.removeCourseById(courseId);

        studentRepo.save(tempStudent);
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
    public Course getCourseById(long courseId) {

        Optional<Course> tempCourse = courseRepo.findById(courseId);

        if (tempCourse.isPresent()){
            return tempCourse.get();
        }
        else {
            throw new RuntimeException("Error finding Course");
        }
    }

    @Override
    public Assignment getAssignmentById(long assignmentId) {

        Optional<Assignment> tempAssignment = assignmentRepo.findById(assignmentId);

        if (tempAssignment.isPresent()){
            return tempAssignment.get();
        }
        else {
            throw new RuntimeException("Error finding Assignment");
        }

    }

    @Override
    @Transactional
    public void saveTheGrade(Assignment theAssignment, Student theStudent, double theGrade) {

        Grade savedgrade = new Grade(theGrade, theAssignment, theStudent);

        gradeRepo.save(savedgrade);
    }

    @Override
    public Grade getGradeByAssignmentAndStudent(long assignmentId, long studentId) {

        TypedQuery<Grade> query = entityManager.createQuery(
                "SELECT g FROM Grade g " +
                        "JOIN FETCH g.assignment a " +
                        "JOIN FETCH g.student s " +
                        "WHERE a.id = :assignmentId AND s.id = :studentId", Grade.class
        );

        query.setParameter("assignmentId", assignmentId);
        query.setParameter("studentId", studentId);
        query.setMaxResults(1);

        List<Grade> results = query.getResultList();

        if (results.isEmpty()) {
            return null;
        } else {
            return results.get(0); // Return the first grade found
        }
    }
}
