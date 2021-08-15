package com.jinu.interview.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
@Data
@Entity
@Table(name = "INTERVIEW_SCHEDULE")
public class InterviewSchedule {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "interview_schedule_id" ,unique=true,nullable = false)
    private long interviewScheduleId;
	@ManyToOne
	@JoinColumn(name="candidateId" ,nullable = false)
	private Candidate candidate;
	@ManyToOne
	@JoinColumn(name="interviewerId" ,nullable = false)
	private Interviewer interviewer;
	private java.sql.Date interviewDate;
	private String timeSchedule;
	private int round;
	private String status;
	private String remarks;
	
}
