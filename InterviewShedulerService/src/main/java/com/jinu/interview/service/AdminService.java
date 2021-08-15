package com.jinu.interview.service;

import org.springframework.stereotype.Service;

import com.jinu.interview.dto.request.AddCandidateRequest;
import com.jinu.interview.dto.request.AddInterviewerRequest;
import com.jinu.interview.dto.request.AddUserRequest;
import com.jinu.interview.dto.response.Response;

@Service
public interface AdminService {
	public Response addUser(AddUserRequest request);

	public Response addCandidate(AddCandidateRequest request);

	public Response addInterviewer(AddInterviewerRequest request);
}
