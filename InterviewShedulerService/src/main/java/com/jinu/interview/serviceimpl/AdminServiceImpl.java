package com.jinu.interview.serviceimpl;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jinu.interview.dto.request.AddCandidateRequest;
import com.jinu.interview.dto.request.AddInterviewerRequest;
import com.jinu.interview.dto.request.AddUserRequest;
import com.jinu.interview.dto.response.Response;
import com.jinu.interview.model.Candidate;
import com.jinu.interview.model.InterviewSchedule;
import com.jinu.interview.model.Interviewer;
import com.jinu.interview.model.User;
import com.jinu.interview.model.UserRoles;
import com.jinu.interview.repository.CandidateRepository;
import com.jinu.interview.repository.InterviewScheduleRepository;
import com.jinu.interview.repository.InterviewerRepository;
import com.jinu.interview.repository.UserRepository;
import com.jinu.interview.repository.UserRolesRepository;
import com.jinu.interview.service.AdminService;
@Service
public class AdminServiceImpl implements AdminService {
	Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserRolesRepository userRolesRepository;
	@Autowired
	CandidateRepository candidateRepository;
	@Autowired
	InterviewerRepository interviewerRepository;
	@Autowired
	InterviewScheduleRepository interviewScheduleRepository;

	//@Autowired
	//BCryptPasswordEncoder encoder;

	public Response addUser(AddUserRequest request) {
		logger.info("Entering addUser");
		Response response;
		try {
			User user = null;
			List<UserRoles> userRoles = null;

			user = userRepository.getUserByUsername(request.getUsername());
			if (user != null) {
				response = createResponse("fail","userId already exists");
				return response;
			}
			user = createUserEnityFromDTO(request);
			user = userRepository.save(user);
			userRoles = ceateUserRoles(request,user);			
			userRolesRepository.saveAll(userRoles);
			response = createResponse("success", "User created with id :" + user.getUserId());
		} catch (Exception e) {
			response = createResponse("fail", e.getMessage());
		}
		logger.info("Exiting addUser");
		return response;

	}

	private List<UserRoles> ceateUserRoles(AddUserRequest request,User user) {
		logger.info("Entering addUser");
		List<UserRoles> userRoles = null;
		userRoles = request.getRoles().stream().map(e -> new UserRoles(user, e)).collect(Collectors.toList());
		logger.info("Exiting addUser");
		return userRoles;

	}

	private User createUserEnityFromDTO(AddUserRequest request) {
		logger.info("Entering createUserEnityFromDTO");
		User user = new User();
		user.setUsername(request.getUsername());
		//user.setPassword(encoder.encode(request.getPassword()));
		user.setPassword(request.getPassword());
		user.setEmail(request.getEmail());
		user.setAccountNonExpired(1);
		user.setAccountNonLocked(1);
		user.setEnabled(1);
		logger.info("Exiting createUserEnityFromDTO");
		return user;
	}

	public Response addCandidate(AddCandidateRequest request) {
		logger.info("Entering addCandidate");
		Response response;
		try {
			Candidate candidate = new Candidate();
			candidate.setName(request.getName());
			candidate.setEmail(request.getEmail());
			candidate.setPhone(request.getPhone());
			candidate.setStatus("new candidate");
			candidate = candidateRepository.save(candidate);
			if (request.getInterviewerId() == null || request.getInterviewerId().trim().isEmpty()) {
				logger.info("Exiting addCandidate");
				response = createResponse("success", "User created interview will be scheduled soon");
				return response;
			} else {
				LocalDate localdate = LocalDate.now();
				localdate = localdate.plusDays(2);
				Date date = Date.valueOf(localdate);
				String scheduledTime;
				while (true) {
					Optional<Interviewer> interviewer = interviewerRepository.findById(Long.parseLong(request.getInterviewerId()));
					if(!interviewer.isPresent()) {
						logger.info("Exiting addCandidate");
						response = createResponse("Success", "Interviwes is not present . Interview will be scheduled soon with existig interviewer soon");
						return response;
					}
					List<InterviewSchedule> interviewSchedules = interviewScheduleRepository.findByInterviewDateAndInterviewer(interviewer.get(), date);
					if (interviewSchedules.stream().map(InterviewSchedule::getTimeSchedule).count() >= 3) {
						continue;
					}
					
					InterviewSchedule interviewSchedule = new InterviewSchedule();
					interviewSchedule.setCandidate(candidate);
					interviewSchedule.setInterviewer(interviewer.get());
					interviewSchedule.setInterviewDate(date);
					interviewSchedule.setRound(1);
					scheduledTime = allocateTime(interviewSchedules);
					interviewSchedule.setTimeSchedule(scheduledTime);
					interviewScheduleRepository.save(interviewSchedule);
					break;
				}
				response = createResponse("success",
						"Canddate created and interview assigned at date: " + date + "time : " + scheduledTime);
				logger.info("Exiting addCandidate");
				return response;
			}
		} catch (Exception e) {
			response = createResponse("fail", e.getMessage());
		}
		logger.info("Exiting addCandidate");
		return response;
	}

	public Response addInterviewer(AddInterviewerRequest request) {
		logger.info("Entering addCandidate");
		Response response;
		User user = null;
		List<UserRoles> userRoles = null;
		user = userRepository.getUserByUsername(request.getUsername());
		if (user != null) {
			response = createResponse("fail","username already exists");
			return response;
		}
		user = createUserEnityFromDTO(request);
		user = userRepository.save(user);
		userRoles = ceateUserRoles(request,user);			
		userRolesRepository.saveAll(userRoles);
		Interviewer interviewer = new Interviewer();
		interviewer.setEmail(request.getEmail());
		interviewer.setName(request.getName());
		interviewer.setUserId(user.getUserId());
		interviewer = interviewerRepository.save(interviewer);
		response = createResponse("success","Interviewer created with id:"+interviewer.getInterviewerId());
		logger.info("Exiting addCandidate");
		return response;
	}
	private String allocateTime(List<InterviewSchedule> interviewSchedules) {
		logger.info("Entering allocateTime");
		String timeAllocated;
		List<String> timeScheluelist = interviewSchedules.stream().map(InterviewSchedule::getTimeSchedule)
				.collect(Collectors.toList());
		if (!timeScheluelist.contains("9-11")) {
			timeAllocated = "9-11";
		}
		else if (!timeScheluelist.contains("11-1")) {
			timeAllocated = "11-1";
		} else {
			timeAllocated =  "2-4";
		}
		logger.info("Exiting allocateTime");
		return timeAllocated;
	
	}
	private Response createResponse(String status,String message) {
		logger.info("Entering createResponse");
		Response response = new Response(status,message);
		logger.info("Exiting createResponse");
		return response;
	}
}
