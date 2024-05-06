package com.tvtoner.schoolapp.dao.assignments;

import com.tvtoner.schoolapp.entity.assignments.Answer;
import com.tvtoner.schoolapp.entity.assignments.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

}
