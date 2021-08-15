package com.jinu.interview.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jinu.interview.dto.request.AddCandidateRequest;
import com.jinu.interview.dto.request.AddInterviewerRequest;
import com.jinu.interview.dto.request.AddUserRequest;
import com.jinu.interview.dto.response.Response;
import com.jinu.interview.service.AdminService;

@RestController
@Secured("hasAuthority('ROLE_ADMIN')")
@RequestMapping("/admin")
public class AdminController {
	Logger logger = LoggerFactory.getLogger(AdminController.class);
	@Autowired
	AdminService adminService;

	@PostMapping("/addUser")
	public Response addUser(@RequestBody AddUserRequest request) {
		logger.info("Entering addUser");
		logger.info("Request recieved: "+request.toString());
		Response response = adminService.addUser(request);
		logger.info("Response send: "+response.toString());
		logger.info("Exiting addUser");
		return response;
	}
	
	@PostMapping("/addCandidate")
	public Response addCandidate(@RequestBody AddCandidateRequest request) {
		logger.info("Entering addCandidate");
		logger.info("Request recieved: "+request.toString());
		Response response = adminService.addCandidate(request);
		logger.info("Response send: "+response.toString());
		logger.info("Exiting addCandidate");
		return response;
	}
	
	@PostMapping("/addInterviewer")
	public Response addInterviewer(@RequestBody AddInterviewerRequest request) {
		logger.info("Entering addInterviewer");
		logger.info("Request recieved: "+request.toString());
		Response response =  adminService.addInterviewer(request);
		logger.info("Response send: "+response.toString());
		logger.info("Exiting addInterviewer");
		return response;
	}
}
