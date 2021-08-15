package com.jinu.interview.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@Entity
@Table(name = "USER_ROLES")
public class UserRoles {
	/*public UserRolesNew(String userId, String role) {
		super();
		this.userId = userId;
		this.role = role;
	}*/
	public UserRoles(User user, String role) {
		super();
		this.user = user;
		this.role = role;
	}
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "user_role_id" ,unique=true,nullable = false)
	private long userRoleId;
	@ManyToOne
	@JoinColumn(name="userId" ,nullable = false)
	private User user;
	private String role;	
}
