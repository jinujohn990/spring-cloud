package com.jinu.interview.serviceimpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;

import com.jinu.interview.dto.request.AddCandidateRequest;
import com.jinu.interview.dto.request.AddInterviewerRequest;
import com.jinu.interview.dto.request.AddUserRequest;
import com.jinu.interview.dto.response.Response;
import com.jinu.interview.model.Candidate;
import com.jinu.interview.model.Interviewer;
import com.jinu.interview.model.User;
import com.jinu.interview.model.UserRoles;
import com.jinu.interview.repository.CandidateRepository;
import com.jinu.interview.repository.InterviewScheduleRepository;
import com.jinu.interview.repository.InterviewerRepository;
import com.jinu.interview.repository.UserRepository;
import com.jinu.interview.repository.UserRolesRepository;
import com.jinu.interview.serviceimpl.AdminServiceImpl;

@ExtendWith(MockitoExtension.class)
class AdminServiceImplTest {
	
	@InjectMocks
	private AdminServiceImpl adminService;	
	@Mock
	private UserRepository userRepository;
	@Mock
	private UserRolesRepository userRolesRepository;
	@Mock
	CandidateRepository candidateRepository;
	@Mock
	InterviewerRepository interviewerRepository;
	@Mock
	InterviewScheduleRepository interviewScheduleRepository;

	@Test
	public void addUser() {
		AddUserRequest request = createAddUserRequest();
		Mockito.when(userRepository.getUserByUsername("test")).thenReturn(new User());
		Response response = adminService.addUser(request);		
		assertEquals("fail",response.getStatus());
		assertEquals("userId already exists", response.getMessage());
	}
	@Test
	public void addUserTest() {
		AddUserRequest request = createAddUserRequest();
		User user = createUser();
		Mockito.when(userRepository.getUserByUsername("test")).thenReturn(null);
		Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(setUserId(user));
		//Mockito.when(userRolesRepository.save(Mockito.any(UserRoles.class))).thenReturn(new UserRoles());
		Response response = adminService.addUser(request);		
		assertEquals("success",response.getStatus());
		//assertEquals("User created with id :10", response.getMessage());
	}

	@Test
	public void addCandidateTest(){
		AddCandidateRequest request = createAddCandidateRequest();
		Candidate candidate = createCandidate(request);
		Mockito.when(candidateRepository.save(Mockito.any(Candidate.class))).thenReturn(candidate);
		Response response = adminService.addCandidate(request);
		assertEquals("success",response.getStatus());
		
	}
	@Test
	public void addInterviewerTest() throws Exception {
		AddInterviewerRequest request = createAddInterviewerRequest();
		Mockito.when(userRepository.getUserByUsername("test")).thenReturn(new User());
		Response response = adminService.addInterviewer(request);		
		assertEquals("fail",response.getStatus());
		assertEquals("username already exists", response.getMessage());
	}
	@Test
	public void addInterviewerSuccessTest() throws Exception {
		AddInterviewerRequest request = createAddInterviewerRequest();
		User user = createUser();
		Mockito.when(userRepository.getUserByUsername("test")).thenReturn(null);
		Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(setUserId(user));
		//Mockito.when(userRolesRepository.save(Mockito.any(UserRoles.class))).thenReturn(createUserRoles());
		Mockito.when(interviewerRepository.save(Mockito.any(Interviewer.class))).thenReturn(new Interviewer());
		Response response = adminService.addInterviewer(request);		
		assertEquals("success",response.getStatus());
	}
	
	private Candidate createCandidate(AddCandidateRequest request) {
		Candidate candidate = new Candidate();
		candidate.setEmail("test.gmail.com");
		candidate.setName("Test");
		candidate.setPhone("7736153969");
		return null;
	}
	private AddUserRequest createAddUserRequest() {
		AddUserRequest request = new AddUserRequest();
		request.setEmail("test.gmail.com");
		request.setPassword("123");
		request.setUsername("test");
		List<String> roles = new ArrayList<String>();
		roles.add("ROLE_ADMIN");
		request.setRoles(roles);
		return request;
	}
	private User createUser() {
		User user = new User();
		user.setEmail("test.gmail.com");
		user.setPassword("123");
		user.setUsername("test");
		user.setAccountNonExpired(1);
		user.setAccountNonLocked(1);
		user.setEnabled(1);
		return user;
		
	}
	private User setUserId(User user) {
		user.setUserId(10);
		return user;
	}
	
	private AddCandidateRequest createAddCandidateRequest() {
		AddCandidateRequest request = new AddCandidateRequest();
		request.setName("Test");
		request.setEmail("test.gmail.com");
		request.setPhone("7736153969");
		return request;
	}
	private AddInterviewerRequest createAddInterviewerRequest() {
		AddInterviewerRequest request = new AddInterviewerRequest();
		request.setEmail("test.gmail.com");
		request.setPassword("123");
		request.setUsername("test");
		List<String> roles = new ArrayList<String>();
		roles.add("ROLE_INTERVIEWER");
		request.setRoles(roles);
		request.setName("Test");
		return request;
	}
	private UserRoles createUserRoles() {
		UserRoles userRoles  = new UserRoles();
		userRoles.setUser(createUser());
		userRoles.setRole("ROLE_INTERVIEWER");
		userRoles.setUserRoleId(3);
		return userRoles;
	}
}
