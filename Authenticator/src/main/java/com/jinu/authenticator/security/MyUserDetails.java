package com.jinu.authenticator.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;
public class MyUserDetails implements UserDetails {
	 
    private User user;
     
    public MyUserDetails(User user) {
        this.user = user;
    }
 
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
    	return user.getAuthorities();
    }
 
    @Override
    public String getPassword() {
        return user.getPassword();
    }
 
    @Override
    public String getUsername() {
        return user.getUsername();
    }
 
    @Override
    public boolean isAccountNonExpired() {
    	if(user.getAccountNonExpired() == 1) {
    		return true;
    	}
        return false;
    }
 
    @Override
    public boolean isAccountNonLocked() {
        if(user.getAccountNonLocked() == 1) {
    		return true;
    	}
        return false;
    }
 
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
 
    @Override
    public boolean isEnabled() {
    	 if(user.getEnabled() == 1) {
     		return true;
     	}
         return false;
    }
   
}

