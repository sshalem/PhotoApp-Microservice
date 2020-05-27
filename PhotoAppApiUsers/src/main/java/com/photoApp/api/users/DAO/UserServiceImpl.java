package com.photoApp.api.users.DAO;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.photoApp.api.users.entity.UserEntity;
import com.photoApp.api.users.model.AlbumsResponseModel;
import com.photoApp.api.users.repository.UserRepository;
import com.photoApp.api.users.shared.UserDto;

import feign.FeignException;

@Service
public class UserServiceImpl implements UserService {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	private UserRepository userRepo;
	private BCryptPasswordEncoder bCryptPasswordEncoder;
//	private RestTemplate restTemplate;
	private AlbumsServiceClient albumsServiceClient;
	private Environment env;

	@Autowired
	public UserServiceImpl(UserRepository userRepo, BCryptPasswordEncoder bCryptPasswordEncoder,
			AlbumsServiceClient albumsServiceClient, Environment env) {
		this.userRepo = userRepo;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
//		this.restTemplate = restTemplate;
		this.albumsServiceClient = albumsServiceClient;
		this.env = env;
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
		// So Instead of Creating my own Custom UserDetails class , i used it from
		// spring framework
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

	@Override
	public UserDto getUserByUserId(String userId) {

		UserEntity userEntity = userRepo.findByUserId(userId);
		if (userEntity == null) {
			throw new UsernameNotFoundException("user not found");
		}
		UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);

		// albums-ws , because this is what Eureka discvoery has when Albums Mic serv
		// runs
		/*
		 * RestTemplate Request -------------------- String albumsUrl =
		 * String.format(env.getProperty("albums.url"), userId);
		 * 
		 * ResponseEntity<List<AlbumsResponseModel>> albumsListResponse =
		 * restTemplate.exchange(albumsUrl, HttpMethod.GET, null, new
		 * ParameterizedTypeReference<List<AlbumsResponseModel>>() { });
		 * 
		 * List<AlbumsResponseModel> albumsList = albumsListResponse.getBody();
		 */

		// Feign Client
		List<AlbumsResponseModel> albumsList = null;
		try {
			albumsList = albumsServiceClient.getAlbums(userId);
		} catch (FeignException e) {
			logger.error(e.getLocalizedMessage());
		}

		userDto.setAlbums(albumsList);

		return userDto;
	}

}
