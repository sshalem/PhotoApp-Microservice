package com.photoApp.api.users.repository;

import org.springframework.data.repository.CrudRepository;

import com.photoApp.api.users.entity.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, Long> {

	UserEntity findByEmail(String email);
	
	UserEntity findByUserId(String userId);
}
