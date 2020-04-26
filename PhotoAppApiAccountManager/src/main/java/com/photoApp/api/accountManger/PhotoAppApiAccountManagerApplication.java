package com.photoApp.api.accountManger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PhotoAppApiAccountManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PhotoAppApiAccountManagerApplication.class, args);
	}

}
