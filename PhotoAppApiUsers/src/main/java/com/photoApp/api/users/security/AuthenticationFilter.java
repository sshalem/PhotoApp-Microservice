package com.photoApp.api.users.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.photoApp.api.users.DAO.UserService;
import com.photoApp.api.users.model.LoginRequestModel;
import com.photoApp.api.users.shared.UserDto;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	// I add these 2 private fields in order to use them here in the class
	// Now, I also need to change in class of WebSecurity the method of
	// getAuthenticationFilter(
	private UserService userService;
	private Environment environment;

	// 1. I'm not AUTOWIRING it here because I am getting it from WebSecurity class
	// 2. I am passing as an argument AuthenticationManager, thus I have 
	// another parameter in the constructor , then I super it 
	public AuthenticationFilter(UserService userService, 
								Environment environment, 
								AuthenticationManager authenticationManager) {		
		super.setAuthenticationManager(authenticationManager);
		this.userService = userService;
		this.environment = environment;

	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {

		try {
			LoginRequestModel creds = new ObjectMapper().readValue(request.getInputStream(), LoginRequestModel.class);
			return getAuthenticationManager().authenticate(
					new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getPassword(), new ArrayList<>()));
		} catch (IOException e) {
			throw new RuntimeException();
		}

	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, 
											HttpServletResponse response, 
											FilterChain chain,
											Authentication auth) throws IOException, ServletException {

		String userName = ((User) auth.getPrincipal()).getUsername();

		UserDto userDetails = userService.getUserDetailsByEmail(userName);

		System.out.println(environment.getProperty("token.expiration_time"));
		
		String token = Jwts.builder()
				.setSubject(userDetails.getUserId())
				.setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(environment.getProperty("token.expiration_time"))))
				.signWith(SignatureAlgorithm.HS512, environment.getProperty("token.secret"))
				.compact();

		response.addHeader("token", token);
		response.addHeader("userId", userDetails.getUserId());
	}

}
