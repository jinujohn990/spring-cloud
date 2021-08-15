package com.jinu.interview.dto.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class User implements Serializable {

	private static final long serialVersionUID = 9219282503878880291L;
	private String username;
    private String password;
    private String email;
    private int enabled;
	private int accountNonExpired;
	private int accountNonLocked;
	private List<Authority>  authorities  = new ArrayList<Authority>();
}