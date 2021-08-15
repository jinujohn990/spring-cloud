package com.jinu.interview.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import com.jinu.interview.controller.InterviewController;
import com.jinu.interview.dto.request.UpdateInterviewDetailsRequest;
import com.jinu.interview.dto.response.Response;
import com.jinu.interview.service.InterviewService;
import com.jinu.interview.serviceimpl.InterviewServiceImpl;

@ExtendWith(MockitoExtension.class)
class InterviewControllerTest {

	MockMvc mvc;
	@Mock
	InterviewServiceImpl interviewService;
	@InjectMocks
	InterviewController interviewController;

	@BeforeEach
	public void setup() {
		JacksonTester.initFields(this, new ObjectMapper());
		mvc = MockMvcBuilders.standaloneSetup(interviewController).build();
	}

	@Test
	public void updateInterviewDetailsTest() throws Exception {
		UpdateInterviewDetailsRequest request = createUpdateInterviewDetailsRequest();
		Response response = createResponse("success", "User created ");
		Mockito.when(interviewService.updateInterviewDetails(Mockito.any(UpdateInterviewDetailsRequest.class)))
				.thenReturn(response);
		mvc.perform(post("/interviewer/updateInterviewDetails").contentType(MediaType.APPLICATION_JSON).content(asJsonString(request)))
				.andExpect(status().isOk());
	}

	private UpdateInterviewDetailsRequest createUpdateInterviewDetailsRequest() {
		UpdateInterviewDetailsRequest request = new UpdateInterviewDetailsRequest();
		request.setInterviewScheduleId(5);
		request.setRemarks("Good");
		request.setStatus("passed");
		return request;
	}

	private Response createResponse(String status, String message) {
		Response response = new Response(status, message);
		return response;
	}

	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
