package com.photoApp.api.users.controller;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.photoApp.api.users.DAO.UserService;
import com.photoApp.api.users.model.CreateUserResponseModel;
import com.photoApp.api.users.model.UserRequestModel;
import com.photoApp.api.users.model.UserResponseModel;
import com.photoApp.api.users.shared.UserDto;

@RestController
@RequestMapping("/users")
public class UsersController {

	@Autowired
	private Environment env;

	@Autowired
	private UserService userService;

	@GetMapping(path = "/status/check")
	public String status() {
		return "working on port :" + env.getProperty("local.server.port") + " with token = "
				+ env.getProperty("token.secret");
	}

	@PostMapping(path = "/create", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<UserResponseModel> createUser(@Valid @RequestBody UserRequestModel userDetails) {

		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		UserDto userDto = modelMapper.map(userDetails, UserDto.class);

		UserDto createdUser = userService.createUser(userDto);

		UserResponseModel returnedValue = modelMapper.map(createdUser, UserResponseModel.class);

		return new ResponseEntity<UserResponseModel>(returnedValue, HttpStatus.ACCEPTED);
	}

	// create new method , which uses communication between micro services
	@GetMapping(value = "/{userId}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<CreateUserResponseModel> getUser(@PathVariable("userId") String userId) {

		UserDto userDto = userService.getUserByUserId(userId);
		CreateUserResponseModel returnValue = new ModelMapper().map(userDto, CreateUserResponseModel.class);
		return ResponseEntity.status(HttpStatus.OK).body(returnValue);
	}

}
