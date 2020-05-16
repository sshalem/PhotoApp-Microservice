package com.photoApp.api.users.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.photoApp.api.users.DAO.UserService;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

	private Environment environment;
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	private UserService userService;

	@Autowired
	public WebSecurity(Environment environment, BCryptPasswordEncoder bCryptPasswordEncoder, UserService userService) {
		this.environment = environment;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.userService = userService;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable();
		http
			.authorizeRequests()			
			.antMatchers("/**").hasIpAddress(environment.getProperty("gateway.ip"))
			.and()
			.addFilter(getAuthenticationFilter());
		http.headers().frameOptions().disable();

	}

	public AuthenticationFilter getAuthenticationFilter() throws Exception {

		/*
		 * I modified here the creation of the new Instance of AuthenticationFilter
		 * because I created a constructor in AuthenticationFilter class. I pass to the
		 * constructor AuthenticationFilter 2 objects which are @Autowired here 1.
		 * userService 2. environment
		 */

		/*
		 * Since I'm passing to the AuthenticationFilter userService & environment I can
		 * comment this line here : ->
		 * authenticationFilter.setAuthenticationManager(authenticationManager());
		 * 
		 * Now , I and pass authenticationManager() as an argument to the Constructor of
		 * AuthenticationFilter
		 */

		AuthenticationFilter authenticationFilter = new AuthenticationFilter(userService, environment,
				authenticationManager());
		authenticationFilter.setFilterProcessesUrl(environment.getProperty("login.url.path"));
		return authenticationFilter;
	}

}
