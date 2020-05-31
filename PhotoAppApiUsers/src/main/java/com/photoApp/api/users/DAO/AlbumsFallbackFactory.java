package com.photoApp.api.users.DAO;

import org.springframework.stereotype.Component;

import feign.hystrix.FallbackFactory;

@Component
public class AlbumsFallbackFactory implements FallbackFactory<AlbumsServiceClient> {

	@Override
	public AlbumsServiceClient create(Throwable cause) {
		return new AlbumsServiceClientFallback(cause);
	}
}
