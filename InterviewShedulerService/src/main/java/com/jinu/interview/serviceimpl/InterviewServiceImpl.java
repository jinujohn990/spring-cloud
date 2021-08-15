package com.jinu.interview.serviceimpl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jinu.interview.dto.request.UpdateInterviewDetailsRequest;
import com.jinu.interview.dto.response.Response;
import com.jinu.interview.model.Candidate;
import com.jinu.interview.model.InterviewSchedule;
import com.jinu.interview.repository.CandidateRepository;
import com.jinu.interview.repository.InterviewScheduleRepository;
import com.jinu.interview.service.InterviewService;
@Service
public class InterviewServiceImpl implements InterviewService {
	
	Logger logger = LoggerFactory.getLogger(InterviewServiceImpl.class);
	
	@Autowired
	InterviewScheduleRepository interviewScheduleRepository;
	
	@Autowired
	CandidateRepository candidateRepository;
	
	public Response updateInterviewDetails(UpdateInterviewDetailsRequest request) {
		logger.info("Entering updateInterviewDetails");
		Response response;
		try {
			Optional<InterviewSchedule> interviewScheduleOptional = interviewScheduleRepository.findById(request.getInterviewScheduleId());
			if(interviewScheduleOptional.isPresent()) {
				InterviewSchedule interviewSchedule = interviewScheduleOptional.get();
				interviewSchedule.setRemarks(request.getRemarks());
				interviewSchedule.setStatus(request.getStatus());
				Optional<Candidate> candidateOptional = candidateRepository.findById(interviewSchedule.getCandidate().getCandidateId());
				Candidate candidate = candidateOptional.get();
				if (request.getStatus().equalsIgnoreCase("passed")) {
					if (interviewSchedule.getRound() == 1) {
						candidate.setStatus("roundone_completed");
					} else if (interviewSchedule.getRound() == 2) {
						candidate.setStatus("roundtwo_completed");
					} else if (interviewSchedule.getRound() == 3) {
						candidate.setStatus("roundthree_completed");
					}

				} else {
					if (interviewSchedule.getRound() == 1) {
						candidate.setStatus("roundone_not_cleared");
					} else if (interviewSchedule.getRound() == 2) {
						candidate.setStatus("roundtwo_not_cleared");
					} else if (interviewSchedule.getRound() == 3) {
						candidate.setStatus("roundthree_not_cleared");
					}
				}
				candidateRepository.save(candidate);
				response = createResponse("Success","Interview details updated for round "+interviewSchedule.getRound());
			}else {
				response = createResponse("Fail","Invalid interviewScheduleId");
			}
			
		} catch (Exception e) {
			response = createResponse("fail", e.getMessage());
		}
		logger.info("Exiting updateInterviewDetails");
		return response;
	}
	
	private Response createResponse(String status,String message) {
		logger.info("Entering createResponse");
		Response response = new Response(status,message);
		logger.info("Exiting createResponse");
		return response;
	}
	
}
