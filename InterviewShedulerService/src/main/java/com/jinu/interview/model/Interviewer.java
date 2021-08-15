package com.jinu.interview.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
@Data
@Entity
@Table(name = "INTERVIEWER")
public class Interviewer {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "interviewer_id" ,unique=true,nullable = false)
    private long interviewerId;
    private String name;
    private String email;
    private long userId;
	
}
