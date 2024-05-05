package com.tvtoner.schoolapp.dao;

import com.tvtoner.schoolapp.entity.Course;
import com.tvtoner.schoolapp.entity.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

}
