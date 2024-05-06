package com.tvtoner.schoolapp.dao.assignments;

import com.tvtoner.schoolapp.entity.assignments.Assignment;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
}
