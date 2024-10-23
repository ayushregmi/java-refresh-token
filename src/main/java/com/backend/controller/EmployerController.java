package com.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.entity.Employer;
import com.backend.service.UserService;

@RestController
@RequestMapping("/api/employer")
public class EmployerController {

	@Autowired
	private UserService userService;
	
	@GetMapping("/get/all")
	public ResponseEntity<?> getAllEmployer(){
		return ResponseEntity.ok(userService.getAllEmployer());
	}
	
	@GetMapping("/get/{employer_id}")
	public ResponseEntity<?> getEmployeeById(@PathVariable("employer_id") int employerId){
		Employer emp = userService.getEmployerById(employerId);
		
		if (emp == null) {
			return new ResponseEntity<String>("Employer not found", HttpStatus.NOT_FOUND);
		}
		
		return ResponseEntity.ok(emp);
	}
	
}
