package com.photoApp.api.users.DAO;

import java.util.ArrayList;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.photoApp.api.users.entity.UserEntity;
import com.photoApp.api.users.repository.UserRepository;
import com.photoApp.api.users.shared.UserDto;

@Service
public class UserServiceImpl implements UserService {

	private UserRepository userRepo;
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	public UserServiceImpl(UserRepository userRepo, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userRepo = userRepo;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@Override
	public UserDto createUser(UserDto userDetails) {

		userDetails.setUserId(UUID.randomUUID().toString());
		userDetails.setEncryptedPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));

		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		UserEntity userEntity = modelMapper.map(userDetails, UserEntity.class);

		userRepo.save(userEntity);

		UserDto userDto = modelMapper.map(userEntity, UserDto.class);

		return userDto;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		UserEntity userEntity = userRepo.findByEmail(username);
		if (userEntity == null)
			throw new UsernameNotFoundException(username);

		// this User class is from Spring security  
		// org.springframework.security.core.userdetails.User;
		// It extends UserDetails interface 
		// So Instead of Creating my own Custom UserDetails class , i used it from spring framework 
		User user = new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), true, true, true, true,
				new ArrayList<>());
		return user;
	}

	
	/*
	 * created this method in order to use it in the successfulAuthentication method 
	 * in class of AuthenticationFilter.
	 */
	@Override
	public UserDto getUserDetailsByEmail(String email) {
		UserEntity userEntity = userRepo.findByEmail(email);
		
		if (userEntity == null)
			throw new UsernameNotFoundException(email);
		
		ModelMapper modelMapper = new ModelMapper();
		UserDto userDto = modelMapper.map(userEntity, UserDto.class);
		
		return userDto;		
	}

}
