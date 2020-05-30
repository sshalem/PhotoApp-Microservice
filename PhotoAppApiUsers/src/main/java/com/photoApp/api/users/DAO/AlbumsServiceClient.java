package com.photoApp.api.users.DAO;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.photoApp.api.users.model.AlbumsResponseModel;

// we need to put the name of the micro service as it will be registered with Eureka 
// and we can find it in the application.properties of ALbums Micro serv
@FeignClient(name = "albums-ws")
public interface AlbumsServiceClient {

	@GetMapping("/users/{id}/albums")
	public List<AlbumsResponseModel> getAlbums(@PathVariable("id") String id);
}
