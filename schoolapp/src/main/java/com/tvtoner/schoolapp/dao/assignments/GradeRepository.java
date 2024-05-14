package com.tvtoner.schoolapp.dao.assignments;

import com.tvtoner.schoolapp.entity.assignments.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {

}
