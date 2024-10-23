package com.backend.service;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.entity.RefreshToken;
import com.backend.entity.User;
import com.backend.repository.RefreshTokenRepository;

@Service
public class RefreshTokenService {
	
	private int EXPIRATION = 1000* 60 * 60 * 24;
	
	@Autowired
	private RefreshTokenRepository refreshTokenRepository;
	
	public RefreshToken createRefreshToken(User u) {
		
		RefreshToken refreshToken = RefreshToken.builder()
				.token(UUID.randomUUID().toString())
				.user(u)
				.expiryDate(new Date(System.currentTimeMillis() + EXPIRATION))
				.build();
		return refreshTokenRepository.save(refreshToken);
	}
	
	public RefreshToken findByToken(String token){
		return refreshTokenRepository.findByToken(token).orElse(null);
	}
	
	public boolean isRefreshTokenExpired(RefreshToken token) {
		
		boolean expired = token.getExpiryDate().before(new Date());
		
		if(expired) {
			refreshTokenRepository.delete(token);
		}
		
		return expired;
		
	}
	
	public void deleteToken(RefreshToken refreshToken) {
		refreshTokenRepository.delete(refreshToken);
	}
	
}
