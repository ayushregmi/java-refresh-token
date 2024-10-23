package com.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DefaultController {

	@GetMapping("/auth/hello")
	public ResponseEntity<?> hello(){
		return ResponseEntity.ok("hello");
	}
	
	@GetMapping("/something")
	public ResponseEntity<?> something(){
		return ResponseEntity.ok("Something");
	}
	
}
