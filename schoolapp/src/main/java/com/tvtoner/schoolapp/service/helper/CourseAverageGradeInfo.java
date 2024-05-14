package com.tvtoner.schoolapp.service.helper;

import com.tvtoner.schoolapp.entity.Course;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseAverageGradeInfo {

    private Course course;
    private Double averageGrade;

    // Constructors
    public CourseAverageGradeInfo(Course course, Double averageGrade) {
        this.course = course;
        this.averageGrade = averageGrade;
    }
}
