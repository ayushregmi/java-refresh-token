package com.backend.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	
	private static String SECRET_KEY = "dsfakljoieajflksdjfoisjdfosajdfdjsfoaijdoisjfsfdskl";
	private static int EXPIRATION = 1000 * 60 * 60;
	
	public String extractUsername(String jwtToken) {
		return extractClaim(jwtToken, Claims::getSubject);
	}
	
	private Date extractExpiration(String jwtToken) {
		return extractClaim(jwtToken, Claims::getExpiration);
	}
	
	public <T> T extractClaim(String jwtToken, Function<Claims, T> claimResolver) {
		final Claims claims = this.extractAllClaims(jwtToken);
		return claimResolver.apply(claims);
	}
	
	private Claims extractAllClaims(String jwtToken) {
		return Jwts.parser().verifyWith((SecretKey) getSignInKey()).build().parseSignedClaims(jwtToken).getPayload();
	}
	
	public String generateToken(UserDetails userDetails) {
		return generateToken(new HashMap<String, Object>(), userDetails);
	}
	
	public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
		return Jwts.builder()
				.subject(userDetails.getUsername())
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + EXPIRATION))
				.claims().add(extraClaims).and()
				.signWith(getSignInKey())
				.compact();		
	}

	private Key getSignInKey() {
		
		byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);		
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
	public boolean isTokenValid(String jwtToken, UserDetails userDetails) {
		final String username = extractUsername(jwtToken);
		return  username.equals(userDetails.getUsername()) && !isTokenExpired(jwtToken);
	}

	public boolean isTokenExpired(String jwtToken) {
		return extractExpiration(jwtToken).before(new Date());
	}
	
	
}
