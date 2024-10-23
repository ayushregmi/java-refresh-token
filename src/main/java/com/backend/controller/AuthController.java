package com.backend.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.dto.LoginRequest;
import com.backend.dto.LoginResponse;
import com.backend.dto.RefreshTokenRequest;
import com.backend.dto.RegisterRequest;
import com.backend.entity.Employee;
import com.backend.entity.Employer;
import com.backend.entity.RefreshToken;
import com.backend.entity.Role;
import com.backend.entity.User;
import com.backend.service.JwtService;
import com.backend.service.RefreshTokenService;
import com.backend.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private UserService userService;

	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private RefreshTokenService refreshTokenService;

	@PostMapping("/register/employee")
	public ResponseEntity<?> registerEmployee(@RequestBody RegisterRequest registerRequest) {

		User u = userService.findUserByEmail(registerRequest.getEmail());

		if (u != null) {
			return new ResponseEntity<String>("Email already exists", HttpStatus.CONFLICT);
		}

		u = User.builder().fullName(registerRequest.getFullName())
				.email(registerRequest.getEmail()).password(registerRequest.getPassword()).role(Role.EMPLOYEE).build();

		userService.createUser(new Employee(u));

		return ResponseEntity.ok("nice");
	}

	@PostMapping("/register/employer")
	public ResponseEntity<?> registerEmployer(@RequestBody RegisterRequest registerRequest) {

		User u = userService.findUserByEmail(registerRequest.getEmail());

		if (u != null) {
			return new ResponseEntity<String>("Email already exists", HttpStatus.CONFLICT);
		}

		u = User.builder().fullName(registerRequest.getFullName())
				.email(registerRequest.getEmail()).password(registerRequest.getPassword()).role(Role.EMPLOYER).build();

		userService.createUser(new Employer(u));

		return ResponseEntity.ok("nice");
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest authRequest) {

		User u = userService.findUserByEmail(authRequest.getEmail());

		if (u == null) {
			return new ResponseEntity<String>("User not found", HttpStatus.NOT_FOUND);
		}

		try {
			authManager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
		Map<String, Object> additionalClaims = new HashMap<String, Object>();
//		additionalClaims.put("role", u.getRole().name());
//		additionalClaims.put("email", u.getEmail());
		
		String jwtToken = jwtService.generateToken(additionalClaims, u);
		
		String refreshToken = refreshTokenService.createRefreshToken(u).getToken();
		
		var response = LoginResponse.builder()
				.authToken(jwtToken)
				.user(u)
				.refreshToken(refreshToken)
				.build();
		
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/refresh")
	public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest){
		
		RefreshToken refreshToken = refreshTokenService.findByToken(refreshTokenRequest.getRefreshToken());
		
		if(refreshToken == null) {
			return new ResponseEntity<String>("Refresh token not found", HttpStatus.NOT_FOUND);
		}
		
		if(refreshTokenService.isRefreshTokenExpired(refreshToken)) {
			return new ResponseEntity<String>("Refresh token expired", HttpStatus.UNAUTHORIZED);
		}
		
		User u = refreshToken.getUser();
		Map<String, Object> additionalClaims = new HashMap<String, Object>();
		
		String jwtToken = jwtService.generateToken(additionalClaims, u);
		
		var response = LoginResponse.builder()
				.authToken(jwtToken)
				.user(u)
				.refreshToken(refreshToken.getToken())
				.build();
		
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/logout")
	public ResponseEntity<?> deleteRefreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest){
		
		RefreshToken refreshToken = refreshTokenService.findByToken(refreshTokenRequest.getRefreshToken());
		
		if(refreshToken != null) {
			refreshTokenService.deleteToken(refreshToken);
		}
		
		return ResponseEntity.ok("Refresht Token deleted");
		
	}

}
