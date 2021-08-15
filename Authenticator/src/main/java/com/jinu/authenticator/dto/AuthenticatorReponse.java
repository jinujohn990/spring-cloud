package com.jinu.authenticator.dto;

import com.jinu.authenticator.security.User;

import lombok.Data;

@Data
public class AuthenticatorReponse {
private String status;
private String message;
private User user;
}
