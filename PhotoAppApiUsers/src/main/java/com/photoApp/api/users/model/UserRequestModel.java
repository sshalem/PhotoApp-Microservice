package com.photoApp.api.users.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserRequestModel {

	@NotNull(message = "first name cannot be null")
	@Size(min = 2, message = "first name cannot be less than 2 characters")
	private String firstName;

	@NotNull(message = "last name cannot be null")
	@Size(min = 2, message = "last name cannot be less than 2 characters")
	private String lastName;

	@NotNull(message = "password must not be null")
	@Size(min = 8, max = 16, message = " 8 < password constrains < 16")
	private String password;

	@NotNull(message = "email cannot be null")
	@Email
	private String email;

	public UserRequestModel() {
		super();
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
