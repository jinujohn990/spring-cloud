package com.jinu.interview.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jinu.interview.dto.request.UpdateInterviewDetailsRequest;
import com.jinu.interview.dto.response.Response;
import com.jinu.interview.service.InterviewService;

@RestController
@RequestMapping("/interviewer")
public class InterviewController {
	
	@Autowired
	InterviewService interviewService;
	
	Logger logger = LoggerFactory.getLogger(InterviewController.class);

	@PostMapping("/updateInterviewDetails")
	public Response updateInterviewDetails(@RequestBody UpdateInterviewDetailsRequest request) {
		logger.info("Entering addCandidate");
		logger.info("Request recieved: "+request.toString());
		Response response = interviewService.updateInterviewDetails(request);
		logger.info("Response send: "+response.toString());
		logger.info("Exiting addCandidate");
		return response;
	}
}
