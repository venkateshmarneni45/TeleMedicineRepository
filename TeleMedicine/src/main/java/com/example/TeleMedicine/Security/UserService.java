package com.example.TeleMedicine.Security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserService implements UserDetailsService {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			String query = "select username,password from telemedicine_master.tbl_user_login where username=" + username;
			LoginPojo login = jdbcTemplate.queryForObject(query, (rs, rowNum) -> {
				return new LoginPojo(rs.getString("username"), rs.getString("password"));
			});
			return new User(login.getUsername(), login.getPassword(), getAuthorities(username));

		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
			throw new UsernameNotFoundException("User Not Found");
		}
	}

	public List<GrantedAuthority> getAuthorities(String username) {
		String authoritiesQuery = "select authority from telemedicine_master.tbl_user_authorities where username=?";
		return jdbcTemplate.query(authoritiesQuery, (rs, rowNum) -> {
			return new SimpleGrantedAuthority("ROLE_" + rs.getString("authority"));
		}, username);
	}
}
