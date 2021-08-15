package com.jinu.gateway.dto;

import lombok.Data;

@Data
public class AuthenticatorReponse {
private String status;
private String message;
private User user;
}
