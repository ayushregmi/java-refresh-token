package com.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.backend.entity.Employee;
import com.backend.entity.Employer;
import com.backend.entity.Role;
import com.backend.entity.User;
import com.backend.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public User findUserByEmail(String email) {
		return userRepository.findByEmail(email).orElse(null);
	}
	
	public void createUser(User u) {
		
		u.setPassword(passwordEncoder.encode(u.getPassword()));		
		userRepository.save(u);	
	}
	
	public List<Employee> getAllEmployees(){
		return userRepository.findByRole(Role.EMPLOYEE).stream().map(Employee::new).collect(Collectors.toList());
	}
	
	public List<Employer> getAllEmployer(){
		return userRepository.findByRole(Role.EMPLOYER).stream().map(Employer::new).collect(Collectors.toList());
	}
	
	public Employee getEmployeeById(int employeeId) {
		return getAllEmployees().stream().filter(emp->emp.getUserId() == employeeId).findFirst().orElse(null);
	}
	
	public Employer getEmployerById(int employerId) {
		return getAllEmployer().stream().filter(emp->emp.getUserId() == employerId).findFirst().orElse(null);
	}
	
	public User getUserById(int userId) {
		return userRepository.findById(userId).orElse(null);
	}
	
	public User getUserByEmail(String email) {
		return userRepository.findByEmail(email).orElse(null);
	}
	
	
}
