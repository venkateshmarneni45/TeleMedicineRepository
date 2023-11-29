package com.example.TeleMedicine.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class UserAuthenticationProvider implements AuthenticationProvider{
	@Autowired
    private UserDetailsService userDetailsService;
    
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username=authentication.getName();
		String password=authentication.getCredentials().toString();
		UserDetails login=userDetailsService.loadUserByUsername(username);
		if(login.getUsername().equals(username)&&login.getPassword().equals(password)) {
			return new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword(),login.getAuthorities());
		}else {
			throw new BadCredentialsException("Invalid Username or Password");
		}

	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
