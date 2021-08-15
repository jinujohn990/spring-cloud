package com.jinu.interview.dto.request;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
@Data
public class AddUserRequest implements Serializable{
	private static final long serialVersionUID = -8804080912711756623L;
	private String username;
	private String password;
	private String email;
	private List<String> roles;
}
