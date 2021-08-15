package com.jinu.interview.dto.request;

import lombok.Data;
@Data
public class AddCandidateRequest {
	private String name;
	private String email;
	private String phone;
	private String interviewerId;
}
