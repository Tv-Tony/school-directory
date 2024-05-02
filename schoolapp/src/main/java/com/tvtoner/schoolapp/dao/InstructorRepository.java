package com.tvtoner.schoolapp.dao;

import com.tvtoner.schoolapp.entity.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstructorRepository extends JpaRepository<Instructor, Long> {

}
