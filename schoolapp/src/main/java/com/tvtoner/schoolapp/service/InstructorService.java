package com.tvtoner.schoolapp.service;

import com.tvtoner.schoolapp.entity.Course;
import com.tvtoner.schoolapp.entity.assignments.Assignment;

import java.util.List;

public interface InstructorService {

    List<Course> showInstructorCourses(long instructorId);

    Course getCourseById(long theId);

    void saveAssignment(Assignment theAssignment);

    void createAssignment();

    void deleteAssignment(Assignment theAssignment);

    Assignment getAssignmentById(Long id);

    void createQuestion();

    void deleteQuestion();

    void createAnswer();

    void deleteAnswer();


}
