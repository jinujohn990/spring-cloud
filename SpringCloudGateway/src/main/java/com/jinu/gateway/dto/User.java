package com.jinu.gateway.dto;

import java.util.List;


import lombok.Data;
@Data
public class User {
    private String username;
    private String password;
    private String email;
    private int enabled;
	private int accountNonExpired;
	private int accountNonLocked;
	private List<Authority>  authorities;
	
}
@Data
class Authority{
	private  String role;
}
