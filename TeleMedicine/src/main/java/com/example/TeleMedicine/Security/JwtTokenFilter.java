package com.example.TeleMedicine.Security;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
	@Value("${jwt.SecretKey}")
	private String secretKey;
	private final RequestMatcher loginRequestMatcher = new AntPathRequestMatcher("/login");

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException, java.io.IOException {
		if (loginRequestMatcher.matches(request)) {
			filterChain.doFilter(request, response);
			return;
		}
		String token = extractTokenFromRequest(request);
		if (StringUtils.hasText(token) && validateToken(token) && !isTokenExpired(token, secretKey)) {
			SecurityContextHolder.getContext().setAuthentication(
					new UsernamePasswordAuthenticationToken(extractUsername(token), null, extractRoles(token)));
		} else {
			throw new BadCredentialsException("Token Not Valid");
		}
		filterChain.doFilter(request, response);
	}

	private String extractTokenFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	public boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@SuppressWarnings("deprecation")
	public String extractUsername(String token) {
		Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
		return claims.getSubject();
	}

	@SuppressWarnings("deprecation")
	public List<GrantedAuthority> extractRoles(String token) {
		Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
		@SuppressWarnings("unchecked")
		List<String> roles = claims.get("roles", List.class);
		return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
	}

	@SuppressWarnings("deprecation")
	public boolean isTokenExpired(String token, String secretKey) {
		try {
			Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
			return claims.getExpiration().before(new Date());
		} catch (ExpiredJwtException ex) {
			return true;
		} catch (Exception ex) {
			return true;
		}
	}
}
