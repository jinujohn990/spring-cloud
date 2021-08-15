package com.jinu.interview.dto.response;

import java.io.Serializable;

import lombok.Data;

@Data
public class AuthenticatorReponse  implements Serializable {
	
	private static final long serialVersionUID = -4887187863793261319L;
	private String status;
	private String message;
	private User user;
}
