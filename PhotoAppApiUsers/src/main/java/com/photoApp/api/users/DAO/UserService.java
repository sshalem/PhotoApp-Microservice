package com.photoApp.api.users.DAO;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.photoApp.api.users.shared.UserDto;

public interface UserService extends UserDetailsService{

	UserDto createUser(UserDto userDetails);
	
	UserDto getUserDetailsByEmail(String email);
	
	UserDto getUserByUserId(String userId);
	
}
