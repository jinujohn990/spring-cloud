package com.jinu.interview.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.jinu.interview.model.Interviewer;
@Repository
public interface InterviewerRepository extends JpaRepository<Interviewer, Long>{
	   @Query("SELECT u FROM Interviewer u WHERE u.interviewerId NOT IN ?1")
	   List<Interviewer> findByEmpIdNotIn(Collection<Long> empids);
	   
	   @Query(value="SELECT * FROM Interviewer ORDER BY RAND() LIMIT 1", nativeQuery = true)
	   Interviewer findRandomInterviewer();
}


