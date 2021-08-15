package com.jinu.interview.security;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import lombok.Data;
@Data
public class PrincipalUser {
	    private String username;
	    private String password;
	    private String email;
	    private int enabled;
		private int accountNonExpired;
		private int accountNonLocked;
		private List<GrantedAuthority>  authorities;
}
