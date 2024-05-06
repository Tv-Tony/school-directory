package com.tvtoner.schoolapp.service;

import com.tvtoner.schoolapp.dao.assignments.AnswerRepository;
import com.tvtoner.schoolapp.dao.assignments.AssignmentRepository;
import com.tvtoner.schoolapp.dao.assignments.QuestionRepository;
import com.tvtoner.schoolapp.dao.CourseRepository;
import com.tvtoner.schoolapp.entity.Course;
import com.tvtoner.schoolapp.entity.assignments.Assignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class InstructorServiceImpl implements InstructorService {

    private final AnswerRepository answerRepo;

    private final AssignmentRepository assignmentRepo;

    private final QuestionRepository questionRepo;

    private final CourseRepository courseRepo;

    @Autowired
    public InstructorServiceImpl(AnswerRepository answerRepo, AssignmentRepository assignmentRepo, QuestionRepository questionRepo, CourseRepository courseRepo) {
        this.answerRepo = answerRepo;
        this.assignmentRepo = assignmentRepo;
        this.questionRepo = questionRepo;
        this.courseRepo = courseRepo;
    }

    @Override
    public List<Course> showInstructorCourses(long instructorId) {
        return null;
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
    @Transactional
    public void saveAssignment(Assignment theAssignment) {
        assignmentRepo.save(theAssignment);
    }

    @Override
    public void createAssignment() {

    }

    @Override
    @Transactional
    public void deleteAssignment(Assignment theAssignment) {
        assignmentRepo.delete(theAssignment);
    }

    @Override
    public Assignment getAssignmentById(Long id) {

        Optional<Assignment> tempAssignment = assignmentRepo.findById(id);

        if (tempAssignment.isPresent()){
            return tempAssignment.get();
        }
        else {
            throw new RuntimeException("Error finding Assignment");
        }
    }

    @Override
    public void createQuestion() {

    }

    @Override
    public void deleteQuestion() {

    }

    @Override
    public void createAnswer() {

    }

    @Override
    public void deleteAnswer() {

    }
}
