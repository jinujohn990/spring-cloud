package com.jinu.authenticator.security;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
@Data
public class User {
    private String username;
    @JsonIgnore
    private String password;
    private String email;
    private int enabled;
	private int accountNonExpired;
	private int accountNonLocked;
	private List<GrantedAuthority>  authorities;
	//private List<String>  authorities;
	
}
