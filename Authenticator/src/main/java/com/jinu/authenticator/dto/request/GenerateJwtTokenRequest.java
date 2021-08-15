package com.jinu.authenticator.dto.request;

import lombok.Data;

@Data
public class GenerateJwtTokenRequest {
private String username;
private String password;
}
