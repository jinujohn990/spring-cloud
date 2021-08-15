package com.jinu.authenticator.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.jinu.authenticator.dto.AuthenticatorReponse;
import com.jinu.authenticator.model.User;
import com.jinu.authenticator.model.UserRoles;
import com.jinu.authenticator.repository.UserRepository;
import com.jinu.authenticator.repository.UserRolesRepository;
import com.jinu.authenticator.security.MyUserDetails;
import com.jinu.authenticator.service.JwtUtil;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserRolesRepository userRolesRepository;

	@Autowired
	private JwtUtil jwtUtil;

	public MyUserDetails getUserDetailsFromJwt(String jwtToken) throws Exception {
		String username = jwtUtil.extractUsername(jwtToken);
		User user = userRepository.getUserByUsername(username);
		com.jinu.authenticator.security.User securityUser = createSecurityUserFromUser(user);
		List<UserRoles> userRoles = userRolesRepository.getUserRolesByUser(user);
		List<GrantedAuthority> authorities = userRoles.stream().map(e -> new SimpleGrantedAuthority(e.getRole()))
				.collect(Collectors.toList());
		securityUser.setAuthorities(authorities);
		return new MyUserDetails(securityUser);

	}

	private com.jinu.authenticator.security.User createSecurityUserFromUser(User user) {
		com.jinu.authenticator.security.User securityUser = new com.jinu.authenticator.security.User();
		securityUser.setUsername(user.getUsername());
		//securityUser.setPassword(user.getPassword());
		securityUser.setEmail(user.getEmail());
		securityUser.setAccountNonExpired(user.getAccountNonExpired());
		securityUser.setAccountNonLocked(user.getAccountNonLocked());
		securityUser.setEnabled(user.getEnabled());
		return securityUser;
	}

	public AuthenticatorReponse getUser(String jwtToken) {
		AuthenticatorReponse respose = new AuthenticatorReponse();
		try {
			String username = jwtUtil.extractUsername(jwtToken);
			com.jinu.authenticator.model.User userEntity = userRepository.getUserByUsername(username);
			com.jinu.authenticator.security.User user = createSecurityUserFromUser(userEntity);
			List<UserRoles> userRoles = userRolesRepository.getUserRolesByUser(userEntity);
			List<GrantedAuthority> authorities = userRoles.stream().map(e -> new SimpleGrantedAuthority(e.getRole()))
					.collect(Collectors.toList());
			user.setAuthorities(authorities);
			if (jwtUtil.validateToken(jwtToken, new MyUserDetails(user))) {
				respose.setStatus("success");
				respose.setMessage("User authentication sucessfull");
				respose.setUser(user);
			}else {
				throw new Exception("JWT token not valid");
			}
		} catch (Exception e) {
			respose.setStatus("fail");
			respose.setMessage(e.getMessage());
			respose.setUser(null);
		}
		return respose;
	}

}
