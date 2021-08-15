package com.jinu.interview.service;

import com.jinu.interview.dto.request.UpdateInterviewDetailsRequest;
import com.jinu.interview.dto.response.Response;

public interface InterviewService {
	public Response updateInterviewDetails(UpdateInterviewDetailsRequest request);
}
