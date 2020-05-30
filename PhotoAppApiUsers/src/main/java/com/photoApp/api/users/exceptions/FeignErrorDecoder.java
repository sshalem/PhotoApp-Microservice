package com.photoApp.api.users.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import feign.Response;
import feign.codec.ErrorDecoder;

public class FeignErrorDecoder implements ErrorDecoder {

	@Override
	public Exception decode(String methodKey, Response response) {
		switch (response.status()) {
		case 400:
			// Do Something
			break;
		case 404:
			// methodKey is the name of the method in the AlbumsServiceClient Class
			if (methodKey.contains("getAlbums")) {
				return new ResponseStatusException(HttpStatus.valueOf(response.status()), "Users albums are not found");
			}
			break;
		default:
			return new Exception(response.reason());
		}
		return null;
	}

}
