package com.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.entity.Employee;
import com.backend.service.EmployeeService;
import com.backend.service.UserService;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private EmployeeService employeeService;
	
	@GetMapping("/get/all")
	public ResponseEntity<?> getAllEmployee(){
		return ResponseEntity.ok(userService.getAllEmployees());
	}
	
	@GetMapping("/get/{employee_id}")
	public ResponseEntity<?> getEmployeeById(@PathVariable("employee_id") int employeeId){
		Employee emp = userService.getEmployeeById(employeeId);
		
		if (emp == null) {
			return new ResponseEntity<String>("Employee not found", HttpStatus.NOT_FOUND);
		}
		
		return ResponseEntity.ok(emp);
	}
	
	@GetMapping("/search/{name}")
	public ResponseEntity<?> searchEmployee(@PathVariable("name") String name){
		return ResponseEntity.ok(employeeService.searchEmployee(name));
	}
	
}
