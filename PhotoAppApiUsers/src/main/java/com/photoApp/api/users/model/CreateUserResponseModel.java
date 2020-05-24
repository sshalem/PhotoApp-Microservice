package com.photoApp.api.users.model;

import java.util.List;

public class CreateUserResponseModel {

	private String userId;
	private String firstName;
	private String lastName;
	private String email;
	private List<AlbumsResponseModel> albums;

	public CreateUserResponseModel() {
		super();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<AlbumsResponseModel> getAlbums() {
		return albums;
	}

	public void setAlbums(List<AlbumsResponseModel> albums) {
		this.albums = albums;
	}

}
