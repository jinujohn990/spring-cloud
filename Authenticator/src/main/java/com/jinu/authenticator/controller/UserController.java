package com.jinu.authenticator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jinu.authenticator.dto.AuthenticatorReponse;
import com.jinu.authenticator.dto.request.GenerateJwtTokenRequest;
import com.jinu.authenticator.service.JwtUtil;
import com.jinu.authenticator.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
    private AuthenticationManager authenticationManager;
	
	@Autowired
	JwtUtil jwtUtil;
	
	@Autowired
	UserService userService;
	
    @PostMapping("/generatetoken")
    @ResponseBody
	public String generateJwtToken(@RequestBody GenerateJwtTokenRequest request) throws Exception {
    	Authentication authentication = null;
    	try {
    		 authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
    	}catch (Exception e) {
    		 throw new Exception("inavalid username/password");
		}
    	return jwtUtil.generateToken(authentication);
    }
    @GetMapping("/getUserfromJwt")
	@ResponseBody
	public AuthenticatorReponse getUser(@RequestHeader("jwt") String jwtToken) throws Exception {
		return userService.getUser(jwtToken);
	}
 

    
}
