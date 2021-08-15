package com.jinu.interview.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EmpIoyeeidGroup {
	
	/*public EmpIoyeeidGroup(String interviewerId, long count) {
		super();
		this.interviewerId = interviewerId;
		this.count = count;
	}*/
	public EmpIoyeeidGroup(Interviewer interviewer, long count) {
		super();
		this.interviewer = interviewer;
		this.count = count;
	}
	private Interviewer interviewer;
	private long count;
}
