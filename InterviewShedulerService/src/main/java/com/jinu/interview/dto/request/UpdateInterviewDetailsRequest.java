package com.jinu.interview.dto.request;
import lombok.Data;
@Data
public class UpdateInterviewDetailsRequest {
    private long interviewScheduleId;
	//private long candidateId;
	//private String interviewerId;
	//private java.sql.Date interviewDate;
	//private String timeSchedule;
	//private int round;
	private String remarks;
	private String status;
	//private boolean proceedToNextRound;
}
