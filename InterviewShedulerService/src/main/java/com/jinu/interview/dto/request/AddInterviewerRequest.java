package com.jinu.interview.dto.request;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = true)
public class AddInterviewerRequest extends AddUserRequest {
	private String name;
}
