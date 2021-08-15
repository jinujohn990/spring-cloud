package com.jinu.interview.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinu.interview.controller.AdminController;
import com.jinu.interview.dto.request.AddCandidateRequest;
import com.jinu.interview.dto.request.AddInterviewerRequest;
import com.jinu.interview.dto.request.AddUserRequest;
import com.jinu.interview.dto.response.Response;
import com.jinu.interview.serviceimpl.AdminServiceImpl;

//@SpringBootTest
//@WebMvcTest(AdminController.class)
@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

	MockMvc mvc;
	@Mock
	AdminServiceImpl adminService;
	@InjectMocks
	AdminController adminController;

	@BeforeEach
	public void setup() {
		JacksonTester.initFields(this, new ObjectMapper());
		mvc = MockMvcBuilders.standaloneSetup(adminController).build();
	}

	@Test
	public void addUserTest() throws Exception {
		AddUserRequest request = createAddUserRequest();
		Response response = createResponse("success", "User created ");
		Mockito.when(adminService.addUser(Mockito.any(AddUserRequest.class))).thenReturn(response);
		mvc.perform(post("/admin/addUser").contentType(MediaType.APPLICATION_JSON).content(asJsonString(request)))
				.andExpect(status().isOk());
	}

	@Test
	public void addCandidateTest() throws Exception {
		AddCandidateRequest request = createAddCandidateRequest();
		Response response = createResponse("success", "User created ");
		Mockito.when(adminService.addCandidate(Mockito.any(AddCandidateRequest.class))).thenReturn(response);
		mvc.perform(post("/admin/addCandidate").contentType(MediaType.APPLICATION_JSON).content(asJsonString(request)))
				.andExpect(status().isOk());
	}

	@Test
	public void addInterviewerTest() throws Exception {
		AddInterviewerRequest request = createAddInterviewerRequest();
		Response response = createResponse("success", "User created ");
		Mockito.when(adminService.addInterviewer(Mockito.any(AddInterviewerRequest.class))).thenReturn(response);
		mvc.perform(
				post("/admin/addInterviewer").contentType(MediaType.APPLICATION_JSON).content(asJsonString(request)))
				.andExpect(status().isOk());
	}
	
	
	
	

	private AddInterviewerRequest createAddInterviewerRequest() {
		AddInterviewerRequest request = new AddInterviewerRequest();
		request.setEmail("test.gmail.com");
		request.setPassword("123");
		request.setUsername("test");
		List<String> roles = new ArrayList<String>();
		roles.add("ROLE_ADMIN");
		request.setRoles(roles);
		request.setName("Test");
		return request;
	}

	private AddCandidateRequest createAddCandidateRequest() {
		AddCandidateRequest request = new AddCandidateRequest();
		request.setName("Test");
		request.setEmail("test.gmail.com");
		request.setPhone("7736153969");
		return request;
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

	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private Response createResponse(String status, String message) {
		Response response = new Response(status, message);
		return response;
	}

}
