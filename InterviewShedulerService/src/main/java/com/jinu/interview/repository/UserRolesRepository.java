package com.jinu.interview.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jinu.interview.model.User;
import com.jinu.interview.model.UserRoles;
@Repository
public interface UserRolesRepository extends JpaRepository<UserRoles, String> {
	
    public List<UserRoles> getUserRolesByUser(@Param("user") User user);
}
