package com.example.TeleMedicine.Security;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {
	@Value("${jwt.ExpirationMs}")
	private long jwtExpirationMs;
	@Value("${jwt.SecretKey}")
	private String secretKey;

	@SuppressWarnings("deprecation")
	public String generateToken(String username, List<String> roles) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + jwtExpirationMs);return Jwts.builder().setSubject(username).claim("roles", roles).setIssuedAt(now).setExpiration(expiryDate)
				.signWith(SignatureAlgorithm.HS256, secretKey).compact();
	}
}
