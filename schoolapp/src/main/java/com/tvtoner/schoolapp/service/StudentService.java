package com.tvtoner.schoolapp.service;

import com.tvtoner.schoolapp.entity.Course;
import com.tvtoner.schoolapp.entity.Student;
import com.tvtoner.schoolapp.entity.assignments.Assignment;
import com.tvtoner.schoolapp.entity.assignments.Grade;

import java.util.List;

public interface StudentService {

    List<Course> getAllCourses();

    void addAndSaveCourse(long studentId, long courseId);

    List<Course> getStudentCourses(long studentId);

    void removeStudentCourse(long studentId, long courseId);

    Student getStudentById(long studentId);

    Course getCourseById(long courseId);

    Assignment getAssignmentById(long assignmentId);

    void saveTheGrade(Assignment theAssignment, Student theStudent, double theGrade);

    Grade getGradeByAssignmentAndStudent(long assignmentId, long studentId);
}
