package com.tvtoner.schoolapp.dao.assignments;

import com.tvtoner.schoolapp.entity.assignments.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

}
