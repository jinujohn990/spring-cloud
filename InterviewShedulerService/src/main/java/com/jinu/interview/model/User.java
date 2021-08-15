package com.jinu.interview.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
@Data
@Entity
@Table(name = "USER")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id" ,unique=true,nullable = false)
	private long userId;
	@Column(name = "username" ,unique=true,nullable = false,length = 50)
    private String username;
    private String password;
    private String email;
    private int enabled;
	private int accountNonExpired;
	private int accountNonLocked;
	@OneToMany(fetch = FetchType.LAZY)
	private List<UserRoles>  userRoles;
		
}
