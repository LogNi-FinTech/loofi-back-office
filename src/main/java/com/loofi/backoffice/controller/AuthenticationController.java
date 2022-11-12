package com.loofi.backoffice.controller;

import com.loofi.backoffice.request.ResponseToken;
import com.loofi.backoffice.service.AuthenticationService;
import com.loofi.backoffice.entity.User;
import com.loofi.backoffice.request.LoginRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin("*")
public class AuthenticationController {

	@Autowired
	private AuthenticationService authenticationService;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
		ResponseToken responseToken = authenticationService.login(loginRequest);
		return ResponseEntity.ok(responseToken);
	}

	@PostMapping("/signup")
	public ResponseEntity<?> signUp(@RequestBody User user) {
		authenticationService.userSignUp(user);
		return ResponseEntity.ok().body("sing up successfully");
	}
}
