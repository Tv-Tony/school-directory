package com.tvtoner.schoolapp.service.helper;

import com.tvtoner.schoolapp.entity.assignments.Grade;
import lombok.Getter;
import lombok.Setter;
import com.tvtoner.schoolapp.entity.assignments.Assignment;

@Getter
@Setter
public class AssignmentGradeInfo {

    private Assignment assignment;

    private Grade grade;

    // Constructor

    public AssignmentGradeInfo(Assignment assignment, Grade grade) {
        this.assignment = assignment;
        this.grade = grade;
    }
}
