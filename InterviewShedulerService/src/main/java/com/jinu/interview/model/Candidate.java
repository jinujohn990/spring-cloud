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
@Table(name = "CANDIDATE")
public class Candidate {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "candidate_id" ,unique=true,nullable = false)
    private long candidateId;
    private String name;
    @Column(name = "email" ,unique=true,nullable = false)
    private String email;
    private String phone;
    private String status;

}
