package com.photoApp.api.users.DAO;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.photoApp.api.users.model.AlbumsResponseModel;

import feign.FeignException;

public class AlbumsServiceClientFallback implements AlbumsServiceClient {

	Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	private final Throwable cause;

	public AlbumsServiceClientFallback(Throwable cause) {
		this.cause = cause;
	}

	@Override
	public List<AlbumsResponseModel> getAlbums(String id) {

		if (cause instanceof FeignException && ((FeignException) cause).status() == 404) {
			LOGGER.error("404 error took place when getAlbums was called with userId: " + id + " . Error message: "
					+ cause.getLocalizedMessage());
		} else {
			LOGGER.error("other error took plcae : " + cause.getLocalizedMessage());
		}

		return new ArrayList<>();
	}

}
