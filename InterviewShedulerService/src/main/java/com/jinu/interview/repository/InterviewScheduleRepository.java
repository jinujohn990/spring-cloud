package com.jinu.interview.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.jinu.interview.model.Candidate;
import com.jinu.interview.model.EmpIoyeeidGroup;
import com.jinu.interview.model.InterviewSchedule;
import com.jinu.interview.model.Interviewer;
@Repository
public interface InterviewScheduleRepository extends JpaRepository<InterviewSchedule, Long>  {
	@Query("SELECT c FROM InterviewSchedule c WHERE c.interviewDate = :date")
	List<InterviewSchedule> findByInterviewDate(Date date);
	
	@Query("select new com.jinu.interview.model.EmpIoyeeidGroup(v.interviewer, COUNT(v.timeSchedule)) FROM InterviewSchedule v  where v.interviewDate=:date GROUP BY v.interviewer  HAVING COUNT(v.timeSchedule) < 3")
	public List<EmpIoyeeidGroup> groupByInterviewer(Date date); 
	
	@Query("SELECT c FROM InterviewSchedule c WHERE c.interviewDate = :date AND c.interviewer = :interviewer")
	List<InterviewSchedule> findByInterviewDateAndInterviewer(Interviewer interviewer,Date date);
	
	List<InterviewSchedule> findByCandidateAndRound(Candidate candidate,int round);
	
}
