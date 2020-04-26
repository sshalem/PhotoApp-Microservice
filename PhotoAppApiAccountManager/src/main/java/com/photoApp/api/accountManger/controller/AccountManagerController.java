package com.photoApp.api.accountManger.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accountmanager")
public class AccountManagerController {

	@GetMapping("/test")
	public String status() {
		return "account works";
	}
}
