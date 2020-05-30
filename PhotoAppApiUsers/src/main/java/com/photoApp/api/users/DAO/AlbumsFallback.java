package com.photoApp.api.users.DAO;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.photoApp.api.users.model.AlbumsResponseModel;

@Component
public class AlbumsFallback implements AlbumsServiceClient {

	@Override
	public List<AlbumsResponseModel> getAlbums(String id) {
		// Since this is fallback method,
		// just for the example I return an empty ArrayList
		// I could return What ever I want 
		return new ArrayList<>();
	}

}
